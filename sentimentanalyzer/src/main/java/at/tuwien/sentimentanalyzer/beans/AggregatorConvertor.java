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


public class AggregatorConvertor {
	public static Logger log = Logger.getLogger(AggregatorConvertor.class);
	
	public static AggregatedMessages messagesToAggregateMessages(List<Message> msgList){
		
		log.info("Aggregated msg: " + msgList);
		
		AggregatedMessages agg = new AggregatedMessages();
		HashMap<Author, Integer> authorMap = new HashMap<>();
		Date maxDate = null;
		Date minDate = null;
		HashMap<Sentiment, Integer>sentimentMap = new HashMap<>();
		HashMap<String, Integer> sourceMap = new HashMap<String, Integer>();
		HashMap<String, Integer> wordCountMap = new HashMap<String, Integer>();
		
		for(int i=0;i<msgList.size();i++){
			if(msgList.get(i).getAuthor()!=null || msgList.get(i).getSource()!=null){
				String msgAuthor = msgList.get(i).getAuthor();
				String msgSource = msgList.get(i).getSource();
				Author auth = new Author(msgAuthor, msgSource);
				
				if(authorMap.containsKey(auth)){
					authorMap.put(auth, authorMap.get(auth)+1);
				}
				
				else{
					authorMap.put(auth, 1);
				}			
			}
			if(msgList.get(i).getTimePosted()!=null){
				Date d = msgList.get(i).getTimePosted();
				if(minDate ==null || d.compareTo(minDate)<=0){
					minDate = d;
				}
				if(maxDate ==null || d.compareTo(maxDate)>=0){
					maxDate = d;
				}
			}
			
			Sentiment senti = msgList.get(i).getSentiment();
			if(sentimentMap.containsKey(senti)){
				sentimentMap.put(senti, sentimentMap.get(senti)+1);
			}
			else{
				sentimentMap.put(senti, 1);
			}
			if(msgList.get(i).getSource()!=null){
				String source = msgList.get(i).getSource();
				
				if(sourceMap.containsKey(source)){
					sourceMap.put(source, sourceMap.get(source)+1);
				}
				else{
					sourceMap.put(source, 1);
				}
			}
			if(msgList.get(i).getWordcounts()!=null){
				for(Map.Entry<String, Integer>entry: msgList.get(i).getWordcounts().entrySet()){
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
		log.info("New Aggregated msg: " + agg);
		
		return agg;
		
	}
	

}
