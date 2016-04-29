/*
 * Copyright (c) NASK, NCSC
 * 
 * This file is part of HoneySpider Network 2.1.
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

package pl.nask.swftool.cvetool;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.nask.swftool.Loggers;
import pl.nask.swftool.cvejob.CveJob;
import pl.nask.swftool.cvetool.detector.CveDetectionResults;
import pl.nask.swftool.cvetool.detector.CveDetector;
import pl.nask.swftool.cvetool.plugins.loader.PluginLoader;
import pl.nask.swftool.helpers.FilesystemHelper;
import pl.nask.swftool.plugin.CvePlugin;
import pl.nask.swftool.plugin.CvePluginInfo;
import pl.nask.swftool.plugin.FlashVersion;
import pl.nask.swftool.plugin.distributor.PluginsDistributor;

public class CveTool {

	private List<CvePlugin> plugins = new ArrayList<CvePlugin>();
	private PluginsDistributor distributor = new PluginsDistributor();
	private CveDetector detector;

	private Logger logger = Loggers.APP_LOGGER;

	public void init(String pathToPluginDirectory) {
	    loadPlugins(pathToPluginDirectory);
	    printPluginsInfo();
	    bulidPluginsDistributor();
	}

	// load plugins from directory
	public void loadPlugins( String pathToDirectory ) {
		logger.info( "loading plugins..." );

		// 1. load list of jar files (form "pathToDirectory")
		ArrayList<String> jarFiles = FilesystemHelper.dirCommand( pathToDirectory );

		logger.info( "Number of jar files: " + jarFiles.size() );
		for ( String jarFileName : jarFiles ) {
		    String pluginPath = pathToDirectory + System.getProperty("file.separator") + jarFileName;
		    CvePlugin obj = PluginLoader.loadPlugin( pluginPath );
		    if (obj != null) {
		        plugins.add( obj );
		    } else {
		        logger.info("Skipping (not a plugin): " + pluginPath);
		    }
		}
	}

	public void printPluginsInfo() {

		//print debbuging information
	    logger.info( "plugins available:" );

		for ( CvePlugin plugin : plugins ) {
		    CvePluginInfo info = plugin.getPluginInfo();

			Loggers.APP_LOGGER.info( "\tCVE id: " + info.getCveId() );
			Loggers.APP_LOGGER.info( "\tCVE description: " );
			Loggers.APP_LOGGER.info( "\t\t" + info.getDescription() );

			Loggers.APP_LOGGER.info( "\tSupported Flash versions: " );

			StringBuilder sb = new StringBuilder("\t\t");
			for ( FlashVersion fv : info.getFlashVersions() ) {
				sb.append(fv.getMajorVersion()).append(" ");
			}
			logger.info( sb );
		}
	}

	public void bulidPluginsDistributor() {
		distributor.addPlugins( plugins );
		this.detector = new CveDetector( distributor );
	}

	public CveDetectionResults detect( CveJob job ) {
		return this.detector.detect( job );
	}
}
