package edu.iit.swyne.crawler.extractor;

import java.net.URL;
import java.util.Date;

import edu.iit.swyne.crawler.NewsDocument;

public abstract class ArticleExtractor {
	protected URL articleURL;
	protected String articleTitle, collection;
	protected Date articleDate;
	
	public ArticleExtractor(URL link, String title, Date date, String collection) {
		this.articleURL = link;
		this.articleTitle = title;
		this.articleDate = date;
		this.collection = collection;
	}
	
	public abstract NewsDocument parseArticle();
}
