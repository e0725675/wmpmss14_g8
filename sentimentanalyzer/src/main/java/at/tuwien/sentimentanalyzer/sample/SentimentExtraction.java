package at.tuwien.sentimentanalyzer.sample;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
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
	public Integer makeSentiment(String text){
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, parse, pos, sentiment");  //tokenize, ssplit, pos, lemma, ner, parse, dcoref
		
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
			
		edu.stanford.nlp.pipeline.Annotation doc = new edu.stanford.nlp.pipeline.Annotation(text);
		pipeline.annotate(doc);
			
		List<CoreMap>sentences = doc.get(SentencesAnnotation.class);
		int senti = -10;
		for(CoreMap sentence: sentences){
			Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
			senti = RNNCoreAnnotations.getPredictedClass(tree);				
		}

		return senti;
	}

}
