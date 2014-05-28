package at.tuwien.sentimentanalyzer.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.tuwien.sentimentanalyzer.beans.WordTagger;

public class TestWordTagger {

	public static void main(String[] args) {
		WordTagger tagger = new WordTagger();
		HashMap<String, List<String>> csv  = new HashMap<>();
		ArrayList<String> post  = new ArrayList<>();
		
		post.add("test");
		post.add("This stuff sucks it's really shit!!!");
		csv.put("test", post);
		
		csv = tagger.addWordtype(csv);
		
		System.out.println("************RESULTS****************");
		for(Map.Entry<String, List<String>> entry: csv.entrySet())
			System.out.println(entry.getValue().get(1));
	}
}
