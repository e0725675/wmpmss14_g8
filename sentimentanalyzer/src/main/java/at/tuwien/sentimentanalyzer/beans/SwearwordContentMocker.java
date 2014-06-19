package at.tuwien.sentimentanalyzer.beans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import at.tuwien.sentimentanalyzer.beans.ReportGenerator.SwearwordReportContent;
import at.tuwien.sentimentanalyzer.beans.ReportGenerator.SwearwordReportContent.SwearInformation;
import at.tuwien.sentimentanalyzer.entities.AggregatedMessages.Author;
import at.tuwien.sentimentanalyzer.entities.Message.Source;

public class SwearwordContentMocker {
	private List<String> dictionary = null;
	private List<String> users = null;
	Random r = new Random();
	public SwearwordContentMocker(String swearwordlistfile, String userlistfile) throws IOException {
		if (swearwordlistfile == null ) throw new RuntimeException("swearwordlistfile is null");
		if (userlistfile == null ) throw new RuntimeException("userlistfile is null");
		
		File f = new File(swearwordlistfile);
		if (!f.exists()) throw new RuntimeException("swearwordlistfile "+swearwordlistfile+" does not exist");
		dictionary = MessageMocker.fileToList(f);
		
		File f2 = new File(userlistfile);
		if (!f2.exists()) throw new RuntimeException("userlistfile "+userlistfile+" does not exist");
		users = MessageMocker.fileToList(f2);
	}
	public SwearwordReportContent nextSwearwordReportContent() {
		
		SwearwordReportContent out = new SwearwordReportContent();
		final int MAXUSERS = 200;
		final int MAXOFFENCES = 30;
		final int MAXWORDS = 50;
		int numUsers = r.nextInt(MAXUSERS);
		List<String> randomUsers = MessageMocker.getRandomElements(this.users, r, numUsers);
		List<Author> authors = new ArrayList<Author>();
		for (String user : randomUsers) {
			Author author = new Author(user, new Source("MessageMocker"+r.nextInt(4)));
			authors.add(author);
		}
		HashMap<Author,SwearInformation> userInformation = new HashMap<Author,SwearInformation>();
		int offset =  777600;
		long end = new Date().getTime();
		for (Author a : authors) {
			SwearInformation si = new SwearInformation();
			List<Date> offs = si.recordedOffences;
			int numOff = r.nextInt(MAXOFFENCES+5);
			for (int i=0; i<numOff; i++) {
				Date date = new Date(end-new Long(r.nextInt(offset)));
				offs.add(date);
			}
			si.recordedOffences = offs;
			
			int numWords = r.nextInt(MAXWORDS);
			List<String> randomWords = MessageMocker.getRandomElements(dictionary, r, numWords);
			HashMap<String, Integer> words = new HashMap<String, Integer>();
			for (String word : randomWords) {
				Integer num = r.nextInt(MAXWORDS);
				words.put(word, num);
			}
			si.usedSwearWordsDecapitalized = words;
			
			userInformation.put(a, si);
		}
		out.userInformation = userInformation;
		return out;
	}
}
