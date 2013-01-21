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

package pl.nask.swf.plugins.cve_2007_0071;


import java.io.IOException;
import java.io.InputStream;

import pl.nask.swftool.plugin.CvePlugin;
import pl.nask.swftool.plugin.CvePluginInfo;
import pl.nask.swftool.plugin.FlashVersion;
import pl.nask.swftool.plugin.flex.SimpleTagHandler;
import pl.nask.swftool.plugin.flex.SwfParser;

public class cve_2007_0071  extends SwfParser implements CvePlugin {
	private final CvePluginInfo info;

	public cve_2007_0071() {
	    String desc = "Integer overflow in Adobe Flash Player 9.0.115.0 and earlier, and 8.0.39.0 and earlier," +
        " allows remote attackers to execute arbitrary code via a crafted SWF file with " +
        "a negative Scene Count value, which passes a signed comparison, is used as an offset " +
        "of a NULL pointer, and triggers a buffer overflow. ";

	    info = new CvePluginInfo("CVE_2007_0071", desc, new FlashVersion(6, 0), new FlashVersion(7, 0), new FlashVersion(8, 0), new FlashVersion(9, 0));
    }

	@Override
	public boolean findCVE( String swfFilename ) {
		try {
			return this.parseSwf(swfFilename);
		} catch ( Exception e ) {
			System.err.print( "Error: swf file parsing error: " + e.getMessage() );
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected boolean doParseSwf(InputStream is) throws IOException {
		MyTagDecoder decoder = new MyTagDecoder(is);
		return decoder.parse(new SimpleTagHandler());
	}

    @Override
    public CvePluginInfo getPluginInfo() {
        return info;
    }

    @Override
    public boolean supportsFlashVersion(FlashVersion version) {
    	return version.getMajorVersion() >= 6 && version.getMajorVersion() <= 9;
    }
}
