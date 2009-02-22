package edu.iit.swyne.crawler.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RequestQueue {

	private String requestHandlerName;
	private int maxQueueSize;
	private int maxThreads;
	private int minThreads;
	private List<RequestThread> threadPool = new ArrayList<RequestThread>();
	private ArrayList<Object> queue = new ArrayList<Object>();
	private int currentNumThreads;
	private boolean running = true;
	private CrawlerServer server;

	public RequestQueue(SwyneCrawlerServer server, String requestHandlerName, int maxQueueSize, int maxThreads, int minThreads) throws Exception {
		this.requestHandlerName = requestHandlerName;
		this.maxQueueSize = maxQueueSize;
		this.maxThreads = maxThreads;
		this.minThreads = minThreads;
		this.currentNumThreads = minThreads;
		this.server = server;
		
		for (int i = 0; i < this.minThreads; i++) {
			RequestThread thread = new RequestThread(this.server, this, i);
			thread.start();
			this.threadPool.add(thread);
		}
	}

	public String getRequestHandlerClassName() {
		return requestHandlerName;
	}

	public synchronized void add(Object o) throws RequestQueueException {
		if (this.queue.size() > this.maxQueueSize) throw new RequestQueueException("Request queue full. Max size: " + this.maxQueueSize);
		
		this.queue.add(o);
		
		boolean threadAvailable = false;
		for (Iterator<RequestThread> iterator = this.threadPool.iterator(); iterator.hasNext();) {
			RequestThread thread = iterator.next();
			if (!thread.isProcessing()) {
				threadAvailable = true;
				break;
			}
		}
		
		if (!threadAvailable && this.currentNumThreads < this.maxThreads) {
			System.out.println("Creating additional request thread...");
			RequestThread thread;
			try {
				thread = new RequestThread(this.server, this, this.currentNumThreads++);
				thread.start();
				this.threadPool.add(thread);
			} catch (Exception e) {
				System.err.println("ERROR: Creating new request thread failed");
				e.printStackTrace();
			}
		}
		
		notifyAll();
	}
	
	public synchronized Object getNextObject() {
		while (this.queue.isEmpty())
			try {
				if (!this.running) return null;
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return this.queue.remove(0);
	}

	public synchronized void shutdown() {
		System.out.println("Shutting down request queue...");
		this.running = false;
		for (Iterator<RequestThread> iterator = this.threadPool.iterator(); iterator.hasNext();) {
			RequestThread thread = iterator.next();
			thread.killThread();
		}
		
		notifyAll();
	}

	@SuppressWarnings("serial")
	public class RequestQueueException extends Exception {
		public RequestQueueException(String string) { super(string); }
	}

}
