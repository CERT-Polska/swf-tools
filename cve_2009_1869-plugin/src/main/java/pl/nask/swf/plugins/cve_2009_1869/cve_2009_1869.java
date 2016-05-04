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

package pl.nask.swf.plugins.cve_2009_1869;

import java.io.IOException;
import java.io.InputStream;

import pl.nask.swftool.plugin.CvePlugin;
import pl.nask.swftool.plugin.CvePluginInfo;
import pl.nask.swftool.plugin.FlashVersion;
import pl.nask.swftool.plugin.flex.SimpleTagHandler;
import pl.nask.swftool.plugin.flex.SwfParser;

public class cve_2009_1869 extends SwfParser implements CvePlugin {
	private final CvePluginInfo info;

    public cve_2009_1869() {
	    String desc = "Integer overflow in the ActionScript Virtual Machine 2 (AVM2) abcFile parser " +
        "in Adobe Flash Player before 9.0.246.0 and 10.x before 10.0.32.18, and Adobe AIR before 1.5.2," +
        " allows attackers to cause a denial of service (application crash) or possibly execute " +
        "arbitrary code via an AVM2 file with a large intrf_count value that triggers a " +
        "dereference of an out-of-bounds pointer. ";
	    this.info = new CvePluginInfo("CVE_2009_1869", desc, new FlashVersion(6,0), new FlashVersion(7,0), new FlashVersion(8,0), new FlashVersion(9,0), new FlashVersion(10, 0));
    }

    @Override
    public CvePluginInfo getPluginInfo() {
        return info;
    }

    @Override
    public boolean supportsFlashVersion(FlashVersion version) {
        return version.getMajorVersion() >= 6 && version.getMajorVersion() <= 10;
    }

	@Override
	public boolean findCVE( String swfFilename ) {
		try {
			return parseSwf(swfFilename);
		} catch ( Exception e ) {
			System.err.print( "Error: swf file parsing error: " + e.getMessage() );
			return false;
		}
	}
	
	@Override
	protected boolean doParseSwf(InputStream is) throws IOException {
		MyTagDecoder decoder = new MyTagDecoder(is);		
		return decoder.parse(new SimpleTagHandler());		
	}
}
