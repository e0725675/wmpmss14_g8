package at.tuwien.sentimentanalyzer.sample;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import at.tuwien.sentimentanalyzer.beans.AggregatorConvertor;
import at.tuwien.sentimentanalyzer.entities.AggregatedMessages;
import at.tuwien.sentimentanalyzer.entities.AggregatedMessages.Author;
import at.tuwien.sentimentanalyzer.entities.Message;
import at.tuwien.sentimentanalyzer.entities.Message.Sentiment;
import at.tuwien.sentimentanalyzer.entities.Message.Source;

public class TestAggregatedMesssage {
	
	
	@Test
	public void test() {
		List<Message>testMessages = testMessages();
		AggregatedMessages agg = AggregatorConvertor.messagesToAggregateMessages(testMessages);
		assertNotNull(agg);
		
		int actual = agg.getWordCounts().get("hello");
		assertEquals(30, actual);  
		assertNotNull(agg.getWordCounts().get("goodbyezzz"));
		System.out.println(agg.getAuthors().keySet());
		assertNotNull(agg.getAuthors());
		String authorName = "John"; 
		Source authorSource = new Source("Test Message");
		Author auth = new Author(authorName,authorSource);
		assertEquals(authorName, auth.getName());
		System.out.println("Original.. auth: " + auth.getName() + " Source: " + auth.getSource());
		System.out.println("original type: " + auth.getClass());
		for(Author map: agg.getAuthors().keySet()){
			System.out.println("Auth class: " + map.getClass());
			System.out.println("Auth: " + map.getName() + " Source: " + map.getSource());
			if(map==auth){
				System.out.println("SAME!");
			}
		}
		
		
		assertNotNull(agg.getAuthors().get(auth));
		
		System.out.println("Max:" + agg.getMaxTimePosted());
		System.out.println("Min:" + agg.getMinTimePosted());
		
		assertTrue(agg.getMinTimePosted().before(agg.getMaxTimePosted()));
		
		Sentiment neg = Message.intToSentiment(1);
		
		assertEquals(Integer.valueOf(2), agg.getSentimentCounts().get(neg));
		
		
		
	}
	public static List<Message>testMessages(){
		List<Message>msgs = new ArrayList<>();
		
		Message msg = new Message();
		msg.setAuthor("John");
		msg.setMessage("OMG THERE HAHAHA");
		msg.setOriginalMessage("OMG HI THERE HAHAHA");
		msg.setSentiment(Message.intToSentiment(1));
		msg.setSource(new Source("Test Message"));
		DateTime toDay=new DateTime();
		DateTime dateOfPreviousWeek=toDay.minusDays(7);
		
		Date d = dateOfPreviousWeek.toDate();
		msg.setTimePosted(d);
		System.out.println("Second date: " +  d);
		HashMap<String, Integer>wordcounts = new HashMap<>();
		wordcounts.put("hello", 10);
		wordcounts.put("goodbye", 2);
		wordcounts.put("something", 3);
		wordcounts.put("more", 10);
		msg.setWordcounts(wordcounts);
		
		
		Message msg1 = new Message();
		msg1.setAuthor("John");
		msg1.setMessage("OMG THERE HAHAHA");
		msg1.setOriginalMessage("OMG HI THERE HAHAHA");
		msg1.setSentiment(Message.intToSentiment(1));
		msg1.setSource(new Source("Test Message"));
		
		
		DateTime dateMinusThree=toDay.minusDays(3);
		
		Date e = dateMinusThree.toDate();
		
		msg1.setTimePosted(e);
		System.out.println("Second date: " +  e);
		HashMap<String, Integer>wordcounts1 = new HashMap<>();
		wordcounts1.put("hello", 10);
		wordcounts1.put("goodbyezzz", 22);
		wordcounts1.put("somethingzzz", 32);
		wordcounts1.put("morezzz",2);
		msg1.setWordcounts(wordcounts1);
		
		Message msgs2 = new Message();
		msgs2.setAuthor("John");
		msgs2.setMessage("OMG THERE HAHAHA");
		msgs2.setOriginalMessage("OMG HI THERE HAHAHA");
		msgs2.setSentiment(Message.intToSentiment(3));
		msgs2.setSource(new Source("Test Message"));
		Date f = new Date();
		msgs2.setTimePosted(f);
		HashMap<String, Integer>wordcounts2 = new HashMap<>();
		wordcounts2.put("hello", 10);
		wordcounts2.put("goodbyezzz", 22);
		wordcounts2.put("somethingzzz", 32);
		wordcounts2.put("morezzz",2);
		msgs2.setWordcounts(wordcounts2);
		msgs.add(msg);
		msgs.add(msg1);
		msgs.add(msgs2);
		return msgs;
		
	}

}
