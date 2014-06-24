package at.tuwien.sentimentanalyzer.sample;

import at.tuwien.sentimentanalyzer.beans.WordTagger;
import at.tuwien.sentimentanalyzer.entities.Message;

public class TestWordTagger {

	public static void main(String[] args) {
		WordTagger tagger = new WordTagger();
		Message msg  = new Message();
//		ArrayList<String> post  = new ArrayList<>();
			
		msg.setMessage("This is a test! Does it work?");
		
		msg = tagger.addWordtype(msg);
		
		System.out.println("************RESULTS****************");
//		//for(Map.Entry<String, List<String>> entry: csv.entrySet())
//		//	System.out.println(entry.getValue().get(1));
		System.out.println(msg);
	}
}