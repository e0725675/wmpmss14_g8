package at.tuwien.sentimentanalyzer.beans;

import java.util.List;

import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.entities.Message;
import at.tuwien.sentimentanalyzer.sample.SentimentExtraction;

public class SentimentBean {
	public static Logger log = Logger.getLogger(SentimentBean.class);
	
	public List<String> attachSentiment(Message message)
	{
		List<String> out = MessageConverter.messageToList(message);
		SentimentExtraction senti = new SentimentExtraction();
		String text = message.getMessage();
		String sentiment = String.valueOf(senti.makeSentiment(text));
		out.add(sentiment);
		return out;
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
