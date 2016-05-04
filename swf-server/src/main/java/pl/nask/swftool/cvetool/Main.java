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

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

import pl.nask.swftool.Loggers;
import pl.nask.swftool.SwfToolConf;
import pl.nask.swftool.cvejob.CveJob;
import pl.nask.swftool.cvetool.detector.CveDetectionResults;
import pl.nask.swftool.cvetool.server.TcpServer;
import pl.nask.swftool.helpers.JobResultsPrinter;
import pl.nask.swftool.share.ToolException;

public class Main {

	private static final Logger logger_a = Loggers.APP_LOGGER;
	private static final Logger logger_r = Loggers.RES_LOGGER;

	private static BlockingQueue<String> queue = new LinkedBlockingDeque<String>();

    private static TcpServer server;

	public static void main( String[] args ) throws Exception {
		SwfToolConf prop = initConfig(args);
		CveTool ct = initCveTool(prop);

		startTcpServer(prop);

		logger_a.info( "queuing..." );
		while ( true ) {
		    String id = queue.take();
		    processJob(prop, ct, id);
		}
	}

	private static void processJob(SwfToolConf prop, CveTool ct, String id) {
	    try {
	        logger_r.info( id + " ACCEPTED" );
	        CveJob job = new CveJob(id, prop.getInputPathString() + System.getProperty("file.separator") + id);
	        CveDetectionResults cdr = ct.detect( job );
	        JobResultsPrinter.printResults(job, cdr);
	    } catch (Exception e) {
	        logger_a.error("Got an excepion processing job " + id, e);
	        logger_a.info("Keep running");
	    }
    }

    private static void startTcpServer(SwfToolConf prop) {
        logger_a.info( "starting server..." );
        try {
            server = new TcpServer( new Integer(prop.getPortString()).intValue(), prop.getAddressString(), queue );
            server.start();
        } catch( IOException e ) {
            logger_a.error( "Server error: " + e );
            throw new IllegalStateException("Failed to start TcpServer", e);
        }
        logger_a.info( "server started" );
    }

    private static CveTool initCveTool(SwfToolConf prop) {
	    CveTool ct = new CveTool();
	    ct.init( prop.getPluginsPathString() );
	    return ct;
	}

    private static SwfToolConf initConfig(String[] args) {
        try {

            SwfToolConf prop;
            if ( args.length == 0 ) {
                logger_a.info( "Tool Default" );
                prop = new SwfToolConf();
            } else {
                logger_a.info( "Tool Arg" );
                prop = new SwfToolConf( args[0] );
            }

            prop.printToLogger(logger_a);
            return prop;
        } catch ( ToolException ex ) {
            logger_a.error( "mes: " +  ex.getMessage() );
            logger_a.error( "critical: " +  ex.getCritical() );
            throw new IllegalStateException("Config not initialized");
        }
    }
}
