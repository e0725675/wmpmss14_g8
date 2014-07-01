package at.tuwien.sentimentanalyzer.entities;

import java.io.Serializable;
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
	private String id = null;
	/**
	 * Defines a Source for a Message Object
	 * @author CLF
	 *
	 */
	public static class Source implements Comparable<Source>,Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6965338746715206888L;
		private String source;
		
		public Source(String source) {
			if (source == null || source.equals("")) {
				throw new RuntimeException("Invalid source. Cannot be null or empty String");
			}
			this.source = source;
		}

		@Override
		public String toString() {
			return  this.source;
		}

		@Override
		public int hashCode() {
			return this.source.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Source other = (Source) obj;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}

		@Override
		public int compareTo(Source o) {
			return this.source.compareTo(o.source);
		}

	}
	
//	public static Sentiment intToSentiment(Integer i) {
//		switch(i) {
//		case 1:
//			return Sentiment.NEGATIVE;
//		case 2:
//			return Sentiment.NEUTRAL;
//		case 3:
//			return Sentiment.POSITIVE;
//		default:
//			throw new RuntimeException("Invalid input integer "+i+". Has to be 1, 2 or 3");
//		}
//	}
	public static class Sentiment implements Comparable<Sentiment> {
		private static final int I_POSITIVE = 3;
		private static final int I_NEUTRAL = 2;
		private static final int I_NEGATIVE = 1;
		private static final int I_INVALID = 0;
		public final static Sentiment POSITIVE = new Sentiment(I_POSITIVE);
		public final static Sentiment NEUTRAL = new Sentiment(I_NEUTRAL);
		public final static Sentiment NEGATIVE = new Sentiment(I_NEGATIVE);
		public Sentiment(String sentiment) {
			if (sentiment == null) throw new RuntimeException("Sentiment string is null");
			if (sentiment.equalsIgnoreCase("POSITIVE")) this.sentiment = I_POSITIVE;
			if (sentiment.equalsIgnoreCase("NEUTRAL")) this.sentiment = I_NEUTRAL;
			if (sentiment.equalsIgnoreCase("NEGATIVE")) this.sentiment = I_NEGATIVE;
			else {
				log.warn("Sentiment: Invalid sentiment "+sentiment);
				this.sentiment=I_INVALID;
				//throw new RuntimeException("Invalid sentiment string "+sentiment);
			}
		}
		public Sentiment(int sentiment) {
			if (sentiment < I_NEGATIVE || sentiment > I_POSITIVE) {
				//throw new RuntimeException("Invalid sentiment "+sentiment);
				log.warn("Sentiment: Invalid sentiment "+sentiment);
				this.sentiment=I_INVALID;
			} else 
				this.sentiment = sentiment;
		}
		private Integer sentiment;
		
		@Override
		public int compareTo(Sentiment o) {
			return this.sentiment.compareTo(o.sentiment);
		}
		@Override
		public String toString() {
			switch(sentiment) {
			case I_NEGATIVE: return "NEGATIVE";
			case I_NEUTRAL: return "NEUTRAL";
			case I_POSITIVE: return "POSITIVE";
			default: return "INVALID";
			}
		}

		public int toInt() {
			return this.sentiment;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((sentiment == null) ? 0 : sentiment.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Sentiment other = (Sentiment) obj;
			if (sentiment == null) {
				if (other.sentiment != null)
					return false;
			} else if (!sentiment.equals(other.sentiment))
				return false;
			return true;
		}
	}
	public static Logger log = Logger.getLogger(Message.class);
	
	@Override
	public String toString() {
		return "Message [originalMessage=" + originalMessage + ", message="
				+ message + ", author=" + author + ", timePosted=" + timePosted
				+ ", sentiment=" + sentiment + ", source=" + source
				+ ", wordcounts=" + wordcounts + "]";
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
	private Source source;
	
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
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
