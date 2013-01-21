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

package pl.nask.swftool.cvetool.plugins.loader;

import java.util.Map;

import org.apache.log4j.Logger;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

import pl.nask.swftool.Loggers;
import pl.nask.swftool.plugin.CvePlugin;

public class PluginLoader {
    private static Logger logger = Loggers.APP_LOGGER;

	static String getClassPath( String m ) {

		String path = m.replace( "/", "." );
        path = path.replace( "\\", "." );
        path = path.replace( ".class", "" );

        return path;
	}

	public static boolean isMainClass( String m ) {
		return false;
	}

	private static boolean pathToCVE( String path ) {
		String str[] = path.split("\\.");
		if ( str[str.length-1].substring(0, 3).equals("cve") )
			return true;
		else return false;
	}

	public static CvePlugin loadPlugin( String jarFilePath ) {
	    logger.info("Jar filename: " + jarFilePath );
        JarClassLoader jcl = new JarClassLoader();
        jcl.initialize();
        jcl.add( jarFilePath );

        JclObjectFactory factory1 = JclObjectFactory.getInstance();

        Map<String, byte[]> maps = jcl.getLoadedResources();
        CvePlugin obj = null;

        for ( String m : maps.keySet() ) {


            // If class name is called "Main" is shouldn't be loaded
            if ( isMainClass(m) ) continue;

            String path = getClassPath( m );

            if ( pathToCVE( path ) ) {
                logger.info( "\tResource: " + m );
                logger.info( "\tLoad resource, resource name: " + path );
            	obj = (CvePlugin) factory1.create( jcl, path );
            	logger.info("\tPlugin object created for: " + path );
            }
        }

        return obj;
	}
}
