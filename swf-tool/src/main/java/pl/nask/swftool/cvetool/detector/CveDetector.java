/*
 * Copyright (c) NASK, NCSC
 * 
 * This file is part of HoneySpider Network 2.0.
 * 
 * This is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.nask.swftool.cvetool.detector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import pl.nask.swftool.Loggers;
import pl.nask.swftool.cvejob.CveJob;
import pl.nask.swftool.plugin.CvePlugin;
import pl.nask.swftool.plugin.FlashVersion;
import pl.nask.swftool.plugin.distributor.PluginsDistributor;

public class CveDetector {

	private final Logger logger = Loggers.RES_LOGGER;

	private PluginsDistributor pd;

	public void readFully( InputStream input, byte[] b ) throws IOException 
	{
		int count = input.read( b );
		if ( count != b.length ) {
			throw new IllegalStateException( "Error: Couldn't read header" );
		}
	}

	private int decodeHeader( InputStream input ) throws IOException  {

    	byte[] sig = new byte[8];

    	readFully( input, sig );

    	if ( !( ( sig[0] == 'C' || sig[0] == 'F' ) && sig[1] == 'W' && sig[2] == 'S') ) {
    		throw new IllegalStateException( "Error: not a SWF file!" );
    	}

    	return sig[3] ; // flash version
	}

	private int getFlashVersion( CveJob job ) {

		File f = null;

		try {

			f = new File( job.getFilename() );

			if ( !f.exists() ) {
			    logger.error( job.getId() + " NOT_FILE" );

				return -1;
			}

		} catch ( Exception ex ) {
		    logger.error( job.getId() + " NOT_FILE" );

			return -1;
		}

		int version = -1;
		InputStream input = null;

		try {
			URL fileUrl = f.toURL();

			input = fileUrl.openStream();
		} catch ( Exception e ) {
		    logger.error( job.getId() + " NOT_FILE" );

			return -1;
		}

		try {
			version = decodeHeader( input );
		} catch (Exception ex) {
		    logger.error( job.getId() + " VERSION" );
			return -1;
		} finally {
			if (input != null) {

				try {
					input.close();
				} catch ( Exception ex ) {
					ex.printStackTrace();
				}
			}
		}

		return version;
	}

	public CveDetector( PluginsDistributor pd ) {
		this.pd = pd;
	}


	public CveDetectionResults detect( CveJob job ) {

		CveDetectionResults cdr = new CveDetectionResults();

		int flashVersion = getFlashVersion( job );

		if ( flashVersion < 0 ) {
			job.setCriticalError( true );
			return cdr;
		}

		logger.info( job.getId() + " VERSION " + flashVersion );

		FlashVersion fv = new FlashVersion(flashVersion, 0);
		job.setFv( fv );
		List<CvePlugin> plugins = pd.getPluginsPerVersion( fv );

		for ( CvePlugin plugin : plugins ) {
			boolean result = false;

			try {

				result = plugin.findCVE( job.getFilename() );

			} catch ( Exception ex ) {
				job.setCriticalError(true);
				logger.error( job.getId() + " PLUGIN " + plugin.getPluginInfo().getCveId());
			}

			if ( result == true ) {

				CveResult cr = new CveResult(plugin.getPluginInfo().getCveId());
				cdr.addResult( cr );
			}

			logger.info( job.getId() + " PLUGIN " + plugin.getPluginInfo().getCveId() + " " + (result?1:0) );
		}

		return cdr;
	}
}
