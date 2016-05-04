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

////////////////////////////////////////////////////////////////////////////////
//
//  ADOBE SYSTEMS INCORPORATED
//  Copyright 2003-2007 Adobe Systems Incorporated
//  All Rights Reserved.
//
//  NOTICE: Adobe permits you to use, modify, and distribute this file
//  in accordance with the terms of the license agreement accompanying it.
//
////////////////////////////////////////////////////////////////////////////////

package pl.nask.swf.plugins.cve_2009_1869;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import pl.nask.swftool.plugin.flex.AbstractTagDecoder;
import pl.nask.swftool.plugin.flex.MyAbcPrinter;
import flash.swf.Tag;
import flash.swf.TagHandler;
import flash.swf.TagValues;
import flash.swf.tags.DoABC;

/**
 * A SWF tag decoder.  It is typically used by passing an InputStream
 * to the constructor and then calling parse() with a TagHandler.
 *
 * @author Clement Wong
 */
public final class MyTagDecoder extends AbstractTagDecoder implements TagValues
{
    public MyTagDecoder(InputStream swfIn)
	{
		super(swfIn);
	}

    @Override
    protected boolean decodeTags( TagHandler handler ) throws IOException
    {
    	 int type, h, length, currentOffset;

         do
         {
             currentOffset = r.getOffset();

             type = ( h = r.readUI16() ) >> 6;

             // is this a long tag header (>=63 bytes)?
             if ( ( ( length = h & 0x3F ) == 0x3F ) )
             {
                 // [ed] the player treats this as a signed field and stops if it is negative.
                 length = r.readSI32();

                 if ( length < 0 )
                 {
                 	throw new IllegalStateException( "Negative tag length:" + length + " at offset " + currentOffset );
                 }
             }

             if ( type != 0 )
             {
                if ( type == 82 || type == 72 ) {
                	Tag t = decodeDoABC( type, length );
                	File f = new File ( "tmp" );
                	PrintWriter op = new PrintWriter( f );
                	// 1. print DoABC tag:
                	MyAbcPrinter printer = new MyAbcPrinter( ((DoABC)t).abc, op, false, 3, false );
                	return printer.print();
                } else {
                	byte [] data;
                	try {
                		data = new byte[ length ];
                		r.readFully( data );
                	} catch ( java.lang.OutOfMemoryError ex ) {
                		break;
                	}
                }
             }
         }
         while ( type != 0 );

 		return false;
    }

    private Tag decodeDoABC(int type, int length) throws IOException
	{
		DoABC t;

        if ( type == stagDoABC2 )
        {
            int pos = r.getOffset();
            int skip = r.readSI32();
            String name = r.readString();
            t = new DoABC( name , skip );
            // cannot just use length of string, because might not match
            // the number of bytes in the string for nonascii characters
            //length -= (4 + name.length() + 1);

            length -= ( r.getOffset() - pos );
        }
        else
        {
            t = new DoABC();
        }

		t.abc = new byte[ length ];
		r.readFully( t.abc );
		return t;
	}
}




