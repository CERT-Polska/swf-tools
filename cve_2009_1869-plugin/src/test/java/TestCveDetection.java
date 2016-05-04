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

import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.Test;

import pl.nask.swf.plugins.cve_2009_1869.cve_2009_1869;


public class TestCveDetection {

	@Test
	public void testWithBenignFile() {
		testWith("standard.swf", false);
	}
	
	@Test
	public void testWithMaliciousFile() {
		testWith("maliciousCVE_2009_1869.swf", true);
	}

	private void testWith(String filename, boolean result) {
		cve_2009_1869 cve = new cve_2009_1869();
		URL res = getClass().getResource("/" + filename);
		boolean r = cve.findCVE(res.getPath());
		Assert.assertEquals(r, result, "CVE detected in " + filename);
	}
}
