package edu.iit.swyne.crawler.experiment;

public class Metric {
	
	private String control, experimental;
	private int controlLength, experimentalLength;
	private Integer[][] memo;

	public Metric(String control, String experimental) {
		this.control = control;
		this.experimental = experimental;
		this.controlLength = control.length();
		this.experimentalLength = experimental.length();
		this.memo = new Integer[control.length()][experimental.length()];
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
		return longestCommonSubsequence(controlLength-1, experimentalLength-1);
	}

	private int longestCommonSubsequence(int i, int j) {
		if (i == -1 || j == -1)
			return 0;
		
		int result;
		if (memo[i][j] != null)
			return memo[i][j].intValue();
		else if (control.charAt(i) == experimental.charAt(j))
			result = longestCommonSubsequence(i-1, j-1) + 1;
		else result = Math.max(longestCommonSubsequence(i-1, j), longestCommonSubsequence(i, j-1));
		
		memo[i][j] = new Integer(result);
		return result;
	}
}
