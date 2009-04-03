package edu.iit.swyne.crawler.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import edu.iit.swyne.crawler.CorpusBuilder;
import edu.iit.swyne.crawler.NewsDocument;
import junit.framework.TestCase;

public class TestCorpusBuilder extends TestCase {
	
	private static final String TEST_CORPUS_DIR = "tmp/testData/corpusTest";
	private static final String TEST_XML_OUTPUTFILE = TEST_CORPUS_DIR+"/test.xml";
	private static final String TEST_MAX_DOCS = "3";
	
	private static final String COLLECTION = "IITTech";
	private static final String TITLE = "An Awesome new RSS Crawler Developed at IIT";
	private static final String ARTICLE = "Today at IIT, a undergraduate computer science major participating in IPRO327 developed a web feed crawler. Ori Rawlings implemented the web feed listener to gather news articles for the teams 'Swyne' system. Swyne, being a semantically aware news search engine. After failures to implement a proper article gathering system in previous semester of the project, Rawlings stepped up to see the task accomplished. Originally planned to be first deployed on February 18th, 2009, some minor hang-ups accumulated during development pushing the inital deployment to February 23rd, 2009.";
	private static final Date DATE = new Date();
	private static final String SOURCE = "http://omega.cs.iit.edu/~orawling/iproTesting/article.html";
	
	private Properties props;
	private CorpusBuilder bob;
	
	private NewsDocument doc;

	private String html = "";
	
	private URL docURL;
	private File corpusDoc;
	
	public TestCorpusBuilder(String name) {
		super(name);
		
		props = new Properties();
		props.setProperty("crawler.indexer.xml.outputFile", TEST_XML_OUTPUTFILE);
		props.setProperty("crawler.indexer.corpus.dir", TEST_CORPUS_DIR);
		props.setProperty("crawler.indexer.corpus.maxDocsPerCollection", TEST_MAX_DOCS);

		doc = new NewsDocument();
		doc.setTitle(TITLE);
		doc.setPublishedDate(DATE);
		doc.setCollection(COLLECTION);
		doc.setSource(SOURCE);
		doc.setArticle(ARTICLE);
		
		try {
			docURL = new URL(doc.getSource());
			corpusDoc = new File(new File(TEST_CORPUS_DIR, doc.getCollection()), Integer.toBinaryString(docURL.hashCode())+".html");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(docURL.openStream()));
			
			String line;
			while ((line = in.readLine()) != null)
				html += line + "\n";
			
			tearDown();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setUp() throws Exception {
		bob = new CorpusBuilder(props);		
	}

	protected void tearDown() throws Exception {
		File corpus = new File(TEST_CORPUS_DIR);
		destroy(corpus);
	}

	private void destroy(File file) {
		if (file.isFile())
			file.delete();
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (int i = 0; i < children.length; i++)
				destroy(children[i]);
			file.delete();
		}
	}
	
	public void testSendDocument() throws Exception {
		bob.sendDocument(doc);
		
		BufferedReader in = new BufferedReader(new FileReader(corpusDoc));
		
		String fileContents = "", line;
		while ((line = in.readLine()) != null)
			fileContents += line + "\n";
		
		assertEquals(html, fileContents);
	}
	
	public void testMaxDocumentsPerCollection() throws Exception {
		String collection1 = "NewsPaperA";
		String collection2 = "NewsPaperB";
		String collection3 = "NewsPaperC";
		
		for (int i = 0; i < Integer.parseInt(TEST_MAX_DOCS) + 1; i++) {
			NewsDocument news = new NewsDocument();
			news.setSource("http://omega.cs.iit.edu/~orawling/iproTesting/article"+i+".html");
			news.setTitle(TITLE);
			news.setArticle(ARTICLE);
			news.setPublishedDate(DATE);
			news.setCollection(collection1);
			bob.sendDocument(news);
			
			NewsDocument news2 = new NewsDocument();
			news2.setSource("http://omega.cs.iit.edu/~orawling/iproTesting/article"+i+".html");
			news2.setTitle(TITLE);
			news2.setArticle(ARTICLE);
			news2.setPublishedDate(DATE);
			news2.setCollection(collection2);
			bob.sendDocument(news2);
		}
		
		for (int i = 0; i < Integer.parseInt(TEST_MAX_DOCS) - 1; i++) {
			NewsDocument news = new NewsDocument();
			news.setSource("http://omega.cs.iit.edu/~orawling/iproTesting/article"+i+".html");
			news.setTitle(TITLE);
			news.setArticle(ARTICLE);
			news.setPublishedDate(DATE);
			news.setCollection(collection3);
			bob.sendDocument(news);
		}
		
		File collectionA = new File(TEST_CORPUS_DIR, collection1);
		assertTrue(collectionA.isDirectory());
		assertEquals(Integer.parseInt(TEST_MAX_DOCS), collectionA.list().length);
		
		File collectionB = new File(TEST_CORPUS_DIR, collection2);
		assertTrue(collectionB.isDirectory());
		assertEquals(Integer.parseInt(TEST_MAX_DOCS), collectionB.list().length);
		
		File collectionC = new File(TEST_CORPUS_DIR, collection3);
		assertTrue(collectionC.isDirectory());
		assertEquals(2, collectionC.list().length);
	}
	
	public void testRepeatDocument() throws Exception {
		
	}
}
