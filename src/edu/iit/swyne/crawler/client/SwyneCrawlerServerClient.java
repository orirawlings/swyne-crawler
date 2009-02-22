package edu.iit.swyne.crawler.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SwyneCrawlerServerClient implements Runnable {

	private String host;
	private int port;
	private String command;

	public SwyneCrawlerServerClient(String host, int port, String command) {
		this.host = host;
		this.port = port;
		this.command = command;
	}

	public void run() {
		Socket server;
		PrintWriter out = null;
		
		try {
			server = new Socket(this.host, this.port);
			out = new PrintWriter(server.getOutputStream(), true);
			out.println(this.command);
		} catch (UnknownHostException e) {
			System.err.println("ERROR: "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("ERROR: "+e.getMessage());
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

}
