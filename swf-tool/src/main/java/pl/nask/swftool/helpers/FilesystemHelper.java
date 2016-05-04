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

package pl.nask.swftool.helpers;

import java.io.File;
import java.util.ArrayList;

public class FilesystemHelper {

	public static  ArrayList<String> dirCommand( String directory ) {
		ArrayList<String> lst = new ArrayList<String>();

		File dir = new File( directory );

		String[] children = dir.list();

		for ( String s : children ) {
			lst.add( s );
		}

		return lst;
	}
}
