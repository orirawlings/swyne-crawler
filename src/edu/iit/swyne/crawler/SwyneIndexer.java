package edu.iit.swyne.crawler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class SwyneIndexer implements Indexer {

	private String host;
	private int port;

	public SwyneIndexer(String host, int port) {
		this.host = host;
		this.port = port;
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
			
			out.println(message);
			
			out.close();
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
