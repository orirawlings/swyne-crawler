/**
 * 
 */
package edu.iit.swyne.crawler.experiment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import edu.iit.swyne.crawler.extractor.TextToTagExtractor;

import processing.core.PApplet;

public class TextToTagRatioChart extends PApplet {
	
	private static final long serialVersionUID = -6028427891320535337L;
	private static URL u;
	private static double max, textToTagRatios[];
	
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
//		noStroke();
		stroke(0);
		fill(0);
		
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
			
			int bars = textToTagRatios.length;
			for (int i = 0; i < textToTagRatios.length; i++) {
				rect(i*((float) (width-20))/bars+10, (float) ((height-20) - ((float) (height-20)*textToTagRatios[i]/max)), ((float) (width-20))/bars, (float) ((float) (height-20)*textToTagRatios[i]/max));
			}
			saveFrame("text2tag.tiff");
			
			background(150);
			double[] smoothed = TextToTagExtractor.smooth(textToTagRatios, 3);
			bars = smoothed.length;
			for (int i = 0; i < smoothed.length; i++) {
				rect(i*((float) (width-20))/bars+10, (float) ((height-20) - ((float) (height-20)*smoothed[i]/max)), ((float) (width-20))/bars, (float) ((float) (height-20)*smoothed[i]/max));
			}
			saveFrame("text2tag_smoothed.tiff");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void draw() {
		background(100);
		
		double[] smoothed = TextToTagExtractor.smooth(textToTagRatios, 50*mouseX/width);
		
		int bars = smoothed.length;
		for (int i = 0; i < smoothed.length; i++) {
			rect(i*((float) (width-20))/bars+10, (float) ((height-20) - ((float) (height-20)*smoothed[i]/max)), ((float) (width-20))/bars, (float) ((float) (height-20)*smoothed[i]/max));
		}
	}
}