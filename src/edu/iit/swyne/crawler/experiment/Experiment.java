package edu.iit.swyne.crawler.experiment;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import edu.iit.swyne.crawler.extractor.Clusterer.ClusterAlgorithm;

import processing.core.PApplet;

public class Experiment extends PApplet {

	private static final long serialVersionUID = -1441473921424474434L;


	private static Document corpusXML;
	private static File corpusDir;
	
	private DataSet data = new DataSet();
	private ExperimentTrial[] trials;

	@Override
	public void setup() {
		size(400, 400);
		background(100);
		
		trials = new ExperimentTrial[] {
				new ExperimentTrial(corpusXML, corpusDir, ClusterAlgorithm.THRESHOLD, data), 
				new ExperimentTrial(corpusXML, corpusDir, ClusterAlgorithm.PREDICTION, data), 
				new ExperimentTrial(corpusXML, corpusDir, ClusterAlgorithm.K_MEANS, data)
		};
		
		for (int i = 0; i < trials.length; i++)
			trials[i].run();
	}
	
	public static void main(String[] args) {
		if (args.length < 2) {
			printUsage();
			return;
		}
		
		try {
			corpusXML = (new SAXBuilder()).build(new File(args[0]));
			corpusDir = new File(args[1]);
			
			PApplet.main(new String[] { "edu.iit.swyne.crawler.experiment.Experiment" });
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("ExperimentTrial <corpusXML> <corpus_directory>");
		System.out.println();
	}
}
