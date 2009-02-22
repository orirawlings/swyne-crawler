package edu.iit.swyne.crawler.server;

import edu.iit.swyne.crawler.Crawler;

public interface CrawlerServer {
	public void startServer();
	public void stopServer();
	public boolean isRunning();
	public Crawler getCrawler();
	public RequestHandler getRequestHandlerInstance();
}
