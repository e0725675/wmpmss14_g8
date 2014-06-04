package at.tuwien.sentimentanalyzer.entities;

import java.util.Date;
import java.util.HashMap;

public class AggregatedMessages {
	public class Author {
		public Author(String name, String source) {
			this.name = name;
			this.source = source;
		}
		private String name;
		private String source;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
	}
	private HashMap<Author, Integer> authors = new HashMap<Author, Integer>();
	private Date minTimePosted = new Date();
	private Date maxTimePosted = new Date();
	private HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();
	private HashMap<String, Integer> sourceCounts = new HashMap<String, Integer>();
	private HashMap<Message.Sentiment, Integer> sentimentCounts = new HashMap<Message.Sentiment, Integer>();
	protected HashMap<Author, Integer> getAuthors() {
		return authors;
	}
	protected void setAuthors(HashMap<Author, Integer> authors) {
		this.authors = authors;
	}
	protected Date getMinTimePosted() {
		return minTimePosted;
	}
	protected void setMinTimePosted(Date minTimePosted) {
		this.minTimePosted = minTimePosted;
	}
	protected Date getMaxTimePosted() {
		return maxTimePosted;
	}
	protected void setMaxTimePosted(Date maxTimePosted) {
		this.maxTimePosted = maxTimePosted;
	}
	protected HashMap<String, Integer> getWordCounts() {
		return wordCounts;
	}
	protected void setWordCounts(HashMap<String, Integer> wordCounts) {
		this.wordCounts = wordCounts;
	}
	protected HashMap<String, Integer> getSourceCounts() {
		return sourceCounts;
	}
	protected void setSourceCounts(HashMap<String, Integer> sourceCounts) {
		this.sourceCounts = sourceCounts;
	}
	protected HashMap<Message.Sentiment, Integer> getSentimentCounts() {
		return sentimentCounts;
	}
	protected void setSentimentCounts(
			HashMap<Message.Sentiment, Integer> sentimentCounts) {
		this.sentimentCounts = sentimentCounts;
	}
	
}
