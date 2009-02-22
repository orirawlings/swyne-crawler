package edu.iit.swyne.crawler.server;

import java.net.Socket;

public interface RequestHandler {
	public void handleRequest(Socket socket);
}
