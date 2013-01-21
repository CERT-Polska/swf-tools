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

package pl.nask.swftool.helpers;

import org.apache.log4j.Logger;

import pl.nask.swftool.Loggers;
import pl.nask.swftool.cvejob.CveJob;
import pl.nask.swftool.cvetool.detector.CveDetectionResults;
import pl.nask.swftool.cvetool.detector.CveResult;

public class JobResultsPrinter {
    private static final Logger logger = Loggers.RES_LOGGER;
    public static void printResults(CveJob job, CveDetectionResults cdr) {
        StringBuilder cves = new StringBuilder();
        if ( job.isCriticalError() ) {
            logger.error( job.getId() + " DONE" );
        } else if ( cdr.getResults().size() > 0 ) {
            for ( CveResult c : cdr.getResults() ) {
                cves.append(" ").append(c.getId());
            }
        }

        logger.info( job.getId() + " DONE " + job.getFv().getMajorVersion() + " " + (cdr == null ? 0 : cdr.getResults().size()) + cves.toString());
    }

}
