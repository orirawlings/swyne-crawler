package edu.iit.swyne.crawler.mock;

import java.net.URL;
import java.util.HashMap;

import edu.iit.swyne.crawler.Crawler;
import edu.iit.swyne.crawler.FeedAlreadyTrackedException;

public class MockCrawler implements Crawler {
	
	private HashMap<URL, Boolean> trackedFeeds;
	private boolean listening;
	private boolean initialized;
	
	public void init() {
		if (!this.initialized) {
			this.initialized = true;
			this.trackedFeeds = new HashMap<URL, Boolean>();
		}
	}
	
	public synchronized void start() {
		if (!this.listening) {
			if (!this.initialized) this.init();
			this.listening = true;	
		}
	}
	
	public synchronized void shutdown() {
		this.listening = false;
	}
	
	public boolean isTrackingFeed(URL feedURL) {
		Boolean value = this.trackedFeeds.get(feedURL);
		return value == null ? false : value.booleanValue();
	}
	
	public void removeFeed(URL feedURL) {
		this.trackedFeeds.put(feedURL, false);
	}

	public boolean isRunning() {
		return this.listening;
	}

	public int numFeedsTracking() {
		return this.trackedFeeds.size();
	}

	public void addFeed(URL feedURL, String collection, String extractorClass) throws FeedAlreadyTrackedException, ClassNotFoundException {
		if (this.trackedFeeds.containsKey(feedURL) && this.trackedFeeds.get(feedURL).booleanValue())
			throw new FeedAlreadyTrackedException("Feed " + feedURL.toString() + " has been previously added.");
		this.trackedFeeds.put(feedURL, new Boolean(true));		
	}
}
