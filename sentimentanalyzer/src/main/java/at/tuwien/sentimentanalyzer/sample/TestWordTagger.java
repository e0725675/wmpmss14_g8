package at.tuwien.sentimentanalyzer.sample;

import java.util.ArrayList;

import at.tuwien.sentimentanalyzer.beans.WordTagger;

public class TestWordTagger {

	public static void main(String[] args) {
		WordTagger tagger = new WordTagger();
		ArrayList<String> textList  = new ArrayList<>();
		ArrayList<String> taggedList  = new ArrayList<>();
		textList.add("This stuff sucks it's really shit!!!");
		textList.add("I LOVE apache camel. It is so useful");
		textList.add("Bananas apples and camel's are all examples of things");
		textList.add("MOTHER FUCKING DICK BUTTER IN MOTHER FUCKER!");
		textList.add("This is ok I guess");
		textList.add("Something more than this will do");
		textList.add("Hahahaha this is the best!! I like it!");
		taggedList = (ArrayList<String>) tagger.addWordtype(textList);
		
		System.out.println("************RESULTS****************");
		for(String text: taggedList)
			System.out.println(text);
	}
}
