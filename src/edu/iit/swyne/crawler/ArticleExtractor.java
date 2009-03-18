package edu.iit.swyne.crawler;

import java.util.Date;

public interface ArticleExtractor {
	NewsDocument parseArticle(String link, String title, Date publishedDate, String collection);
}
