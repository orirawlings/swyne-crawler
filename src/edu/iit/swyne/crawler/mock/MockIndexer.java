package edu.iit.swyne.crawler.mock;

import edu.iit.swyne.crawler.Indexer;
import edu.iit.swyne.crawler.NewsDocument;

public class MockIndexer implements Indexer {
	
	private NewsDocument[] docs;
	private int currentIndex;
	
	public MockIndexer() {
		docs = new NewsDocument[20];
		currentIndex = 0;
	}
	
	public NewsDocument getDocument(int index) throws ArrayIndexOutOfBoundsException{
		return docs[index];
	}

	public void sendDocument(NewsDocument doc) {
//System.err.println("Trying to submit: "+doc.getTitle());
		if(currentIndex < docs.length) {
//System.err.println("Submiting article: "+doc.getTitle());
			docs[currentIndex] = doc;
			currentIndex++;
		}
	}
	
	public int getNumArticles() {
		return currentIndex;
	}

}
