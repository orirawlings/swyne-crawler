package edu.iit.swyne.crawler.mock;

import java.util.Properties;

import edu.iit.swyne.crawler.Crawler;
import edu.iit.swyne.crawler.server.CrawlerServer;
import edu.iit.swyne.crawler.server.RequestHandler;

public class MockCrawlerServer implements CrawlerServer {
	private Crawler crawler;
	private boolean running;

	public MockCrawlerServer() throws Exception {
		this.crawler = new MockCrawler();
		this.crawler.init();
		this.crawler.start();
	}
	
	public MockCrawlerServer(Properties props) throws Exception {
		this();
	}
	
	public Crawler getCrawler() {
		return this.crawler;
	}
	
	public boolean isRunning() {
		return running;
	}

	public synchronized void startServer() {
		this.running = true;
	}
	
	public synchronized void stopServer() {
		this.running = false;
	}

	public RequestHandler getRequestHandlerInstance() {
		return new MockRequestHandler();
	}
}
