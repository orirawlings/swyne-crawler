package edu.iit.swyne.crawler.extractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ParagraphTag;

import edu.iit.swyne.crawler.NewsDocument;

public class NewYorkTimesExtractor extends HardCodedExtractor {

	public NewYorkTimesExtractor(URL link, String title, Date date, String collection) {
		super(link, title, date, collection);
	}

	@Override
	protected NodeFilter getFilterInstance() {
		return new NewYorkTimesFilter();
	}

	@Override
	protected Pattern getGarbagePattern() {
		return Pattern.compile("^\\s*$");
	}

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		NewsDocument doc = (new NewYorkTimesExtractor(new URL("http://www.nytimes.com/2009/03/27/nyregion/27bigcity.html?partner=rss&amp;emc=rss"), "title", new Date(), "collection")).parseArticle();
		System.out.println(doc.getArticle());
	}

	public class NewYorkTimesFilter implements NodeFilter {
	
		private static final long serialVersionUID = 1L;

		public boolean accept(Node node) {
			Pattern articleBody = Pattern.compile("articleBody");
			Node parent;
			
			if (node instanceof ParagraphTag && (parent = node.getParent()) instanceof Div) {
				String ids = ((TagNode) parent).getAttribute("id");
				return ids != null ? articleBody.matcher(ids).find() : false;
			}
			return false;
		}
	
	}

}
