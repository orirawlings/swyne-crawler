package edu.iit.swyne.crawler.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MockSwyneServer extends Thread {
	
	private static final int DEFAULT_PORT = 6971;
	
	private ServerSocket serverSocket;
	private String message;

	public MockSwyneServer(ServerSocket s) {
		this.serverSocket = s;
	}
	
	public MockSwyneServer() throws IOException {
		this(new ServerSocket(DEFAULT_PORT));
	}

	public void run() {
		String result = "";
		
		try {
			Socket s = serverSocket.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			
			out.println("MockSwyneServer v0.1");
			
			String line;
			while((line = in.readLine()) != null && !line.equals(":[END]:")) {
				System.out.println(line);
				result += line+"\n";
			}
			System.out.println();
			
			in.close();
			out.close();
			s.close();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setMessage(result);
	}

	private synchronized void setMessage(String message) {
		this.message = message;
	}

	public synchronized String getMessage() {
		return message;
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		if (args.length < 1) {
			System.err.println("A port for the server must be specified.");
			System.exit(1);
		}
		MockSwyneServer m = new MockSwyneServer(new ServerSocket(Integer.parseInt(args[0])));
		m.run();
	}
}
