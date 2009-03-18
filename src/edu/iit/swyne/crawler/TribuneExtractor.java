package edu.iit.swyne.crawler;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class TribuneExtractor implements ArticleExtractor {
	
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

	private Pattern garbageText = Pattern.compile("^\\s*$|^(--)");

	public NewsDocument parseArticle(String link, String title, Date publishedDate, String collection) {
		String article = "";
		
		Parser parser = new Parser();
		try {
			parser.setURL(link);
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
System.out.println(text);
					}
				}
			}
		}
		catch (ParserException e) {
			e.printStackTrace();
		}
		
		NewsDocument result = new NewsDocument();
		result.setSource(link);
		result.setPublishedDate(publishedDate);
		result.setTitle(title.trim());
		result.setCollection(collection);		
		result.setArticle(article.trim());
		return result;
	}
	
	public static void main(String[] args) {
		(new TribuneExtractor()).parseArticle("http://feeds.chicagotribune.com/~r/chicagotribune/news/local/~3/Xf348TIcPd4/chi-riverside-fire,0,5042971.storylink", "title", new Date(), "collection");
	}
}
