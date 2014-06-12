package at.tuwien.sentimentanalyzer.entities;

import java.util.Date;
import java.util.HashMap;

public class AggregatedMessages {
	public static class Author {
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
		@Override
		public String toString() {
			return "Author [name=" + name + ", source=" + source + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result
					+ ((source == null) ? 0 : source.hashCode());
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
			Author other = (Author) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}
	}
	private HashMap<Author, Integer> authors = new HashMap<Author, Integer>();
	private Date minTimePosted = new Date();
	private Date maxTimePosted = new Date();
	private HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();
	private HashMap<String, Integer> sourceCounts = new HashMap<String, Integer>();
	private HashMap<Message.Sentiment, Integer> sentimentCounts = new HashMap<Message.Sentiment, Integer>();
	public HashMap<Author, Integer> getAuthors() {
		return authors;
	}
	public void setAuthors(HashMap<Author, Integer> authors) {
		this.authors = authors;
	}
	public Date getMinTimePosted() {
		return minTimePosted;
	}
	public void setMinTimePosted(Date minTimePosted) {
		this.minTimePosted = minTimePosted;
	}
	public Date getMaxTimePosted() {
		return maxTimePosted;
	}
	public void setMaxTimePosted(Date maxTimePosted) {
		this.maxTimePosted = maxTimePosted;
	}
	public HashMap<String, Integer> getWordCounts() {
		return wordCounts;
	}
	public void setWordCounts(HashMap<String, Integer> wordCounts) {
		this.wordCounts = wordCounts;
	}
	public HashMap<String, Integer> getSourceCounts() {
		return sourceCounts;
	}
	public void setSourceCounts(HashMap<String, Integer> sourceCounts) {
		this.sourceCounts = sourceCounts;
	}
	public HashMap<Message.Sentiment, Integer> getSentimentCounts() {
		return sentimentCounts;
	}
	public void setSentimentCounts(
			HashMap<Message.Sentiment, Integer> sentimentCounts) {
		this.sentimentCounts = sentimentCounts;
	}
	
	
}
