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
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServer extends Thread {

	private BlockingQueue<String> qe;
	private int port;
	private InetAddress address;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);
	
	public TcpServer(int port, String address, BlockingQueue<String> qe) throws IOException {
		this.address = InetAddress.getByName(address);
		this.qe = qe;
	}

	public void run() {
		try (ServerSocket serverSocket = new ServerSocket( port, 0, address);) {
			while (true) {
				try (Socket socket = serverSocket.accept()){
					new TcpConnection(socket, qe).start();
				} catch (IOException e) {
					LOGGER.debug("", e);
				}
			}			
		} catch (IOException e) {
			LOGGER.debug("", e);
		}
	}
}
