package edu.iit.swyne.crawler.server;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import edu.iit.swyne.crawler.FeedListener;
import edu.iit.swyne.crawler.Indexer;

public class SwyneCrawlerServer {

	@SuppressWarnings("serial")
	public class FeedAlreadyTrackedException extends Exception {
		public FeedAlreadyTrackedException(String string) {
			super(string);
		}

	}

	private boolean initialized;
	private boolean listening;
	private Properties props;
	private ScheduledExecutorService scheduler;
	private Indexer indexer;
	@SuppressWarnings("unchecked")
	private Map<URL, ScheduledFuture> feedTasks = Collections.synchronizedMap(new HashMap<URL, ScheduledFuture>());

	public SwyneCrawlerServer(Properties props) {
		this.props = props;
	}

	public void init() {
		this.initialized = true;
		this.scheduler = Executors.newScheduledThreadPool(Integer.parseInt(props.getProperty("server.maxThreads")));
		try {
			this.indexer = (Indexer) Class.forName(props.getProperty("indexer.class", "edu.iit.swyne.crawler.mock.MockIndexer")).newInstance();
		} catch (InstantiationException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public Indexer getIndexer() {
		return indexer;
	}

	public boolean isRunning() {
		return listening;
	}

	public void shutdown() {
		this.listening = false;
		scheduler.shutdown();
		try {
			scheduler.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("ERROR: "+e.getMessage());
			e.printStackTrace();
		}
	}

	public void start() {
		if(!this.isInitialized()) {
			this.init();
		}
		this.listening = true;
	}

	private boolean isInitialized() {
		return initialized;
	}

	public void addFeed(URL feedURL) throws FeedAlreadyTrackedException{
		if (!this.isRunning()) {
			this.start();
		}
		FeedListener listener = new FeedListener(feedURL);
		listener.setIndexer(indexer);
		if (!feedTasks.containsKey(feedURL)) {
			feedTasks.put(feedURL, scheduler.scheduleWithFixedDelay(listener, 0, Integer.parseInt(props.getProperty("feeds.pollingInterval")), TimeUnit.SECONDS));
		}
		else throw new FeedAlreadyTrackedException("This feed has been previously added");
	}

	public int numFeedsTracking() {
		return feedTasks.size();
	}

	public boolean isTrackingFeed(URL feedURL) {
		return feedTasks.containsKey(feedURL);
	}

	@SuppressWarnings("unchecked")
	public void removeFeed(URL feedURL) {
		ScheduledFuture feedJob = feedTasks.remove(feedURL);
		feedJob.cancel(false);
	}

}
