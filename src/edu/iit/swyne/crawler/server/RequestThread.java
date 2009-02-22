package edu.iit.swyne.crawler.server;

import java.net.Socket;


public class RequestThread extends Thread {

	private RequestQueue queue;
	private int threadNumber;
	private CrawlerServer server;
	private boolean processing;
	private boolean running;

	public RequestThread(CrawlerServer crawlerServer, RequestQueue requestQueue, int i) throws Exception {
		this.queue = requestQueue;
		this.threadNumber = i;
		this.server = crawlerServer;
	}

	public boolean isProcessing() {
		return this.processing;
	}

	public void killThread() {
		System.out.println("["+this.threadNumber+"]: Attempting to kill thread...");
		this.running = false;
	}
	
	@Override
	public void run() {
		this.running = true;
		while (this.running) {
			Object o = this.queue.getNextObject();
			if (running) {
				Socket s = (Socket) o;
				this.processing = true;
				System.out.println("["+this.threadNumber+"]: Processing request...");
				this.server.getRequestHandlerInstance().handleRequest(s);
				this.processing = false;
				System.out.println("["+this.threadNumber+"]: Finished processing request...");
			}
		}
		System.out.println("["+this.threadNumber+"]: Thread shutting down...");
	}
}
