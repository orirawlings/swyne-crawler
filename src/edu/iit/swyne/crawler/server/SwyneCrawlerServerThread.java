package edu.iit.swyne.crawler.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SwyneCrawlerServerThread implements RequestHandler {

	private SwyneCrawlerServer server;

	public SwyneCrawlerServerThread(SwyneCrawlerServer server) {
		this.server = server;
	}

	public void handleRequest(Socket socket) {
		BufferedReader in;
		PrintWriter out;
		String line, response = SwyneCrawlerServer.NAME+" "+SwyneCrawlerServer.VERSION, message = "";
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			while ((line = in.readLine()) != null)
				message += line + "\n";
			
			response = SwyneCrawlerServerProtocol.run(message, this.server);
			out.println(response);
			
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.err.println("ERROR: Problem connecting with client");
			e.printStackTrace();
		}
	}
}
