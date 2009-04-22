package edu.iit.swyne.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;


public class SwyneIndexer extends Indexer {

	private String host;
	private int port;

	public SwyneIndexer(Properties props) {
		super(props);
		this.host = this.props.getProperty("crawler.indexer.host");
		this.port = Integer.parseInt(this.props.getProperty("crawler.indexer.port"));
	}

	public void sendDocument(NewsDocument doc) {
		String message = "SUBMIT\n";
		
		message += "title: "+doc.getTitle()+"\n";
		message += "published: "+doc.getPublishedDate().toString()+"\n";
		message += "source: "+doc.getSource()+"\n";
		message += "collection: "+doc.getCollection()+"\n";
		message += "article: \n"+doc.getArticle()+"\n";
		message += ":[END]:\n";
		
		try {
			Socket swyne = new Socket(this.host, this.port);
			PrintWriter out = new PrintWriter(swyne.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(swyne.getInputStream()));
			
			out.println(message);
			
			String line;
			while ((line = in.readLine()) != null)
				System.out.println("Response from Swyne Indexer: " + line);
			
			out.close();
			in.close();
			swyne.close();
		} catch (UnknownHostException e) {
			System.err.println("ERROR: "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("ERROR: "+e.getMessage());
			e.printStackTrace();
		}
	}

}
