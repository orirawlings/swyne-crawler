package edu.iit.swyne.crawler.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("All tests for edu.iit.swyne.crawler");

		suite.addTestSuite(TestLATimesExtractor.class);
		suite.addTestSuite(TestSwyneIndexer.class);
		suite.addTestSuite(TestXMLWriterIndexer.class);
		suite.addTestSuite(TestFeedListener.class);
		suite.addTestSuite(TestSwyneCrawler.class);
		suite.addTestSuite(TestSwyneCrawlerServerProtocol.class);
		suite.addTestSuite(TestSwyneCrawlerServerSocketInterface.class);
		
		return suite;
	}
}
