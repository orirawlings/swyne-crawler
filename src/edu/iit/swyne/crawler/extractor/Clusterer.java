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
		
		boolean inContent = false;
		boolean[] result = new boolean[textToTagRatios.length];
		
		for (int i = 0; i < textToTagRatios.length; i++) {
			if (inContent) {
				inContent = textToTagRatios[i-1] - TextToTagExtractor.mean(textToTagRatios, i, i+2) < stdDev;
//				System.out.println((inContent ? "in " : "out ") + textToTagRatios[i] + " " + TextToTagExtractor.mean(textToTagRatios, i+1, i+3) + " " + stdDev);
			}
			else {
				inContent = textToTagRatios[i] - TextToTagExtractor.mean(textToTagRatios, i-3, i-1) >= stdDev;
//				System.out.println((inContent ? "in " : "out ") + textToTagRatios[i] + " " + TextToTagExtractor.mean(textToTagRatios, i-3, i-1) + " " + stdDev);
			}
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

	public boolean[] kMeansCluster(double[] textToTagRatios) {
		boolean[] result = new boolean[textToTagRatios.length];
		double[] data = TextToTagExtractor.smooth(textToTagRatios, TextToTagExtractor.DEFAULT_SMOOTHING_RADIUS);
		
		for (int i = 0; i < data.length; i++) {
			result[i] = i <= data.length/2;
		}
		
		boolean clustered = false;
		while (!clustered) {
			clustered = true;
			
			double sum1 = 0, sum2 = 0;
			int size1 = 0, size2 = 0;
			
			for (int i = 0; i < result.length; i++) {
				if (result[i]) {
					sum1 += data[i];
					size1++;
				}
				else {
					sum2 += data[i];
					size2++;
				}
			}
			double mean1 = sum1/size1;
			double mean2 = sum2/size2;
			
			for (int i = 0; i < result.length; i++) {
				double distance1 = Math.abs(data[i] - mean1);
				double distance2 = Math.abs(data[i] - mean2);
				
				if ((result[i] && distance1 > distance2) || (!result[i] && distance2 > distance1)) {
					result[i] = !result[i];
					clustered = false;
				}
			}
		}
		
		return result;
	}

	public boolean[] expectationMaximizationCluster(double[] textToTagRatios) {
		// TODO Auto-generated method stub
		return null;
	}
}
