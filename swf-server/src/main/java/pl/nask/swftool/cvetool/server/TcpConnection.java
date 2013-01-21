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

package pl.nask.swftool.cvetool.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpConnection extends Thread {

	private BlockingQueue<String> qe;
	private Socket socket;

	private final static Logger LOGGER = LoggerFactory.getLogger(TcpConnection.class);
	
	public TcpConnection(Socket socket, BlockingQueue<String> qe) throws IOException {
		this.socket = socket;
	    this.qe = qe;
	}

	public void run() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true)
		){
			while (true) {
				String str = in.readLine();
				if (str != null) {
					str = str.trim();
					if (str.equals("END")){
						break;
					}
				}
				qe.put(str);
				out.println(str);
			}
		} catch (IOException | InterruptedException e) { 
			LOGGER.error(e.getMessage(), e);
        }
	}
}
