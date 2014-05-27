package at.tuwien.sentimentanalyzer.sample;

import java.util.ArrayList;

import at.tuwien.sentimentanalyzer.beans.WordCounter;

public class TestWordCounter {
	
	public static void main(String[] args) {
		WordCounter counter = new WordCounter();
		ArrayList<String> textList  = new ArrayList<>();
		ArrayList<String> counterList  = new ArrayList<>();
		textList.add("This_DT stuff_NN sucks_VBZ it_PRP 's_VBZ really_RB shit_JJ !!!_NN");
		textList.add("I_PRP LOVE_VBP apache_JJ camel_NN ._. It_PRP is_VBZ so_RB useful_JJ");
		textList.add("Bananas_NNS apples_NNS and_CC camel_NN 's_POS are_VBP all_DT examples_NNS of_IN things_NNS");
		textList.add("MOTHER_NN FUCKING_NNP DICK_NNP BUTTER_NNP IN_IN MOTHER_NNP FUCKER_NNP !_.");
		textList.add("This_DT is_VBZ ok_JJ I_PRP guess_VBP");
		textList.add("Something_NN more_JJR than_IN this_DT will_MD do_VB");
		textList.add("Hahahaha_NNP this_DT is_VBZ the_DT best_JJS !!_NN I_PRP like_VBP it_PRP shit_JJ!_.");
		counterList = (ArrayList<String>) counter.WordCounter(textList);
		
		System.out.println("************RESULTS****************");
		for(String text: counterList)
			System.out.println(text);
	}

}