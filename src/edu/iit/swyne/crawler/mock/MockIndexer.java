package edu.iit.swyne.crawler.mock;

import java.util.ArrayList;
import java.util.Properties;

import edu.iit.swyne.crawler.Indexer;
import edu.iit.swyne.crawler.NewsDocument;

public class MockIndexer extends Indexer {
	
	private ArrayList<NewsDocument> docs;
	private int currentIndex = 0;
	
	public MockIndexer() {
		docs = new ArrayList<NewsDocument>();
	}
	
	public MockIndexer(Properties p) {
		super(p);
		docs = new ArrayList<NewsDocument>();
	}
	
	public synchronized NewsDocument getDocument(int index) throws ArrayIndexOutOfBoundsException {
		return docs.get(index);
	}

	public synchronized void sendDocument(NewsDocument doc) {
		docs.add(doc);
		currentIndex++;
		System.out.println(this.getClass().toString()+" indexed: "+doc.getTitle());
	}
	
	public synchronized int getNumArticles() {
		return currentIndex;
	}

}
