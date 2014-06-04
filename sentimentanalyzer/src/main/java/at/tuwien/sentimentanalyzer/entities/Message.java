package at.tuwien.sentimentanalyzer.entities;

import java.util.Date;
import java.util.HashMap;

import org.apache.camel.Converter;
import org.apache.log4j.Logger;

/**
 * All messages, tweets, posts, whatever, will be aggregated to this format
 * @author CLF
 *
 */
@Converter
public class Message {
	public static Sentiment intToSentiment(Integer i) {
		switch(i) {
		case 1:
			return Sentiment.NEGATIVE;
		case 2:
			return Sentiment.NEUTRAL;
		case 3:
			return Sentiment.POSITIVE;
		default:
			throw new RuntimeException("Invalid input integer "+i+". Has to be 1, 2 or 3");
		}
	}
	public static enum Sentiment{
		POSITIVE,
		NEUTRAL,
		NEGATIVE
	}
	public static Logger log = Logger.getLogger(Message.class);
	
	@Override
	public String toString() {
		return "Message [message=" + message + ", author=" + author
				+ ", timePosted=" + timePosted + ", source=" + source + "]";
	}
	/**
	 * Unedited content of the message (e.g. a tweet, a fecebook post, ...)
	 */
	private String originalMessage;
	
	/**
	 * The content of the message (e.g. a tweet, a fecebook post, ...)
	 */
	private String message;
	/**
	 * The author of the message
	 */
	private String author;
	/**
	 * When was the message posted
	 */
	private Date timePosted;
	/**
	 * Lucas will add sentiment here
	 */
	private Sentiment sentiment = null;
	
	/**
	 * From which source did the message come from? (twitter, facebook, reddit, ...)
	 * Since i do not know which sources we will have i leave this as String
	 */
	private String source;
	
	/**
	 * Saves the wordcount for each word in this message.
	 * CASE INSENSITIVE!!!
	 * At the end those will be aggregated together
	 */
	private HashMap<String, Integer> wordcounts;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getTimePosted() {
		return timePosted;
	}
	public void setTimePosted(Date timePosted) {
		this.timePosted = timePosted;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Sentiment getSentiment() {
		return sentiment;
	}
	public void setSentiment(Sentiment sentiment) {
		this.sentiment = sentiment;
	}
	public String getOriginalMessage() {
		return originalMessage;
	}
	public void setOriginalMessage(String originalMessage) {
		this.originalMessage = originalMessage;
	}
	public HashMap<String, Integer> getWordcounts() {
		return wordcounts;
	}
	public void setWordcounts(HashMap<String, Integer> wordcounts) {
		this.wordcounts = wordcounts;
	}
	
}
