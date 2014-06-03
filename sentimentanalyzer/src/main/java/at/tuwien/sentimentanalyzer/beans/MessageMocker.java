package at.tuwien.sentimentanalyzer.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.entities.Message;

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
	private static List<String> fileToList(File f) throws IOException {
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
		out.setSource("MessageMocker");
		log.info("next message");
		return out;
	}
}
