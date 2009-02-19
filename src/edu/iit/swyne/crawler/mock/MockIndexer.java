package edu.iit.swyne.crawler.mock;

import java.util.ArrayList;

import edu.iit.swyne.crawler.Indexer;
import edu.iit.swyne.crawler.NewsDocument;

public class MockIndexer implements Indexer {
	
	private ArrayList<NewsDocument> docs;
	private int currentIndex = 0;
	
	public MockIndexer() {
		docs = new ArrayList<NewsDocument>();
	}
	
	public synchronized NewsDocument getDocument(int index) throws ArrayIndexOutOfBoundsException {
		return docs.get(index);
	}

	public synchronized void sendDocument(NewsDocument doc) {
		docs.add(doc);
		currentIndex++;
	}
	
	public synchronized int getNumArticles() {
		return currentIndex;
	}

}
