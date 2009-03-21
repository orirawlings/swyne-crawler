package edu.iit.swyne.crawler.extractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import edu.iit.swyne.crawler.NewsDocument;

public class TribuneExtractor extends ArticleExtractor {
	
	public TribuneExtractor(URL link, String title, Date date, String collection) {
		super(link, title, date, collection);
	}

	private Pattern garbageText = Pattern.compile("^\\s*$|^(--)");

	public NewsDocument parseArticle() {
		String article = "";
		
		Parser parser = new Parser();
		try {
			parser.setURL(articleURL.toString());
			NodeList nList = parser.extractAllNodesThatMatch(new TribuneFilter());
			for (int i = 0; i < nList.size(); i++) {
				Node node = nList.elementAt(i);
				Node[] children = node.getChildren().toNodeArray();
				for (int j = 0; j < children.length; j++) {
					String text = children[j].toPlainTextString().trim();
					Matcher garbageMatcher = garbageText .matcher(text);
					if (!garbageMatcher.find()) {
						text = text.replaceAll("&nbsp;", " ");
						article += text;
					}
				}
			}
		}
		catch (ParserException e) {
			e.printStackTrace();
		}
		
		NewsDocument result = new NewsDocument();
		result.setSource(articleURL.toString());
		result.setPublishedDate(articleDate);
		result.setTitle(articleTitle.trim());
		result.setCollection(collection);		
		result.setArticle(article.trim());
		return result;
	}
	
	public static void main(String[] args) throws MalformedURLException {
		NewsDocument newsDocument = (new TribuneExtractor(new URL("http://feeds.chicagotribune.com/~r/chicagotribune/news/local/~3/Xf348TIcPd4/chi-riverside-fire,0,5042971.storylink"), "title", new Date(), "collection")).parseArticle();
		System.out.println(newsDocument);
	}

	public class TribuneFilter implements NodeFilter {
	
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
