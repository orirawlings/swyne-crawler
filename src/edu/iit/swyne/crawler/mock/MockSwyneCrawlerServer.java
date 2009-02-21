package edu.iit.swyne.crawler.mock;

import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import edu.iit.swyne.crawler.server.SwyneCrawlerServer;

public class MockSwyneCrawlerServer extends SwyneCrawlerServer {

	private HashMap<URL, Boolean> trackedFeeds;

	public MockSwyneCrawlerServer(Properties props) {
		super(props);
	}
	
	@Override
	public void init() {
		if (!initialized) {
			initialized = true;
			trackedFeeds = new HashMap<URL, Boolean>();
		}
	}
	
	@Override
	public void start() {
		if (!initialized) {
			this.init();
		}
		listening = true;
	}
	
	@Override
	public void shutdown() {
		listening = false;
	}
	
	@Override
	public void addFeed(URL feedURL) throws FeedAlreadyTrackedException {
		if (trackedFeeds.containsKey(feedURL) && trackedFeeds.get(feedURL).booleanValue())
			throw new FeedAlreadyTrackedException("Feed " + feedURL.toString() + " has been previously added.");
		trackedFeeds.put(feedURL, new Boolean(true));
	}
	
	@Override
	public boolean isTrackingFeed(URL feedURL) {
		Boolean value = trackedFeeds.get(feedURL);
		return value == null ? false : value.booleanValue();
	}
	
	@Override
	public void removeFeed(URL feedURL) {
		trackedFeeds.put(feedURL, false);
	}
}
