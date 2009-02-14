/**
 * 
 */
package edu.iit.swyne.crawler.test;

import java.net.MalformedURLException;
import java.net.URL;
import edu.iit.swyne.crawler.FeedListener;
import edu.iit.swyne.crawler.Indexer;
import edu.iit.swyne.crawler.mock.MockIndexer;

import junit.framework.TestCase;

/**
 * @author orirawlings
 *
 */
public class TestFeedListener extends TestCase {
	
	protected final String feedURL = "http://omega.cs.iit.edu/~orawling/iproTesting/news.rss";
	
	protected URL url;
	protected Indexer indexer;
	protected FeedListener ear;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		try {
			url = new URL(feedURL);
		}catch (MalformedURLException mue) {
			System.err.println("ERROR: "+mue.getMessage());
			mue.printStackTrace();
		}
		indexer = new MockIndexer();
		ear = new FeedListener(url);
		ear.setIndexer(indexer);
	}
	
	protected void tearDown() throws Exception {
		ear.destroyCache();
		url = null;
		indexer = null;
		ear = null;
	}
	
	public void testDoesntResubmitEntries() throws Exception {
		ear.run();
		assertEquals(10, ((MockIndexer)indexer).getNumArticles());
		ear.run();
		//Still just ten articles submitted
		assertEquals(10, ((MockIndexer)indexer).getNumArticles());
	}
	
	public void testSimpleListen() throws Exception {
		ear.run();
		assertEquals(10, ((MockIndexer)indexer).getNumArticles());
	}

}
