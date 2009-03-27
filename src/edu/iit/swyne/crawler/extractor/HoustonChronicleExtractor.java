package edu.iit.swyne.crawler.extractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.ParagraphTag;

import edu.iit.swyne.crawler.NewsDocument;

public class HoustonChronicleExtractor extends HardCodedExtractor {

	public HoustonChronicleExtractor(URL link, String title, Date date, String collection) {
		super(link, title, date, collection);
	}

	@Override
	protected NodeFilter getFilterInstance() {
		return new HoustonChronicleFilter();
	}

	@Override
	protected Pattern getGarbagePattern() {
		return Pattern.compile("^\\s*$|^(\\w|\\d)+(\\.(\\w|\\d)+)*@(\\w|\\d)+(\\.(\\w|\\d)+)+$");
	}

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		NewsDocument doc = (new HoustonChronicleExtractor(new URL("http://feeds.chron.com/~r/houstonchronicle/metro/~3/csU_qdsmNpQ/6325753.html"), "title", new Date(), "collection")).parseArticle();
		System.out.println(doc.getArticle());
	}

	public class HoustonChronicleFilter implements NodeFilter {
	
		private static final long serialVersionUID = 1L;

		public boolean accept(Node node) {
			Pattern houstonText = Pattern.compile("HoustonText");
			
			if (node instanceof ParagraphTag) {
				String classes = ((TagNode) node).getAttribute("class");
				classes = classes != null ? classes : "";
				
				return houstonText.matcher(classes).find();
			}
			return false;
		}
	
	}

}
