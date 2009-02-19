package edu.iit.swyne.crawler.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import edu.iit.swyne.crawler.server.SwyneCrawlerServer;
import edu.iit.swyne.crawler.server.SwyneCrawlerServer.FeedAlreadyTrackedException;

import junit.framework.TestCase;

public class TestSwyneCrawlerServer extends TestCase {
	URL feedURL;
	private Properties testProps = new Properties();
	private SwyneCrawlerServer server;
	
	public TestSwyneCrawlerServer() throws MalformedURLException{
		feedURL = new URL("http://omega.cs.iit.edu/~orawling/iproTesting/news.rss");
		testProps.setProperty("server.socket", "6970");
		testProps.setProperty("indexer.class", "edu.iit.swyne.crawler.mock.MockIndexer");
		testProps.setProperty("server.maxThreads", "5");
		testProps.setProperty("feeds.pollingInterval", "600");
	}
	
	@Override
	protected void setUp() throws Exception {
		server = new SwyneCrawlerServer(testProps);
		server.init();
	}
	
	@Override
	protected void tearDown() throws Exception {
		server.shutdown();
	}
	
	public void testStartAndStopServer() throws Exception {
		assertEquals(false, server.isRunning());
		server.start();
		assertEquals(true, server.isRunning());
		server.shutdown();
		assertEquals(false, server.isRunning());
	}
	
	public void testAddFeedBeforeStartingServer() throws Exception {
		// The server should start up if a request to add a feed has been made
		assertEquals(false, server.isRunning());
		server.addFeed(feedURL);
		assertEquals(true, server.isRunning());
	}
	
	public void testAddFeed() throws Exception {
		server.start();
		server.addFeed(feedURL);
		assertEquals(1, server.numFeedsTracking());
	}
	
	public void testAddFeedTwice() throws Exception {
		server.start();
		server.addFeed(feedURL);
		try {
			server.addFeed(feedURL);
			fail();
		}
		catch(FeedAlreadyTrackedException e) {
			assertNotNull(e.getMessage());
		}
		assertEquals(1, server.numFeedsTracking());
	}
}
