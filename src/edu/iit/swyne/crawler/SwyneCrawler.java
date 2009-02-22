package edu.iit.swyne.crawler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SwyneCrawler implements Crawler {

	public static final String NAME = "Swyne Crawler Server";
	public static final String VERSION = "v0.1";
	
	private final static String DEFAULT_MAX_CRAWLING_THREADS = "5";
	private final static String DEFAULT_POLLING_INTERVAL_SECS = "3600";
	private final static String DEFAULT_INDEXER = "edu.iit.swyne.crawler.mock.MockIndexer";
	
	protected boolean initialized;
	protected boolean listening;
	protected Properties props;
	protected Indexer indexer;
	
	private Map<URL, ScheduledFuture<?>> feedTasks = Collections.synchronizedMap(new HashMap<URL, ScheduledFuture<?>>());
	private ScheduledExecutorService scheduler;

	public SwyneCrawler(Properties props) {
		Properties defaultProps = new Properties();
		defaultProps.setProperty("crawler.maxThreads", DEFAULT_MAX_CRAWLING_THREADS);
		defaultProps.setProperty("crawler.pollingFrequency", DEFAULT_POLLING_INTERVAL_SECS);
		defaultProps.setProperty("crawler.indexer", DEFAULT_INDEXER);
		
		this.props = new Properties(defaultProps);
		this.props.putAll(props);
	}
	
	
	public SwyneCrawler() {
		this(new Properties());
	}
	

	public synchronized void init() {
		if(!this.initialized) {
			try {
				this.indexer = (Indexer) Class.forName(props.getProperty("crawler.indexer")).newInstance();
			} catch (InstantiationException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				System.exit(1);
			} catch (IllegalAccessException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				System.exit(1);
			} catch (ClassNotFoundException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}

			this.scheduler = Executors.newScheduledThreadPool(Integer.parseInt(props.getProperty("crawler.maxThreads")));
			this.initialized = true;
		}
	}
	
	public synchronized Indexer getIndexer() {
		return indexer;
	}
	
	public synchronized boolean isRunning() {
		return listening;
	}

	public synchronized void shutdown() {
		if (this.listening) {
			this.listening = false;
			this.scheduler.shutdown();
			try {
				this.scheduler.awaitTermination(20, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				System.err.println("ERROR: "+e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public synchronized void start() {
		if (!this.listening) {
			if(!this.initialized) this.init();
			this.listening = true;
		}
	}

	public synchronized void addFeed(URL feedURL) throws FeedAlreadyTrackedException{
		if (!this.isRunning()) {
			this.start();
		}
		FeedListener listener = new FeedListener(feedURL);
		listener.setIndexer(indexer);
		if (!feedTasks.containsKey(feedURL)) {
			feedTasks.put(feedURL, scheduler.scheduleWithFixedDelay(listener, 0, Integer.parseInt(props.getProperty("crawler.pollingFrequency")), TimeUnit.SECONDS));
		}
		else throw new FeedAlreadyTrackedException("Feed " + feedURL.toString() + " has been previously added.");
	}


	public synchronized int numFeedsTracking() {
		return feedTasks.size();
	}

	
	public synchronized boolean isTrackingFeed(URL feedURL) {
		return feedTasks.containsKey(feedURL);
	}

	
	public synchronized void removeFeed(URL feedURL) {
		ScheduledFuture<?> feedJob = feedTasks.remove(feedURL);
		feedJob.cancel(false);
	}
	
	
	public static void main(String[] args) {
		Properties props = new Properties();
		
		// If a file path is specified on the command-line, load that file into properties
		if (args.length > 0)
			try {
				props.loadFromXML(new FileInputStream(args[0]));
			} catch (InvalidPropertiesFormatException e) {
				System.err.println("ERROR: "+e.getMessage());
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				System.err.println("ERROR: "+e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("ERROR: "+e.getMessage());
				e.printStackTrace();
			}
			
		SwyneCrawler crawler = new SwyneCrawler(props);
		crawler.init();
		crawler.start();
	}
}
