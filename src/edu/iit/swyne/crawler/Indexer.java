package edu.iit.swyne.crawler;

import java.util.Properties;

public abstract class Indexer {
	protected Properties props;
	
	public Indexer(Properties props) {
		this.props = props;
	}
	
	public Indexer() {}
	
	public abstract void sendDocument(NewsDocument doc);
}
