package at.tuwien.sentimentanalyzer.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.entities.AggregatedMessages;
import at.tuwien.sentimentanalyzer.entities.AggregatedMessages.Author;
import at.tuwien.sentimentanalyzer.entities.Message;
import at.tuwien.sentimentanalyzer.entities.Message.Sentiment;
import at.tuwien.sentimentanalyzer.entities.Message.Source;
import at.tuwien.sentimentanalyzer.sample.SimpleGroupMap;

/**
 * since we overuse twitter...
 * @author CLF
 *
 */
public class MessageMocker {
	private static Logger log = Logger.getLogger(MessageMocker.class);
	private final int MAX_EXTRA_WORDS = 20;
	List<String> users = new ArrayList<String>();
	List<String> dictionary = new ArrayList<String>();
	List<List<String>>mandatory_dics = new ArrayList<List<String>>();
	Random r = new Random();
	/**
	 * 
	 * @param userFile - file with a list of users to use
	 * @param dictionaryFile - file with a list of words to use
	 * @param mandatoryDictionaries - optional. you are guaranteed one word in each message from each dictionary
	 * @throws IOException 
	 */
	public MessageMocker(String userFile, String dictionaryFile, List<String>mandatoryDictionaries) throws IOException {
		File f_userFile = new File(userFile);
		if(!f_userFile.exists()) {
			throw new FileNotFoundException("Userfile "+userFile+" does not exist");
		}
		File f_dictionaryFile = new File(dictionaryFile);
		if(!f_dictionaryFile.exists()) {
			throw new FileNotFoundException("Dictionaryfile "+dictionaryFile+" does not exist");
		}
		if (mandatoryDictionaries != null) {
			List<File> f_mandatoryDictionaries = new ArrayList<File>();
			for (String f : mandatoryDictionaries) {
				File file = new File(f);
				
				if(!file.exists()) {
					throw new FileNotFoundException("Mandatory dictionaryfile "+f+" does not exist");
				}
				f_mandatoryDictionaries.add(file);
			}
			for (File f : f_mandatoryDictionaries) {
				this.mandatory_dics.add(MessageMocker.fileToList(f));
			}
		}
		
		this.users = MessageMocker.fileToList(f_userFile);
		this.dictionary = MessageMocker.fileToList(f_dictionaryFile);
		log.info("MessageMocker initialized with: "+userFile+" "+dictionaryFile);
	}
	public static List<String> fileToList(File f) throws IOException {
		ArrayList<String> out = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while ((line = br.readLine()) != null) {
			if (!line.equals("")) {
				out.add(line);
			}
		}
		br.close();
		return out;
	}
	private static <T> T getRandomElement(List<T> list, Random r) {
		if (list == null ||list.isEmpty() || r == null) {
			return null;
		}
		return list.get(r.nextInt(list.size()));
	}
	private static <T> List<T> getRandomElements(List<T> list, Random r, int numElements) {
		if (list == null ||list.isEmpty() || r == null) {
			throw new RuntimeException("invalid input parameter");
		}
		if (numElements < 0) {
			throw new RuntimeException("numElements cannot be smaller than 0");
		}
		if (list.size() < numElements) {
			throw new RuntimeException("input list ("+list.size()+") is smaller than the number of elements you want ("+numElements+").");
		}
		ArrayList<T> in = new ArrayList<T>(list);
		List<T> out = new ArrayList<T>();
		for (int i=0; i<numElements; i++) {
			int index = r.nextInt(in.size());
			out.add(in.get(index));
			in.remove(index);
		}
		return out;
	}
	/**
	 * Generates a random message
	 * @return
	 */
	public Message nextMessage() {
		Message out = new Message();
		String user = MessageMocker.getRandomElement(users, r);
		int wordCount=1 +r.nextInt(MAX_EXTRA_WORDS);
		ArrayList<String> words = new ArrayList<String>();
		for (List<String> dic : mandatory_dics) {
			words.add(MessageMocker.getRandomElement(dic, r));
		}
		for (int i=0; i<wordCount; i++) {
			words.add(MessageMocker.getRandomElement(dictionary, r));
		}
		Collections.shuffle(words);
		String message = "";
		for (int i=0; i<words.size(); i++) {
			if (i!=0) {
				message += " ";
			}
			message += words.get(i);
			int point =  r.nextInt(20);
			switch(point) {
			case 0:
				message+=".";break;
			case 1:
				message+="!";break;
			case 2:
				message+="?";break;
			case 3:
				message+=",";break;
			default:
				//skip
			}
		}
		out.setAuthor(user);
		out.setTimePosted(new Date());
		out.setMessage(message);
		int i_src = r.nextInt(4)+1;
		out.setSource(new Source("MessageMocker "+i_src));
		log.debug("next message");
		return out;
	}
	/**
	 * Generates a random AggregatedMessages
	 * @param numberOfMessagesIncluded - number of messages that will be generated
	 * @return
	 */
	public AggregatedMessages nextAggregatedMessage() {
		if (this.users.size() == 0) {
			throw new MessageMockerException("User list size is 0");
		}
		if (this.dictionary.size() == 0) {
			throw new MessageMockerException("Dictionary list size is 0");
		}
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 01, 01, 1, 2, 3);
		final Date MINTIMEPOSTED = cal.getTime();
		cal.set(2014, 02, 02, 2, 3, 4);
		final Date MAXTIMEPOSTED = cal.getTime();
		
		final int MAXUSERS = 20;
		final int MAXWORDS = 200;
		final int MAXWORDSPERSOURCE = 70;
		final int MAXSOURCECOUNTS = 50;
		final int MAXSENTIMENTCOUNTSPERSOURCE = 50;
		
		AggregatedMessages out = new AggregatedMessages();
		
		out.setMinTimePosted(MINTIMEPOSTED);
		out.setMaxTimePosted(MAXTIMEPOSTED);
		
		HashMap<Source, Integer> sourceCounts = new HashMap<Source, Integer>();
		int numSourceCounts = r.nextInt(MAXSOURCECOUNTS);
		sourceCounts.put(new Source("MessageMocker"), numSourceCounts);
		numSourceCounts = r.nextInt(MAXSOURCECOUNTS);
		sourceCounts.put(new Source("MessageMocker2"), numSourceCounts);
		numSourceCounts = r.nextInt(MAXSOURCECOUNTS);
		sourceCounts.put(new Source("MessageMocker3"), numSourceCounts);
		out.setSourceCounts(sourceCounts);
		
		Set<Source> sources = out.getSourceCounts().keySet();
		
		
		int numUsers = r.nextInt(MAXUSERS);
		if (numUsers == 0) numUsers = 1;
		List<String> randomUsers = MessageMocker.getRandomElements(this.users, r, numUsers);
		HashMap<Author, Integer> authors = new HashMap<Author, Integer>();
		for (String user : randomUsers) {
			Author author = new Author(user, new Source("MessageMocker"));
			Integer num = r.nextInt(MAXUSERS);
			authors.put(author, num);
		}
		out.setAuthors(authors);
		
		int numWords = r.nextInt(MAXWORDS);
		List<String> randomWords = MessageMocker.getRandomElements(this.dictionary, r, numWords);
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		for (String word : randomWords) {
			Integer num = r.nextInt(MAXWORDS);
			words.put(word, num);
		}
		out.setWordCounts(words);
		SimpleGroupMap<Source,String,Integer> wordsPerSource = new SimpleGroupMap<Source,String,Integer>();
		
		for (Source source : sources) {
			List<String> randomWordsPerSource = MessageMocker.getRandomElements(this.dictionary, r, r.nextInt(MAXWORDSPERSOURCE));
			//HashMap<String, Integer> words = new HashMap<String, Integer>();
			for (String word : randomWordsPerSource) {
				Integer num = r.nextInt(MAXWORDSPERSOURCE);
				wordsPerSource.put(source, word, num);
			}
		}
		out.setWordCountsBySource(wordsPerSource);
		
		HashMap<Sentiment, Integer> sentimentCounts = new HashMap<Sentiment, Integer>();
		
		SimpleGroupMap<Source,Sentiment,Integer> sentPerSource = new SimpleGroupMap<Source,Sentiment,Integer>();
		int totalSent = 0;
		for (Source source : sources) {
			int count = r.nextInt(MAXSENTIMENTCOUNTSPERSOURCE);
			sentPerSource.put(source, Sentiment.NEGATIVE, count);
			totalSent+=count;
		}
		sentimentCounts.put(Sentiment.NEGATIVE, totalSent);
		
		totalSent = 0;
		for (Source source : sources) {
			int count = r.nextInt(MAXSENTIMENTCOUNTSPERSOURCE);
			sentPerSource.put(source, Sentiment.NEUTRAL, count);
			totalSent+=count;
		}
		sentimentCounts.put(Sentiment.NEUTRAL, totalSent);
		
		totalSent = 0;
		for (Source source : sources) {
			int count = r.nextInt(MAXSENTIMENTCOUNTSPERSOURCE);
			sentPerSource.put(source, Sentiment.POSITIVE, count);
			totalSent+=count;
		}
		sentimentCounts.put(Sentiment.POSITIVE, totalSent);
		
		out.setSentimentCounts(sentimentCounts);
		out.setSentimentCountsBySource(sentPerSource);
		
		
		
		return out;
	}
	public static class MessageMockerException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8136826189510596543L;

		public MessageMockerException(String msg) {
			super(msg);
		}
	}
}
