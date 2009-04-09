package edu.iit.swyne.crawler.test;

import edu.iit.swyne.crawler.experiment.Metric;
import junit.framework.TestCase;

public class TestMetric extends TestCase {
	private Metric m;
	private String control = "abcdef", experimental = "a0b1c2d3e4f56789";
	
	@Override
	protected void setUp() throws Exception {
		m = new Metric(control, experimental);
	}

	public void testGetRecall() {
		double one = 1;
		assertEquals(one, m.getRecall());
	}

	public void testGetPrecision() {
		double p = ((double) 6)/16;
		assertEquals(p, m.getPrecision());
	}

	public void testLongestCommonSubsequence() {
		assertEquals(6, m.longestCommonSubsequence());
		
		m = new Metric("ori rawlings", "Ori Rawlings");
		assertEquals(10, m.longestCommonSubsequence());
		
		m = new Metric("swyne", "pig");
		assertEquals(0, m.longestCommonSubsequence());
	}
}
