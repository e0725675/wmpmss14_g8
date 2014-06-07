package at.tuwien.sentimentanalyzer.beans;

import java.util.List;

import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.entities.Message;
import at.tuwien.sentimentanalyzer.entities.Message.Sentiment;
import at.tuwien.sentimentanalyzer.sample.SentimentExtraction;

public class SentimentBean {
	public static Logger log = Logger.getLogger(SentimentBean.class);
	
	public Message attachSentiment(Message message)
	{
		
		SentimentExtraction senti = new SentimentExtraction();
		String text = message.getMessage();
		int sentiment = senti.makeSentiment(text);
		Sentiment s = Message.intToSentiment(sentiment);
		message.setSentiment(s);
		return message;
		
	}

}
