package at.tuwien.sentimentanalyzer.beans;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import at.tuwien.sentimentanalyzer.entities.Message;

/*
 * Author: Serafima
 */

public class WordCounter {
	private static Logger log = Logger.getLogger(WordCounter.class);
	public Message countWords(Message message) {
		
		HashMap<String, Integer> countableWords = new HashMap<String, Integer>();
		
		
			for(String word : message.getMessage().split(" ")){
				if (word.endsWith("_FW")
						|| word.endsWith("_JJ")
						|| word.endsWith("_JJR")
						|| word.endsWith("_JJS")
						|| word.endsWith("_NN")
						|| word.endsWith("_NNP")
						|| word.endsWith("_NNPS")
						|| word.endsWith("_NNS")
						|| word.endsWith("_VB")
						|| word.endsWith("_VBD")
						|| word.endsWith("_VBG")
						|| word.endsWith("_VBN")
						|| word.endsWith("_VBP")
						|| word.endsWith("_VBZ")
						|| word.endsWith("_RB")
						|| word.endsWith("_RBR")
						|| word.endsWith("_RBS")
						) {
					//log.info("word1: "+word+"#");
					String wordMod = (word.split("_")[0]);
					//log.info("word2: "+wordMod+"#");
					if(countableWords.containsKey(wordMod)){
						countableWords.put(wordMod, countableWords.get(wordMod) + 1);
					}else{
						countableWords.put(wordMod,1);
					}
				}
			}
		message.setWordcounts(countableWords);
		return message;
	}

}
