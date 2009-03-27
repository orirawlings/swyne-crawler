package edu.iit.swyne.crawler.extractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.Div;

import edu.iit.swyne.crawler.NewsDocument;

public class LATimesExtractor extends HardCodedExtractor {

	public LATimesExtractor(URL link, String title, Date date, String collection) {
		super(link, title, date, collection);
	}
	
	@Override
	protected Pattern getGarbagePattern() {
		return Pattern.compile("^\\s*$|^(&nbsp;)+$|^&raquo;&nbsp;Discuss Article$|^\\(\\d+ Comments\\)$|^(\\w|\\d)+(\\.(\\w|\\d)+)*@(\\w|\\d)+(\\.(\\w|\\d)+)+$");
	}

	@Override
	protected NodeFilter getFilterInstance() {
		return new LATimesFilter();
	}

	public static void main(String[] args) throws MalformedURLException {
		NewsDocument doc = (new LATimesExtractor(new URL("http://feeds.latimes.com/~r/latimes/news/local/~3/9K6rvpbuz7g/la-me-calstate27-2009mar27,0,143715.story"), "title", new Date(), "collection")).parseArticle();
		System.out.println(doc.getArticle());
	}

	public class LATimesFilter implements NodeFilter {

		private static final long serialVersionUID = 1L;

		public boolean accept(Node node) {
			if(node instanceof Div) {
				Pattern articleBody = Pattern.compile("article_body");
				Pattern storybody = Pattern.compile("storybody");
				
				Div divNode = (Div) node;
				
				String ids = divNode.getAttribute("id");
				ids = ids != null ? ids : "";
				String classes = divNode.getAttribute("class");
				classes = classes != null ? classes : "";

				return !articleBody.matcher(ids).find() && storybody.matcher(classes).find();
			}
			return false;
		}

	}
}
