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

package pl.nask.swftool.plugin.distributor;

import java.util.ArrayList;
import java.util.List;

import pl.nask.swftool.plugin.CvePlugin;
import pl.nask.swftool.plugin.FlashVersion;

public class PluginsDistributor {

	private List<CvePlugin> plugins = new ArrayList<CvePlugin>();

    public void addPlugins( List<CvePlugin> plugins ) {
		this.plugins.addAll(plugins);
	}

	public List<CvePlugin> getPluginsPerVersion( FlashVersion fv ) {
	    // TODO: add caching queriers in the hashMap
		List<CvePlugin> res = new ArrayList<CvePlugin>();
		for (CvePlugin p: plugins) {
		    if (p.supportsFlashVersion(fv))
		        res.add(p);
		}

		return res;
	}
}
