package at.tuwien.sentimentanalyzer.connectors;

import java.util.Date;

/**
 * All messages, tweets, posts, whatever, will be aggregated to this format
 * @author CLF
 *
 */
public class Message {
	
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
	 * From which source did the message come from? (twitter, facebook, reddit, ...)
	 * Since i do not know which sources we will have i leave this as String
	 */
	private String source;
	
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
}
