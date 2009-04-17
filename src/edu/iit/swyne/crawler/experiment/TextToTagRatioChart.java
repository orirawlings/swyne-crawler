/**
 * 
 */
package edu.iit.swyne.crawler.experiment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import edu.iit.swyne.crawler.extractor.Clusterer;
import edu.iit.swyne.crawler.extractor.TextToTagExtractor;
import edu.iit.swyne.crawler.extractor.Clusterer.ClusterAlgorithm;

import processing.core.PApplet;

public class TextToTagRatioChart extends PApplet {
	
	private static final long serialVersionUID = -6028427891320535337L;
	private static URL u;
	private static double max, textToTagRatios[];
	private Clusterer clusterer;
	private int xOffset, yOffset,chartWidth, chartHeight;
	private float timer;
	
	public static void main(String[] args) {
		try {
			u = new URL(args[0]);
			System.out.println(u.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		PApplet.main(new String[] { "--bgcolor=#FFFFFF", "edu.iit.swyne.crawler.experiment.TextToTagRatioChart" });
	}
	
	@Override
	public void setup() {
		size(800, 450);
		background(150);
		
		clusterer = new Clusterer(ClusterAlgorithm.THRESHOLD);
		
		String htmlText;
		try {
			htmlText = TextToTagExtractor.fetchHTML(u);
			htmlText = TextToTagExtractor.cleanHTML(htmlText);
			System.out.println(htmlText);
			String[] htmlLines = htmlText.split("\\n|\\n\r\\r\n|\\r|\\u0085|\\u2028|\\u2029");
			textToTagRatios = TextToTagExtractor.generateTextToTag(htmlLines);

			max = 0;
			for (int i = 0; i < textToTagRatios.length; i++)
				if (textToTagRatios[i] > max) max = textToTagRatios[i];
			
			xOffset = 10;
			yOffset = 10;
			chartWidth = width - 2 * xOffset;
			chartHeight = height - 2 * yOffset;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void draw() {
		background(100);
		rectMode(CORNER);
		
		boolean[] colored = clusterer.cluster(textToTagRatios);
		double[] smoothed = TextToTagExtractor.smooth(textToTagRatios, TextToTagExtractor.DEFAULT_SMOOTHING_RADIUS);
		
//		float angle = PI*timer/180;
		float angle = (PI*mouseX)/(180*width);
		float originalAlpha = 255*(sq(sin(angle/2)));
		float smoothedAlpha = 255*(sq(cos(angle/2)));
		
		
		
		int bars = smoothed.length;
		noStroke();
		for (int i = 0; i < smoothed.length; i++) {
			fill(colored[i] ? color(0, 255, 0, originalAlpha) : color(255, 255, 255, originalAlpha));
			rect(xOffset + ((float) i*chartWidth)/bars, height-yOffset - (float) (chartHeight*textToTagRatios[i]/max), ((float) chartWidth)/bars, (float) (chartHeight*textToTagRatios[i]/max));
			
			fill(colored[i] ? color(0, 255, 0, smoothedAlpha) : color(255, 255, 255, smoothedAlpha));
			rect(xOffset + ((float) i*chartWidth)/bars, height-yOffset - (float) (chartHeight*smoothed[i]/max), ((float) chartWidth)/bars, (float) (chartHeight*smoothed[i]/max));
		}
		stroke(255);
		double stdDev = TextToTagExtractor.standardDeviation(smoothed);
		line(xOffset, height-yOffset - (float) (chartHeight*stdDev/max), width - xOffset, height-yOffset - (float) (chartHeight*stdDev/max));
		
		rectMode(CENTER);
		noFill();
		ellipse(xOffset*2, yOffset*2, xOffset*2, yOffset*2);
		ellipse(xOffset*2 + xOffset*cos(angle), yOffset*2 + yOffset*sin(angle), ((float) xOffset)/2, ((float) yOffset)/2);
		timer++;
	}
}