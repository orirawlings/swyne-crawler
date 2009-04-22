package edu.iit.swyne.crawler.test;

import edu.iit.swyne.crawler.experiment.Data;
import edu.iit.swyne.crawler.experiment.DataSet;
import edu.iit.swyne.crawler.experiment.DataSet.DataNotFoundException;
import edu.iit.swyne.crawler.extractor.Clusterer.ClusterAlgorithm;
import junit.framework.TestCase;

public class TestDataSet extends TestCase {
	
	private DataSet d;
	private String c = "ChicagoTribune", n = "NYTimes", l = "LATimes";

	protected void setUp() throws Exception {
		d = new DataSet();
		d.addData(new Data(ClusterAlgorithm.THRESHOLD, c, 1.0, 0));
		d.addData(new Data(ClusterAlgorithm.THRESHOLD, c, 0, 0.5));
		d.addData(new Data(ClusterAlgorithm.THRESHOLD, c, 0.5, 1.0));
		d.addData(new Data(ClusterAlgorithm.THRESHOLD, n, 0.25, 0.75));
		d.addData(new Data(ClusterAlgorithm.THRESHOLD, l, 0.75, 0.25));
		
		d.addData(new Data(ClusterAlgorithm.PREDICTION, c, 0.25, 1.0));
		d.addData(new Data(ClusterAlgorithm.PREDICTION, n, 0.75, 1.0));
		d.addData(new Data(ClusterAlgorithm.PREDICTION, n, 0, 1.0));
		d.addData(new Data(ClusterAlgorithm.PREDICTION, l, 0.25, 1.0));
		d.addData(new Data(ClusterAlgorithm.PREDICTION, l, 0, 1.0));
		
		d.addData(new Data(ClusterAlgorithm.K_MEANS, l, 1.0, 0.7));
	}
	
	public void testGetPrecisionFromAlgo() throws Exception {
		assertEquals(0.5, d.getAveragePrecision(ClusterAlgorithm.THRESHOLD));
		assertEquals(0.25, d.getAveragePrecision(ClusterAlgorithm.PREDICTION));
		assertEquals(1.0, d.getAveragePrecision(ClusterAlgorithm.K_MEANS));
		
		try {
			d.getAveragePrecision(ClusterAlgorithm.EM);
			fail();
		}
		catch (DataNotFoundException e) {
			// good, we made it
		}
	}
	
	public void testGetRecallFromAlgo() throws Exception {
		assertEquals(0.5, d.getAverageRecall(ClusterAlgorithm.THRESHOLD));
		assertEquals(1.0, d.getAverageRecall(ClusterAlgorithm.PREDICTION));
		assertEquals(0.7, d.getAverageRecall(ClusterAlgorithm.K_MEANS));
		
		try {
			d.getAverageRecall(ClusterAlgorithm.EM);
			fail();
		}
		catch (DataNotFoundException e) {
			// good, we made it
		}
	}
	
	public void testGetPrecisionFromCollection() throws Exception {
		assertEquals(1.75/4, d.getAveragePrecision(c));
		assertEquals(1.0/3, d.getAveragePrecision(n));
		assertEquals(2.0/4, d.getAveragePrecision(l));
		
		try {
			d.getAveragePrecision("BostonGlobe");
			fail();
		}
		catch (DataNotFoundException e) {
			// good, we made it
		}
	}
	
	public void testGetRecallFromCollection() throws Exception {
		assertEquals(2.5/4, d.getAverageRecall(c));
		assertEquals(2.75/3, d.getAverageRecall(n));
		assertEquals(2.95/4, d.getAverageRecall(l));
		
		try {
			d.getAverageRecall("BostonGlobe");
			fail();
		}
		catch (DataNotFoundException e) {
			// good, we made it
		}
	}
	
	public void testGetPrecisionFromAlgoAndCollection() throws Exception {
		assertEquals(0.5, d.getAveragePrecision(ClusterAlgorithm.THRESHOLD, c));
		assertEquals(0.25, d.getAveragePrecision(ClusterAlgorithm.THRESHOLD, n));
		assertEquals(0.75, d.getAveragePrecision(ClusterAlgorithm.THRESHOLD, l));
		
		assertEquals(0.25, d.getAveragePrecision(ClusterAlgorithm.PREDICTION, c));
		assertEquals(0.375, d.getAveragePrecision(ClusterAlgorithm.PREDICTION, n));
		assertEquals(0.125, d.getAveragePrecision(ClusterAlgorithm.PREDICTION, l));
		
		try {
			d.getAveragePrecision(ClusterAlgorithm.K_MEANS, c);
			d.getAveragePrecision(ClusterAlgorithm.K_MEANS, n);
			fail();
		}
		catch(DataNotFoundException e) {
			// good, we made it
		}
		assertEquals(1.0, d.getAveragePrecision(ClusterAlgorithm.K_MEANS, l));
	}
}
