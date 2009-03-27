package edu.iit.swyne.crawler.extractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.Div;

import edu.iit.swyne.crawler.NewsDocument;

public class ChicagoTribuneExtractor extends HardCodedExtractor {
	
	public ChicagoTribuneExtractor(URL link, String title, Date date, String collection) {
		super(link, title, date, collection);
	}
	
	@Override
	protected Pattern getGarbagePattern() {
		// TODO Auto-generated method stub
		return Pattern.compile("^\\s*$|^(--)");
	}

	@Override
	protected NodeFilter getFilterInstance() {
		return new ChicagoTribuneFilter();
	}

	public static void main(String[] args) throws MalformedURLException {
		NewsDocument newsDocument = (new ChicagoTribuneExtractor(new URL("http://feeds.chicagotribune.com/~r/chicagotribune/news/local/~3/Xf348TIcPd4/chi-riverside-fire,0,5042971.storylink"), "title", new Date(), "collection")).parseArticle();
		System.out.println(newsDocument.getArticle());
	}

	public class ChicagoTribuneFilter implements NodeFilter {
	
		private static final long serialVersionUID = 1L;
	
		public boolean accept(Node node) {
			if (node instanceof Div) {
				Div divNode = (Div)node;
				String c = divNode.getAttribute("class");
				if(c != null && (c.equals("asset-body") || c.equals("asset-more")))
					return true;
			}
			return false;
		}
	}
}
