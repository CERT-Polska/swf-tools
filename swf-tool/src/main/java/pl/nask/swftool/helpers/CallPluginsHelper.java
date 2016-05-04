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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import pl.nask.swftool.Loggers;
@Deprecated
public class CallPluginsHelper {
	 private static Logger logger = Loggers.APP_LOGGER;

	 public static String getDescription( Object cveplugin ) {
	        // TYPES CONVERSION:
	        String desc = (String)callMethod( cveplugin, "getDescription" );

	        return  desc;
	 }

	 public static boolean findCVE( Object cveplugin, String swfFilename ) {
		 boolean result = (boolean)callMethodString( cveplugin, "findCVE", swfFilename );

		 return result;
	 }

	 private static Object callMethod( Object obj, String methodName ) {
		 Method meth = null;

		 try {
			 meth = obj.getClass().getMethod( methodName );
		 } catch ( NoSuchMethodException ex ) {
			 logger.error( "Error: No such method!" );
		 }

		 Object ret = null;
	     try {

	    	 ret = meth.invoke( obj );

	     } catch (IllegalAccessException ex) {

	    	 logger.error("Error: Illegal access!" );

	     } catch (InvocationTargetException ex) {

	    	 logger.error("Error: Invocation target!" );
	     }

	     return ret;
	 }

	 private static boolean callMethodString( Object obj, String methodName, String filename ) {
	        Method meth = null;

	        try {
	            meth = obj.getClass().getMethod( methodName, new Class[]{String.class} );
	        } catch ( NoSuchMethodException ex ) {
	        	logger.error("FeatureCallHelper.callMethod: No such method exception !!!!" );
	        }

	        boolean ret = false;

	        try {
	            ret = ((Boolean)meth.invoke( obj, new Object[]{filename} )).booleanValue();
	        } catch ( IllegalAccessException ex ) {
	        	logger.error( "FeatureCallHelper.callMethod: Illegal access exception !!!!" );
	        } catch ( InvocationTargetException ex ) {
	        	logger.error( "FeatureCallHelper.callMethod: Invocation target exception  !!!!" );
	        }

	        return ret;
	    }

}
