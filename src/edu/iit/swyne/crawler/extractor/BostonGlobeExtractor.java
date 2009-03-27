package edu.iit.swyne.crawler.extractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.Div;

import edu.iit.swyne.crawler.NewsDocument;

public class BostonGlobeExtractor extends HardCodedExtractor {

	public BostonGlobeExtractor(URL link, String title, Date date,
			String collection) {
		super(link, title, date, collection);
	}

	@Override
	protected Pattern getGarbagePattern() {
		return Pattern.compile("^\\s*$|^&copy;");
	}

	@Override
	protected NodeFilter getFilterInstance() {
		return new BostonGlobeFilter();
	}

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		NewsDocument newsDocument = (new BostonGlobeExtractor(new URL("http://www.boston.com/news/local/massachusetts/articles/2009/03/27/ts_bus_trainees_can_now_fake_the_wheel?rss_id=Boston.com+--+Local+news"), "title", new Date(), "collection")).parseArticle();
		System.out.println(newsDocument.getArticle());
	}

	public class BostonGlobeFilter implements NodeFilter {
	
		private static final long serialVersionUID = 1L;

		public boolean accept(Node node) {
			Pattern pageId = Pattern.compile("page\\d");
			
			if (node instanceof Div) {
				String ids = ((Div) node).getAttribute("id");
				if (ids != null && pageId.matcher(ids).find()) return true;
			}
			return false;
		}
	
	}

}
