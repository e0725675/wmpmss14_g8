package at.tuwien.sentimentanalyzer.beans;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

/*
 * Author: Serafima
 */

public class WordCounter {

	public ArrayList<String> WordCounter(ArrayList<String> textList) {
		int wordMaximum =10;
		Hashtable<String, Integer> countableWords = new Hashtable<String, Integer>();
		
		for(String text : textList){
			for(String word : text.split(" ")){
				if(countableWords.contains(word)){
					countableWords.put(word, countableWords.get(word) + 1);
				}else{
					countableWords.put(word,1);
				}
			}
		}
		
		
		return null;
	}

}
