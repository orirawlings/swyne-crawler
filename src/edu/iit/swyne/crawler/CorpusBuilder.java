package edu.iit.swyne.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

public class CorpusBuilder extends XMLWriterIndexer {

	private static final String DEFAULT_CORPUS_DIR = "data/corpus/";
	private static final Integer DEFAULT_MAX_DOCS_PER_COLLECTION = Integer.valueOf(50);
	
	private HashMap<String, Integer> docCounts = new HashMap<String, Integer>();
	private Integer maxDocsPerCollection;

	public CorpusBuilder(Properties props) {
		super(props);
		String num = props.getProperty("crawler.indexer.corpus.maxDocsPerCollection");
		maxDocsPerCollection = num != null ? Integer.decode(num) : DEFAULT_MAX_DOCS_PER_COLLECTION;
	}

	public CorpusBuilder() {
		super();
	}
	
	@Override
	public void sendDocument(NewsDocument doc) {
		if (doc.getArticle() == null || doc.getArticle().equals(""))
			return;
		
		Integer collectionCount = docCounts.get(doc.getCollection());
		if (collectionCount != null && collectionCount.compareTo(maxDocsPerCollection) >= 0)
			return;
		
		super.sendDocument(doc);
		
		URL docURL;
		try {
			docURL = new URL(doc.getSource());
			
			File html = new File(new File(props.getProperty("crawler.indexer.corpus.dir", DEFAULT_CORPUS_DIR), doc.getCollection()),Integer.toBinaryString(docURL.hashCode())+".html");
			html.getParentFile().mkdirs();
			html.createNewFile();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(docURL.openStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(html)));
			
			String line;
			while ((line = in.readLine()) != null) {
				out.println(line);
			}
			
			in.close();
			out.close();
			
			int newCount = collectionCount != null ? collectionCount.intValue() + 1 : 1;
			docCounts.put(doc.getCollection(), Integer.valueOf(newCount));
			
		} catch (MalformedURLException e) {
			System.err.println("ERROR: Bad URL for news document "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("ERROR: Failed to open stream to news document "+e.getMessage());
			e.printStackTrace();
		}
	}
}
