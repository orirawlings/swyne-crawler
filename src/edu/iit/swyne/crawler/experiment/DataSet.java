package edu.iit.swyne.crawler.experiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.iit.swyne.crawler.extractor.Clusterer.ClusterAlgorithm;

public class DataSet {
	
	private HashMap<ClusterAlgorithm, HashMap<String, ArrayList<Data>>> data = new HashMap<ClusterAlgorithm, HashMap<String, ArrayList<Data>>>();

	public void addData(Data dataPoint) {
		if (data.get(dataPoint.getAlgorithm()) == null)
			data.put(dataPoint.getAlgorithm(), new HashMap<String, ArrayList<Data>>());
		HashMap<String, ArrayList<Data>> m = data.get(dataPoint.getAlgorithm());
		
		if (m.get(dataPoint.getCollection()) == null)
			m.put(dataPoint.getCollection(), new ArrayList<Data>());
		ArrayList<Data> points = m.get(dataPoint.getCollection());
		
		points.add(dataPoint);
	}
	
	public double getAveragePrecision(ClusterAlgorithm algo) throws DataNotFoundException {
		double result = 0;
		int total = 0;
		
		HashMap<String, ArrayList<Data>> m = data.get(algo);
		if (m == null) throw new DataNotFoundException();
		
		Iterator<ArrayList<Data>> lists = m.values().iterator();
		
		while (lists.hasNext()) {
			ArrayList<Data> dataPoints = (ArrayList<Data>) lists.next();
			for (Iterator<Data> i = dataPoints.iterator(); i.hasNext();) {
				Data data = (Data) i.next();
				result += data.getPrecision();
				total++;
			}
		}
		return result/total;
	}

	public double getAverageRecall(ClusterAlgorithm algo) throws DataNotFoundException {
		double result = 0;
		int total = 0;

		HashMap<String, ArrayList<Data>> m = data.get(algo);
		if (m == null) throw new DataNotFoundException();
		
		Iterator<ArrayList<Data>> lists = m.values().iterator();
		
		while (lists.hasNext()) {
			ArrayList<Data> dataPoints = (ArrayList<Data>) lists.next();
			for (Iterator<Data> i = dataPoints.iterator(); i.hasNext();) {
				Data data = (Data) i.next();
				result += data.getRecall();
				total++;
			}
		}
		return result/total;
	}

	public double getAveragePrecision(String collection) throws DataNotFoundException {
		double result = 0;
		int total = 0;
		
		for (Iterator<HashMap<String, ArrayList<Data>>> i = data.values().iterator(); i.hasNext();) {
			HashMap<String, ArrayList<Data>> map = (HashMap<String, ArrayList<Data>>) i.next();
			ArrayList<Data> l = map.get(collection);
			if (l != null)
				for (Iterator<Data> j = l.iterator(); j.hasNext();) {
					Data d = j.next();
					result += d.getPrecision();
					total++;
				}
		}
		if (total == 0) throw new DataNotFoundException();
		return result/total;
	}

	public double getAverageRecall(String collection) throws DataNotFoundException {
		double result = 0;
		int total = 0;
		
		for (Iterator<HashMap<String, ArrayList<Data>>> i = data.values().iterator(); i.hasNext();) {
			HashMap<String, ArrayList<Data>> map = (HashMap<String, ArrayList<Data>>) i.next();
			ArrayList<Data> l = map.get(collection);
			if (l != null)
				for (Iterator<Data> j = l.iterator(); j.hasNext();) {
					Data d = j.next();
					result += d.getRecall();
					total++;
				}
		}
		if (total == 0) throw new DataNotFoundException();
		return result/total;
	}

	public double getAveragePrecision(ClusterAlgorithm algo, String collection) throws DataNotFoundException {
		double result = 0;
		int total = 0;
		
		HashMap<String, ArrayList<Data>> m = data.get(algo);
		if (m == null) throw new DataNotFoundException();
		
		ArrayList<Data> l = m.get(collection);
		if (l == null) throw new DataNotFoundException();
		
		for (Iterator<Data> i = l.iterator(); i.hasNext();) {
			Data d = i.next();
			result += d.getPrecision();
			total++;
		}
		return result/total;
	}

	public class DataNotFoundException extends Exception {
		private static final long serialVersionUID = 7814862054184867562L;
	}
}
