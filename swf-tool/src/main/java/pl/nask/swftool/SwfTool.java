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

package pl.nask.swftool;

import org.apache.log4j.Logger;

import pl.nask.swftool.cvejob.CveJob;
import pl.nask.swftool.cvetool.CveTool;
import pl.nask.swftool.cvetool.detector.CveDetectionResults;
import pl.nask.swftool.helpers.JobResultsPrinter;
import pl.nask.swftool.share.ToolException;

public class SwfTool {
	private static Logger logger = Loggers.APP_LOGGER;
	private static Logger logger_r = Loggers.RES_LOGGER;


	// FIXME: refactoring needed: the similar code is placed in swf-server/Main
	public static void main(String[] args) throws ToolException  {
	    //Properties

	    System.out.println("Usage: ");
	    System.out.println("java -jar ... pathToFile");

	    if (args.length == 0) {
	        System.out.println("No path to file provided");
	        System.exit(0);
	    }

	    String filename = args[0].trim();

	    SwfToolConf prop = new SwfToolConf();
	    prop.printToLogger(logger);

	    // init cve tool
	    CveTool tool = new CveTool();
	    tool.init(prop.getPluginsPathString());

	    // run the job
	    String id = filename;
	    CveJob job = new CveJob(id, filename);
	    CveDetectionResults cdr = tool.detect(job);

	    JobResultsPrinter.printResults(job, cdr);
	}

}
