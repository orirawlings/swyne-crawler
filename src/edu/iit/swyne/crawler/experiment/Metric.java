package edu.iit.swyne.crawler.experiment;

public class Metric {
	
	private String control, experimental;
	private int controlLength, experimentalLength, longestCommonSubsequence;
	private boolean solved = false;

	public Metric(String control, String experimental) {
		this.control = control;
		this.experimental = experimental;
		this.controlLength = control.length();
		this.experimentalLength = experimental.length();
	}
	
	public int getControlLength() {
		return controlLength;
	}

	public int getExperimentalLength() {
		return experimentalLength;
	}

	public double getRecall() {
		double lcs = longestCommonSubsequence();
		double c = controlLength;
		return lcs/c;
	}
	
	public double getPrecision() {
		double lcs = longestCommonSubsequence();
		double e = experimentalLength;
		return lcs/e;
	}

	public int longestCommonSubsequence() {
		if (!solved) {
			System.gc();
			longestCommonSubsequence = longestCommonSubsequence(controlLength-1, experimentalLength-1, new Integer[controlLength][experimentalLength]);
			solved = true;
		}
		return longestCommonSubsequence;
	}

	private int longestCommonSubsequence(int i, int j, Integer[][] memo) {
		if (i == -1 || j == -1)
			return 0;
		
		int result;
		if (memo[i][j] != null)
			return memo[i][j].intValue();
		else if (control.charAt(i) == experimental.charAt(j))
			result = longestCommonSubsequence(i-1, j-1, memo) + 1;
		else result = Math.max(longestCommonSubsequence(i-1, j, memo), longestCommonSubsequence(i, j-1, memo));
		
		memo[i][j] = new Integer(result);
		return result;
	}
}
