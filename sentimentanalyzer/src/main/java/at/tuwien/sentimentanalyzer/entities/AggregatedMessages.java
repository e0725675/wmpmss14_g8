package at.tuwien.sentimentanalyzer.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.tuwien.sentimentanalyzer.entities.Message.Sentiment;
import at.tuwien.sentimentanalyzer.entities.Message.Source;
import at.tuwien.sentimentanalyzer.sample.SimpleGroupMap;

public class AggregatedMessages {
	public static class Author implements Comparable<Author>{
		public Author(String name, Source source) {
			this.name = name;
			this.source = source;
		}
		private String name;
		private Source source;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Source getSource() {
			return source;
		}
		public void setSource(Source source) {
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
		@Override
		public int compareTo(Author o) {
			int compare = this.source.compareTo(o.source);
			if (compare == 0) {
				return this.name.compareTo(o.name);
			} else {
				return compare;
			}
		}
	}
	private HashMap<Author, Integer> authors = new HashMap<Author, Integer>();
	private Date minTimePosted = new Date();
	private Date maxTimePosted = new Date();
	private SimpleGroupMap<Source, String, Integer> wordCountsBySource = new SimpleGroupMap<Source, String, Integer>();
	private SimpleGroupMap<Source, Message.Sentiment, Integer> sentimentCountsBySource = new SimpleGroupMap<Source, Message.Sentiment, Integer>();
	private HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();
	private HashMap<Source, Integer> sourceCounts = new HashMap<Source, Integer>();
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
	public HashMap<Source, Integer> getSourceCounts() {
		return sourceCounts;
	}
	public void setSourceCounts(HashMap<Source, Integer> sourceCounts) {
		this.sourceCounts = sourceCounts;
	}
	public HashMap<Message.Sentiment, Integer> getSentimentCounts() {
		return sentimentCounts;
	}
	public void setSentimentCounts(
			HashMap<Message.Sentiment, Integer> sentimentCounts) {
		this.sentimentCounts = sentimentCounts;
	}
	
	public void validate() {
		if (!this.sentimentCounts.containsKey(Sentiment.NEGATIVE)) {
			this.sentimentCounts.put(Sentiment.NEGATIVE, 0);
		}
		if (!this.sentimentCounts.containsKey(Sentiment.POSITIVE)) {
			this.sentimentCounts.put(Sentiment.POSITIVE, 0);
		}
		if (!this.sentimentCounts.containsKey(Sentiment.NEUTRAL)) {
			this.sentimentCounts.put(Sentiment.NEUTRAL, 0);
		}
	}
	public SimpleGroupMap<Source, Message.Sentiment, Integer> getSentimentCountsBySource() {
		return sentimentCountsBySource;
	}
	public void setSentimentCountsBySource(SimpleGroupMap<Source, Message.Sentiment, Integer> sentimentCountsBySource) {
		this.sentimentCountsBySource = sentimentCountsBySource;
	}
	public SimpleGroupMap<Source, String, Integer> getWordCountsBySource() {
		return wordCountsBySource;
	}
	public void setWordCountsBySource(SimpleGroupMap<Source, String, Integer> wordCountsBySource) {
		this.wordCountsBySource = wordCountsBySource;
	}
	@Override
	public String toString() {
		return "AggregatedMessages [authors=" + authors + ", minTimePosted="
				+ minTimePosted + ", maxTimePosted=" + maxTimePosted
				+ ", wordCountsBySource=" + wordCountsBySource
				+ ", sentimentCountsBySource=" + sentimentCountsBySource
				+ ", wordCounts=" + wordCounts + ", sourceCounts="
				+ sourceCounts + ", sentimentCounts=" + sentimentCounts + "]";
	}
}
