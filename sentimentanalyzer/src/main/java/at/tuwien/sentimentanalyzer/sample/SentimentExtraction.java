package at.tuwien.sentimentanalyzer.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
/*
 * Author: Lucas Gerrand
 * lucasgerrand@gmail.com
 * Using the Stanford Core NLP Library
 * 1: Negative
 * 2: Neutral
 * 3: Positive
 */

public class SentimentExtraction {
	public HashMap<String, Integer> makeSentiment(List<String> textList){
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, parse, pos, sentiment");  //tokenize, ssplit, pos, lemma, ner, parse, dcoref
		if(textList.size()<1){
			return null;
		}
		HashMap<String, Integer> resultsList = new HashMap<>();
		
			
//			System.out.println("Extracting sentiment for sentence: " + text);
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
			for(String text: textList){
			
			edu.stanford.nlp.pipeline.Annotation doc = new edu.stanford.nlp.pipeline.Annotation(text);
			pipeline.annotate(doc);
			
			List<CoreMap>sentences = doc.get(SentencesAnnotation.class);
			
			for(CoreMap sentence: sentences){
				Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
//				System.out.println("Tree");
//				System.out.println(tree);
//				Label label = tree.label();
//				System.out.println("Label: " + label);
				
				int senti = RNNCoreAnnotations.getPredictedClass(tree);
				resultsList.put(text, senti);
			}
			
		}
		
		return resultsList;
		
	}
	

}
