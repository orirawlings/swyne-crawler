package edu.iit.swyne.crawler.experiment;

import edu.iit.swyne.crawler.extractor.Clusterer.ClusterAlgorithm;

public class Data {

	private ClusterAlgorithm algorithm;
	private String collection;
	private double precision, recall;

	public Data(ClusterAlgorithm algo, String collection, double precision, double recall) {
		this.algorithm = algo;
		this.collection = collection;
		this.precision = precision;
		this.recall = recall;
	}

	public ClusterAlgorithm getAlgorithm() {
		return algorithm;
	}

	public String getCollection() {
		return collection;
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}
}
