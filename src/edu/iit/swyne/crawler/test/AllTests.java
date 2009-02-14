package edu.iit.swyne.crawler.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("All tests for edu.iit.swyne.crawler");

		suite.addTestSuite(TestTextExtractor.class);
		suite.addTestSuite(TestFeedListener.class);
		
		return suite;
	}
}
