package edu.iit.swyne.crawler.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import junit.framework.TestCase;
import edu.iit.swyne.crawler.mock.MockCrawlerServer;
import edu.iit.swyne.crawler.server.CrawlerServer;
import edu.iit.swyne.crawler.server.SwyneCrawlerServerProtocol;

public class TestSwyneCrawlerServerProtocol extends TestCase {
	private final String SERVER_CLASS = "edu.iit.swyne.crawler.mock.MockCrawlerServer";
//	private final String SERVER_CLASS = "edu.iit.swyne.crawler.SwyneCrawlerServer";
	
	private Properties props = new Properties();
	private URL feedURL;
	private CrawlerServer server;
	
	public TestSwyneCrawlerServerProtocol() throws MalformedURLException {
		feedURL = new URL("http://omega.cs.iit.edu/~orawling/iproTesting/news.rss");
		props.setProperty("server.class", SERVER_CLASS);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void setUp() throws Exception {
		Class serverClass = Class.forName(props.getProperty("server.class"));
		server = (CrawlerServer) serverClass.newInstance();
		server = new MockCrawlerServer();
		server.startServer();
	}
	
	@Override
	protected void tearDown() throws Exception {
		server.stopServer();
//		Thread.sleep(15000);
	}
	
	public void testShutdown() throws Exception {
		String command = "shutdown";
		
		assertTrue(server.isRunning());
		assertEquals(SwyneCrawlerServerProtocol.SHUTDOWN_SUCCESS_MESSAGE, SwyneCrawlerServerProtocol.run(command, server));
		assertFalse(server.isRunning());
	}
	
	public void testUnknownCommand() throws Exception {
		assertTrue(server.isRunning());
		
		String command = "make me a sandwich";
		assertEquals(SwyneCrawlerServerProtocol.UNKNOWN_COMMAND_MESSAGE+" \""+command+"\"\n", SwyneCrawlerServerProtocol.run(command, server));
		
		command = "sudo "+command;
		assertEquals(SwyneCrawlerServerProtocol.UNKNOWN_COMMAND_MESSAGE+" \""+command+"\"\n", SwyneCrawlerServerProtocol.run(command, server));
		
		assertTrue(server.isRunning());
	}
	
	public void testUnexpectedError() throws Exception {
		server.stopServer();
		assertFalse(server.isRunning());
		
		String command = "add "+feedURL.toString();
		assertEquals(SwyneCrawlerServerProtocol.UNEXPECTED_ERROR_MESSAGE+" \"Swyne crawler server not running.\"\n", SwyneCrawlerServerProtocol.run(command, server));
	}
	
	public void testAddFeed() throws Exception {
		String command = "add "+feedURL.toString();
		
		assertEquals(SwyneCrawlerServerProtocol.ADD_SUCCESS_MESSAGE+" "+feedURL.toString()+"\n", SwyneCrawlerServerProtocol.run(command, server));
		assertTrue(server.getCrawler().isTrackingFeed(feedURL));
	}
	
	public void testAddFeedTwice() throws Exception {
		String command = "add "+feedURL.toString();
		
		assertEquals(SwyneCrawlerServerProtocol.ADD_SUCCESS_MESSAGE+" "+feedURL.toString()+"\n", SwyneCrawlerServerProtocol.run(command, server));
		assertTrue(server.getCrawler().isTrackingFeed(feedURL));
		
		assertEquals(SwyneCrawlerServerProtocol.ADD_FAILURE_MESSAGE_ALREADY_TRACKED+" "+"Feed " + feedURL.toString() + " has been previously added.\n", SwyneCrawlerServerProtocol.run(command, server));
	}
	
	public void testAddingFeedWithoutURL() throws Exception {
		String command = "add";
		
		assertEquals(SwyneCrawlerServerProtocol.ADD_FAILURE_MESSAGE_NO_URL_GIVEN+"\n", SwyneCrawlerServerProtocol.run(command, server));
	}

	public void testRemoveFeed() throws Exception {
		String command = "add "+feedURL.toString();
		
		SwyneCrawlerServerProtocol.run(command, server);
		assertTrue(server.getCrawler().isTrackingFeed(feedURL));
		
		command = "remove "+feedURL.toString();
		assertEquals(SwyneCrawlerServerProtocol.REMOVE_SUCCESS_MESSAGE+" "+feedURL.toString()+"\n", SwyneCrawlerServerProtocol.run(command, server));
		assertFalse(server.getCrawler().isTrackingFeed(feedURL));
	}
}
