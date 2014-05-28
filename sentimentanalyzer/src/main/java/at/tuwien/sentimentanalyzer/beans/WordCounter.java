package at.tuwien.sentimentanalyzer.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/*
 * Author: Serafima
 */

public class WordCounter {

	public Map<String, Integer> WordCounter(ArrayList<String> textList) {

		Hashtable<String, Integer> countableWords = new Hashtable<String, Integer>();
		
		for(String text : textList){
			for(String word : text.split(" ")){
				if(countableWords.containsKey(word)){
					countableWords.put(word, countableWords.get(word) + 1);
				}else{
					countableWords.put(word,1);
				}
			}
		}
		
		
		return countableWords;
	}

}
