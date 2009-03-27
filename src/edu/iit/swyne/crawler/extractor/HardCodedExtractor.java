package edu.iit.swyne.crawler.extractor;

import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import edu.iit.swyne.crawler.NewsDocument;

public abstract class HardCodedExtractor extends ArticleExtractor {

	public HardCodedExtractor(URL link, String title, Date date, String collection) {
		super(link, title, date, collection);
	}
	
	public NewsDocument parseArticle() {
		String article = "";
		
		Parser parser = new Parser();
		try {
			parser.setURL(articleURL.toString());
			NodeList nList = parser.extractAllNodesThatMatch(getFilterInstance());
			for (int i = 0; i < nList.size(); i++) {
				Node node = nList.elementAt(i);
				NodeList childList = node.getChildren();
				if (childList == null) continue;
				Node[] children = node.getChildren().toNodeArray();
				for (int j = 0; j < children.length; j++) {
					String text = children[j].toPlainTextString().trim();
					Matcher garbageMatcher = getGarbagePattern().matcher(text);
					if (!garbageMatcher.find()) {
						text = text.replaceAll("&nbsp;", " ");
						text = text.replaceAll("&#8220;", "\"");
						text = text.replaceAll("&#8221;", "\"");
						text = text.replaceAll("&#8217;", "'");
						text = text.replaceAll("&#8212;", "--");
						text = text.replaceAll("&#8226;", "");
						article += text+"\n";
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

	protected abstract Pattern getGarbagePattern();

	protected abstract NodeFilter getFilterInstance();
}
