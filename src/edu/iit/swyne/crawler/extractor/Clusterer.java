package edu.iit.swyne.crawler.extractor;


public class Clusterer {
	
	public enum ClusterAlgorithm {
		K_MEANS,
		EM,
		FARTHEST_FIRST,
		THRESHOLD,
		PREDICTION;
	}
	
	private ClusterAlgorithm algo = ClusterAlgorithm.THRESHOLD;
	
	public Clusterer(ClusterAlgorithm algo) {
		this.algo = algo;
	}
	
	public Clusterer() {}
	
	public boolean[] cluster(double[] textToTagRatios) {
		switch (algo) {
		case K_MEANS:
			return kMeansCluster(textToTagRatios);
		case EM:
			return expectationMaximizationCluster(textToTagRatios);
		case FARTHEST_FIRST:
			return farthestFirstCluster(textToTagRatios);
		case THRESHOLD:
			return thresholdCluster(textToTagRatios);
		case PREDICTION:
			return predictionCluster(textToTagRatios);
		}
		
		// dumb, temporary clusterer
		boolean[] result = new boolean[textToTagRatios.length];
		for (int i = 0; i < textToTagRatios.length; i++) {
			result[i] = textToTagRatios[i] > 100;
		}
		return result;
	}

	public boolean[] predictionCluster(double[] textToTagRatios) {
		double stdDev = TextToTagExtractor.standardDeviation(textToTagRatios);
		
		boolean inContent = false, result[] = new boolean[textToTagRatios.length];
		
		for (int i = 0; i < textToTagRatios.length; i++) {
			if (inContent)
				inContent = textToTagRatios[i] - TextToTagExtractor.mean(textToTagRatios, i+1, i+4) < stdDev;
			else
				inContent = TextToTagExtractor.mean(textToTagRatios, i+1, i+4) - textToTagRatios[i] >= stdDev;
			result[i] = inContent;
		}
		
		return result;
	}

	public boolean[] thresholdCluster(double[] textToTagRatios) {
		boolean[] result = new boolean[textToTagRatios.length];
		double[] data = TextToTagExtractor.smooth(textToTagRatios, TextToTagExtractor.DEFAULT_SMOOTHING_RADIUS);
		
		double stdDev = TextToTagExtractor.standardDeviation(data);
		
		for (int i = 0; i < data.length; i++)
			result[i] = data[i] >= stdDev;
		
		return result;
	}

	public boolean[] farthestFirstCluster(double[] textToTagRatios) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean[] expectationMaximizationCluster(double[] textToTagRatios) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean[] kMeansCluster(double[] textToTagRatios) {
		// TODO Auto-generated method stub
		return null;
	}
}
