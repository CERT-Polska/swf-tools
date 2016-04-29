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

package pl.nask.swftool.cvejob;

import pl.nask.swftool.plugin.FlashVersion;

public class CveJob {
	private String filename;

	private String id;
	private FlashVersion fv;

	private boolean criticalError = false;

	public CveJob(String id, String filename) {
	    this.id = id;
	    this.filename = filename;
	}

    public boolean isCriticalError() {
		return criticalError;
	}

	public void setCriticalError( boolean criticalError ) {
		this.criticalError = criticalError;
	}

	public String getFilename() {
		return filename;
	}

	public String getId() {
		return id;
	}

	public FlashVersion getFv() {
		return fv;
	}

	public void setFv(FlashVersion fv) {
		this.fv = fv;
	}
}
