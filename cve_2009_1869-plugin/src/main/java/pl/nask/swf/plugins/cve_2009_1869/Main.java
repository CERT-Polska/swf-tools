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

package pl.nask.swf.plugins.cve_2009_1869;

public class Main {
	private static final String INFO_MSG = "Program detects CVE-2009-1869 in SWF files.\n"
		+ "Usage:\n"
		+ "\tjava -Djava.ext.dirs=./libs -cp cve_2009_1869_plugin.jar cve_plugins.cve_2009_1869.Main [swf file]\n"
		+ "\texample: java -Djava.ext.dirs=./libs -cp cve_2009_1869_plugin.jar cve_plugins.cve_2009_1869.Main /home/hakier/malicious.swf\n"
		+ "Output:\n"
		+ "\t0 - CVE was not detected in swf file\n"
		+ "\t1 - CVE was detected in the swf file\n"
		+ "Remarks:\n"
		+ "\t1. Directory libs should contain at least plugin.jar and swfutils.jar.\n"
		+ "\t2. swfutils.jar should be from Flex SDK 4.5.\n";

	private Main() {
		// this is utility class
	}

	private static void printUsage() {
		System.out.println(INFO_MSG);
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			printUsage();
			return;
		}

		cve_2009_1869 cve = new cve_2009_1869();

		String filename = args[0];
		boolean result = cve.findCVE(filename);

		if (result) {
			System.out.print("1");
		} else {
			System.out.print("0");
		}
	}
}
