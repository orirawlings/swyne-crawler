package edu.iit.swyne.crawler.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

import edu.iit.swyne.crawler.NewsDocument;
import edu.iit.swyne.crawler.experiment.TextToTagRatioChart;
import edu.iit.swyne.crawler.extractor.Clusterer.ClusterAlgorithm;

public class TextToTagExtractor extends ArticleExtractor {

	public static final int DEFAULT_SMOOTHING_RADIUS = 2;
	public static final Clusterer DEFAULT_CLUSTERER = new Clusterer(ClusterAlgorithm.THRESHOLD);
	
	private Clusterer clusterer = DEFAULT_CLUSTERER;
	
	protected String html;

	public TextToTagExtractor(URL link, String title, Date date, String collection) {
		super(link, title, date, collection);
		
		try {
			// fetch html page
			html = fetchHTML(articleURL);
		} catch (IOException e) {
			System.err.println("Could not extract article text from: "+articleURL.toString());
			e.printStackTrace();
		}
		
		// clean the html and store in html field
		html = cleanHTML(html);
	}
	
	public TextToTagExtractor(URL link, String title, Date date, String collection, String htmlText) {
		super(link, title, date, collection);
		this.html = cleanHTML(htmlText);
	}

	public NewsDocument parseArticle() {
if(html.equals("")) System.err.println(articleURL);
		String htmlLines[], articleBody = null;
		double[] textToTagRatios;
		
		// Generate Text-To-Tag Ratio Array
		htmlLines = html.split("\\n|\\n\r\\r\n|\\r|\\u0085|\\u2028|\\u2029");
		textToTagRatios = generateTextToTag(htmlLines);
		
		// Cluster the lines of HTML
		boolean[] classifications = clusterer.cluster(textToTagRatios);
		
		// Strip plain text from lines classified as 'content'
		articleBody = "";
		for (int i = 0; i < classifications.length; i++)
			if (classifications[i]) articleBody += plainText(htmlLines[i]);

		NewsDocument doc = new NewsDocument();
		doc.setTitle(articleTitle);
		doc.setSource(articleURL.toString());
		doc.setPublishedDate(articleDate);
		doc.setCollection(collection);
		doc.setArticle(articleBody);
		
		return doc;
	}
	
	public void setClusteringAlgorithm(ClusterAlgorithm algo) {
		this.clusterer = new Clusterer(algo);
	}

	private static String plainText(String html) {
		return html.replaceAll("<.*?(\\n.*?)*?>", "");
	}

	public static String fetchHTML(URL url) throws IOException {
		String htmlText = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
		String line;
		while((line = in.readLine()) != null)
			htmlText += line+"\n";
		return htmlText;
	}

	public static String cleanHTML(String text) {
		String result = new String(text);
		result = result.replaceAll("<script .*?(\\n.*?)+?</script>", "\n");
		result = result.replaceAll("<script .*</script>", "");
		result = result.replaceAll("<!--.*?(\\n.*?)+?-->", "\n");
		result = result.replaceAll("<!--.*-->", "");
		
		return result;
	}

	public static double[] generateTextToTag(String[] htmlLines) {
		double[] result = new double[htmlLines.length];
		boolean inTag = false;
		
		for (int i = 0; i < htmlLines.length; i++) {
			String line = htmlLines[i];
			int nonTagCount = 0;
			int tagCount = 0;
			
			for (int j = 0; j < line.length(); j++) {
				if (!inTag) {
					if (line.charAt(j) == '<') {
						tagCount++;
						inTag = true;
					}
					else nonTagCount++;
				}
				else if (line.charAt(j) == '>')
					inTag = false;
			}
			
			if (tagCount == 0) tagCount = 1;
			result[i] = ((double) nonTagCount)/((double) tagCount);
		}
		return result;
	}

	public static double[] smooth(double[] textToTagRatios, int radius) {
		double[] result = new double[textToTagRatios.length];
		
		for (int i = 0; i < textToTagRatios.length; i++) {
			int sum = 0;
			for (int j = i - radius; j <= i + radius; j++) {
				if (j > 0 && j < textToTagRatios.length)
					sum += textToTagRatios[j];
			}
			result[i] = sum  / (2 * radius + 1);
		}
		
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		TextToTagRatioChart.main(args);
	}

	public static double standardDeviation(double[] data) {
		double avg = mean(data);
		double[] squares = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			squares[i] = Math.pow(data[i] - avg, 2);
		}
		return Math.sqrt(mean(squares));
	}
	
	public static double mean(double[] data) {
		return mean(data, 0, data.length);
	}

	public static double mean(double[] data, int firstIndex, int secondIndex) {
		double sum = 0;
		
		firstIndex = firstIndex >= 0 ? firstIndex : 0;
		secondIndex = secondIndex < data.length ? secondIndex : data.length-1;
		for (int i = firstIndex; i <= secondIndex; i++)
			sum += data[i];
		return sum / (1 + secondIndex - firstIndex);
	}
}
