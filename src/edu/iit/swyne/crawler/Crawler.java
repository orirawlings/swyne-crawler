package edu.iit.swyne.crawler;

import java.net.URL;

public interface Crawler {
	public void init();
	public void start();
	public void shutdown();
	public void addFeed(URL feed, String collection, String extractorClass) throws FeedAlreadyTrackedException, ClassNotFoundException;
	public void removeFeed(URL feed);
	public boolean isRunning();
	public boolean isTrackingFeed(URL feed);
	public int numFeedsTracking();
}
