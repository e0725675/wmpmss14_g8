package at.tuwien.sentimentanalyzer.beans;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.entities.AggregatedMessages;
import at.tuwien.sentimentanalyzer.entities.AggregatedMessages.Author;
import at.tuwien.sentimentanalyzer.entities.Message;
import at.tuwien.sentimentanalyzer.entities.Message.Sentiment;
import at.tuwien.sentimentanalyzer.entities.Message.Source;
import at.tuwien.sentimentanalyzer.sample.SimpleGroupMap;


public class AggregatorConvertor {
	public static Logger log = Logger.getLogger(AggregatorConvertor.class);

	public AggregatedMessages messagesToAggregateMessages(List<Message> msgList){

		log.trace("Aggregated msg: " + msgList);

		AggregatedMessages agg = new AggregatedMessages();
		HashMap<Author, Integer> authorMap = new HashMap<>();
		Date maxDate = null;
		Date minDate = null;
		HashMap<Sentiment, Integer>sentimentMap = new HashMap<>();
		HashMap<Source, Integer> sourceMap = new HashMap<Source, Integer>();
		HashMap<String, Integer> wordCountMap = new HashMap<String, Integer>();
		SimpleGroupMap<Source, String, Integer> wordCountsBySource = new SimpleGroupMap<Source, String, Integer>();
		SimpleGroupMap<Source, Message.Sentiment, Integer> sentimentCountsBySource = new SimpleGroupMap<Source, Message.Sentiment, Integer>();

		for(Message message : msgList){
			if(message.getAuthor()!=null || message.getSource()!=null){
				String msgAuthor = message.getAuthor();
				Source msgSource = message.getSource();
				Author auth = new Author(msgAuthor, msgSource);

				if(authorMap.containsKey(auth)){
					authorMap.put(auth, authorMap.get(auth)+1);
				}

				else{
					authorMap.put(auth, 1);
				}			
			}
			if(message.getSource()!=null){
				Source source = message.getSource();

				if(sourceMap.containsKey(source)){
					sourceMap.put(source, sourceMap.get(source)+1);
				}
				else{
					sourceMap.put(source, 1);
				}
			}
			if(message.getTimePosted()!=null){
				Date d = message.getTimePosted();
				if(minDate ==null || d.compareTo(minDate)<=0){
					minDate = d;
				}
				if(maxDate ==null || d.compareTo(maxDate)>=0){
					maxDate = d;
				}
			}

			Sentiment senti = message.getSentiment();

			// add sentiments by source
			Integer scount = sentimentCountsBySource.get(message.getSource(), senti);
			if (scount == null) {
				scount = 1;
			} else {
				scount++;
			}
			sentimentCountsBySource.put(message.getSource(), senti, scount);

			// add sentiments
			if(sentimentMap.containsKey(senti)){
				sentimentMap.put(senti, sentimentMap.get(senti)+1);
			}
			else{
				sentimentMap.put(senti, 1);
			}

			if(message.getWordcounts()!=null){

				for(Map.Entry<String, Integer>entry: message.getWordcounts().entrySet()){
					// add wordcounts by source
					Integer wcount = wordCountsBySource.get(message.getSource(), entry.getKey());
					if (wcount == null) {
						wcount = entry.getValue();
					} else {
						wcount+=entry.getValue();
					}
					wordCountsBySource.put(message.getSource(), entry.getKey(), wcount);
					
					// add wordcounts
					if(wordCountMap.containsKey(entry.getKey())){
						int oldValue = wordCountMap.get(entry.getKey());
						int newValue = entry.getValue();
						wordCountMap.put(entry.getKey(), oldValue + newValue);
					}
					else{
						wordCountMap.put(entry.getKey(), entry.getValue());
					}
				}
			}

		}
		agg.setSentimentCountsBySource(sentimentCountsBySource);
		agg.setWordCountsBySource(wordCountsBySource);
		
		if(authorMap!=null){
			agg.setAuthors(authorMap);
		}
		if(maxDate !=null){
			agg.setMaxTimePosted(maxDate);
		}
		if(minDate !=null){
			agg.setMinTimePosted(minDate);
		}
		if(sentimentMap !=null){
			agg.setSentimentCounts(sentimentMap);
		}
		if(sourceMap !=null){
			agg.setSourceCounts(sourceMap);
		}
		if(wordCountMap !=null){
			agg.setWordCounts(wordCountMap);
		}
		log.trace("New Aggregated msg: " + agg);

		return agg;

	}


}
