package at.tuwien.sentimentanalyzer.sample;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/*
 * Author: Lucas Gerrand
 * lucasgerrand@gmail.com
 * Using the Stanford Core NLP Library
 * 1: Negative
 * 2: Neutral
 * 3: Positive
 * 
 * Load the tests into a list, pass to the extractor
 * Output: A hasmap with key: text, value: sentiment as an integer
 */
public class TestSentimentExtractor {
	
	public static void main(String[] args) {
		
		SentimentExtraction senti = new SentimentExtraction();
		ArrayList<String> textList  = new ArrayList<>();
		textList.add("This stuff sucks it's really shit!!!");
		textList.add("I LOVE apache camel. It is so useful");
		textList.add("Bananas apples and camel's are all examples of things");
		textList.add("MOTHER FUCKING DICK BUTTER IN MOTHER FUCKER!");
		textList.add("This is ok I guess");
		textList.add("Something more than this will do");
		textList.add("Hahahaha this is the best!! I like it!");
		
		System.out.println("************RESULTS****************");
		HashMap<String, Integer>sentiments = senti.makeSentiment(textList);
		for(Map.Entry<String, Integer> entry: sentiments.entrySet()){
			System.out.println("Text: " + entry.getKey());
			System.out.println("Sentiment: " + entry.getValue());
		}
		
	}

}
