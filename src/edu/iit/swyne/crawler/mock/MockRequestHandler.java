package edu.iit.swyne.crawler.mock;

import java.io.IOException;
import java.net.Socket;

import edu.iit.swyne.crawler.server.RequestHandler;

public class MockRequestHandler implements RequestHandler {

	public void handleRequest(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
