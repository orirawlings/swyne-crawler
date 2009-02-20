package edu.iit.swyne.crawler;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.fetcher.FetcherEvent;
import com.sun.syndication.fetcher.FetcherListener;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;

/**
 * @author Ori Rawlings
 * 
 * A thread-safe object, which polls a web feed and indexes it if there is new content
 *
 */
public class FeedListener implements Runnable{

	private URL url;
	private FeedFetcherCache cache;
	private HttpURLFeedFetcher fetcher;
	
	private Indexer indexer;

	public FeedListener(URL url) {
		this.url = url;
		cache = HashMapFeedInfoCache.getInstance();
		fetcher = new HttpURLFeedFetcher(cache);
		fetcher.addFetcherEventListener(new FetchListenImpl());
	}
	
	public void run() {
		try {
			runMethod();
		}
		catch (IndexerNotDefinedException inde) {
			System.err.println("ERROR: "+inde.getMessage());
			inde.printStackTrace();
		}
	}

	public synchronized void runMethod() throws IndexerNotDefinedException {
		if (indexer == null) throw new IndexerNotDefinedException("No indexer has been set for this feed listener.");
		
		try {
			fetcher.retrieveFeed(url);
		} catch (Exception e) {
			System.err.println("ERROR: "+e.getMessage());
			e.printStackTrace();
		}
	}

	public synchronized URL getUrl() {
		return url;
	}

	public synchronized void setUrl(URL url) {
		this.url = url;
	}

	public synchronized Indexer getIndexer() {
		return indexer;
	}

	public synchronized void setIndexer(Indexer indexer) {
		this.indexer = indexer;
	}
	
	public synchronized void destroyCache() {
		fetcher.getFeedInfoCache().clear();
	}

	public class IndexerNotDefinedException extends Exception {
		public IndexerNotDefinedException(String string) {
			super(string);
		}
		private static final long serialVersionUID = 1L;
	}
	
	private class FetchListenImpl implements FetcherListener {
		private HashMap<SyndEntry, Boolean> entryHash;
		
		public FetchListenImpl() {
			entryHash = new HashMap<SyndEntry, Boolean>();
		}
		
		@SuppressWarnings("unchecked")
		public void fetcherEvent(FetcherEvent event) {
			String eventType = event.getEventType();
			
			if(FetcherEvent.EVENT_TYPE_FEED_RETRIEVED.equals(eventType)) {
				List<SyndEntry> l = event.getFeed().getEntries();
				
				for (int i = l.size()-1; i >=0 ; i--) {
					SyndEntry e = l.get(i);
					if(entryHash.get(e) == null) {
						TextExtractor t = new TextExtractor();
						NewsDocument doc = t.parseArticle(e.getLink(), e.getTitle(), e.getPublishedDate(), "LA Times");
						indexer.sendDocument(doc);
					}
				}
			}
		}
	}

}
