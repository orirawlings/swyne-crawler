package edu.iit.swyne.crawler.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.InvalidPropertiesFormatException;

import junit.framework.TestCase;
import edu.iit.swyne.crawler.Crawler;
import edu.iit.swyne.crawler.FeedAlreadyTrackedException;
import edu.iit.swyne.crawler.SwyneCrawler;

public class TestSwyneCrawler extends TestCase {
	private URL feedURL;
	private Crawler crawler;
	
	public TestSwyneCrawler() throws InvalidPropertiesFormatException, FileNotFoundException, IOException{
		feedURL = new URL("http://omega.cs.iit.edu/~orawling/iproTesting/news.rss");
	}
	
	@Override
	protected void setUp() throws Exception {
		this.crawler = new SwyneCrawler();
		this.crawler.init();
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.crawler.shutdown();
	}
	
	public void testStartAndStopCrawler() throws Exception {
		assertFalse(this.crawler.isRunning());
		this.crawler.start();
		assertTrue(this.crawler.isRunning());
		this.crawler.shutdown();
		assertFalse(this.crawler.isRunning());
	}
	
	public void testAddFeedBeforeStartingCrawler() throws Exception {
		// The crawler should start up if a request to add a feed has been made
		assertFalse(this.crawler.isRunning());
		this.crawler.addFeed(feedURL, "LA Times", "edu.iit.swyne.crawler.extractor.LATimesExtractor");
		assertTrue(this.crawler.isRunning());
	}
	
	public void testAddFeed() throws Exception {
		this.crawler.start();
		this.crawler.addFeed(feedURL, "LA Times", "edu.iit.swyne.crawler.extractor.LATimesExtractor");
		assertEquals(1, this.crawler.numFeedsTracking());
		assertTrue(this.crawler.isTrackingFeed(feedURL));
	}
	
	public void testAddFeedTwice() throws Exception {
		this.crawler.start();
		this.crawler.addFeed(feedURL, "LA Times", "edu.iit.swyne.crawler.extractor.LATimesExtractor");
		try {
			this.crawler.addFeed(feedURL, "LA Times", "edu.iit.swyne.crawler.extractor.LATimesExtractor");
			fail();
		}
		catch(FeedAlreadyTrackedException e) {
			assertNotNull(e.getMessage());
		}
		assertEquals(1, this.crawler.numFeedsTracking());
		assertTrue(this.crawler.isTrackingFeed(feedURL));
	}
	
	public void testRemoveFeed() throws Exception {
		this.crawler.start();
		this.crawler.addFeed(feedURL, "LA Times", "edu.iit.swyne.crawler.extractor.LATimesExtractor");
		assertEquals(1, this.crawler.numFeedsTracking());
		assertTrue(this.crawler.isTrackingFeed(feedURL));
		this.crawler.removeFeed(feedURL);
		assertEquals(0, this.crawler.numFeedsTracking());
		assertFalse(this.crawler.isTrackingFeed(feedURL));
	}
}
