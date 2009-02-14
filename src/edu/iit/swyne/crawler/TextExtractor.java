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

public class TextExtractor {
	
	private Pattern garbageText = Pattern.compile("^\\s*$|^(&nbsp;)+$|^&raquo;&nbsp;Discuss Article$|^\\(\\d+ Comments\\)$");

	public NewsDocument parseArticle(String link, String title,
			Date publishedDate, String collection) {
		
		String article = "";
		
		Parser parser = new Parser();
		try {
			parser.setURL(link);
			NodeList nList = parser.extractAllNodesThatMatch(new StorybodyFilter());
			for (int i = 0; i < nList.size(); i++) {
				Node node = nList.elementAt(i);
				Node[] children = node.getChildren().toNodeArray();
				for (int j = 0; j < children.length; j++) {
					if(!(children[j] instanceof Div)) {
						String text = children[j].toPlainTextString().trim();
						Matcher garbageMatcher = garbageText.matcher(text);
						if(!garbageMatcher.find()) {
							text = text.replaceAll("&raquo;", "");
							text = text.replaceAll("&nbsp;", " ");
							article += text + "\n";
						}
					}
				}
			}
		}
		catch(ParserException pe) {
			System.err.println("ERROR: "+pe.getMessage());
			pe.printStackTrace();
		}
		
		NewsDocument result = new NewsDocument();
		result.setSource(link);
		result.setPublishedDate(publishedDate);
		result.setTitle(title.trim());
		result.setCollection(collection);		
		result.setArticle(article.trim());
		
		return result;
	}

	public class StorybodyFilter implements NodeFilter {

		private static final long serialVersionUID = 1L;

		public boolean accept(Node node) {
			if(node instanceof Div) {
				Div divNode = (Div)node;
				String c = divNode.getAttribute("class");
				if(c != null && c.equals("storybody"))
					return true;
			}
			return false;
		}

	}
}
