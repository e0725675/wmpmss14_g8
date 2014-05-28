package at.tuwien.sentimentanalyzer.connectors;


import org.apache.camel.Converter;
import org.apache.log4j.Logger;

import twitter4j.Status;

@Converter
public class TwitterConverter {
	public static Logger log = Logger.getLogger(TwitterConverter.class);
	
	@Converter
	public static Message toMessage(String string) {
		log.trace("StringToMessage"+string);
		return new Message();
	}
	
	
	@Converter
	public static Message toMessage(Status status) {
		Message m = new Message();
		m.setMessage(status.getText());
		m.setAuthor(status.getUser().getName());
		m.setTimePosted(status.getCreatedAt());
		m.setSource(null);
		log.trace("StatusToMessage"+m.toString());
		return m;
	}
	
}
