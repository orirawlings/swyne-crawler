package edu.iit.swyne.crawler.test;

import java.net.ServerSocket;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;
import edu.iit.swyne.crawler.Indexer;
import edu.iit.swyne.crawler.NewsDocument;
import edu.iit.swyne.crawler.SwyneIndexer;
import edu.iit.swyne.crawler.mock.MockSwyneServer;

public class TestSwyneIndexer extends TestCase {

	private static final String COLLECTION = "IIT Tech";
	private static final String TITLE = "An Awesome new RSS Crawler Developed at IIT";
	private static final String ARTICLE = "Today at IIT, a undergraduate computer science major participating in IPRO327 developed a web feed crawler. Ori Rawlings implemented the web feed listener to gather news articles for the teams 'Swyne' system. Swyne, being a semantically aware news search engine. After failures to implement a proper article gathering system in previous semester of the project, Rawlings stepped up to see the task accomplished. Originally planned to be first deployed on February 18th, 2009, some minor hang-ups accumulated during development pushing the inital deployment to February 23rd, 2009.";
	private static final Date DATE = new Date();
	private static final String SOURCE = "http://omega.cs.iit.edu/~orawling/iproTesting/article.html";
	private static final String MESSAGE = "SUBMIT\ntitle: An Awesome new RSS Crawler Developed at IIT\npublished: "+DATE.getTime()+"\nsource: http://omega.cs.iit.edu/~orawling/iproTesting/article.html\ncollection: IIT Tech\narticle: \nToday at IIT, a undergraduate computer science major participating in IPRO327 developed a web feed crawler. Ori Rawlings implemented the web feed listener to gather news articles for the teams 'Swyne' system. Swyne, being a semantically aware news search engine. After failures to implement a proper article gathering system in previous semester of the project, Rawlings stepped up to see the task accomplished. Originally planned to be first deployed on February 18th, 2009, some minor hang-ups accumulated during development pushing the inital deployment to February 23rd, 2009.\n";
	
	private static final String PORT = "6971";
	private static final String HOST = "localhost";
	private Indexer indexer;
	private NewsDocument doc = new NewsDocument();
	private MockSwyneServer server;
	
	public TestSwyneIndexer() {
		doc.setCollection(COLLECTION);
		doc.setTitle(TITLE);
		doc.setArticle(ARTICLE);
		doc.setPublishedDate(DATE);
		doc.setSource(SOURCE);
		
		Properties props = new Properties();
		props.setProperty("crawler.indexer.host", HOST);
		props.setProperty("crawler.indexer.port", PORT);
		
		this.indexer = new SwyneIndexer(props);
	}

	protected void setUp() throws Exception {
		server = new MockSwyneServer(new ServerSocket(Integer.parseInt(PORT)));
		server.start();
	}

	protected void tearDown() throws Exception {
		server.join();
	}
	
	public void testSendDocument() throws Exception {
		indexer.sendDocument(doc);
		server.join();
		assertEquals(MESSAGE, server.getMessage());
	}

}
