package at.tuwien.sentimentanalyzer.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.sample.SentimentExtraction;

public class SentimentBean {
	public static Logger log = Logger.getLogger(SentimentBean.class);
	
	public List<List<String>> attachSentiment(List<List<String>> csv)
	{
		List<List<String>> out = new ArrayList<List<String>>();
		SentimentExtraction senti = new SentimentExtraction();
		
		for (List<String> l : csv) {
			String text = l.get(0);
			String sentiment = String.valueOf(senti.makeSentiment(text));
			l.add(sentiment);
			out.add(l);
		}
		return csv;
	}
	/*public HashMap<String, List<String>> attachSentiment(HashMap<String, List<String>> csv)
	{
		SentimentExtraction senti = new SentimentExtraction();
		
		for(Map.Entry<String, List<String>> entry: csv.entrySet()){
			System.out.println("Key: " + entry.getKey());
			// first element in the array should be the message. Then get the sentiment of the message
			String text = entry.getValue().get(1);
			String sentiment = String.valueOf(senti.makeSentiment(text));
			entry.getValue().add(0, sentiment);
			
		}
		log.trace("Map: " + csv);
		return csv;
	}*/
}