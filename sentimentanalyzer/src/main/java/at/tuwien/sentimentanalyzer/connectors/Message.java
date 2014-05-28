package at.tuwien.sentimentanalyzer.connectors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.log4j.Logger;

import twitter4j.Status;

/**
 * All messages, tweets, posts, whatever, will be aggregated to this format
 * @author CLF
 *
 */
@Converter
public class Message {
	public static Logger log = Logger.getLogger(Message.class);
	
	@Override
	public String toString() {
		return "Message [message=" + message + ", author=" + author
				+ ", timePosted=" + timePosted + ", source=" + source + "]";
	}
	/**
	 * The content of the message (e.g. a tweet, a fecebook post, ...)
	 */
	private String message;
	/**
	 * The author of the message
	 */
	private String author;
	/**
	 * When was the message posted
	 */
	private Date timePosted;
	/**
	 * From which source did the message come from? (twitter, facebook, reddit, ...)
	 * Since i do not know which sources we will have i leave this as String
	 */
	private String source;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getTimePosted() {
		return timePosted;
	}
	public void setTimePosted(Date timePosted) {
		this.timePosted = timePosted;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	@Converter
	public static Message toMessage(String string) {
		log.trace("StringToMessage"+string);
		return new Message();
	}
	
	@Converter
	public static Message statusToMessage(Status status) {
		Message m = new Message();

		m.setAuthor(status.getUser().getName());
		m.setMessage(status.getText());
		m.setTimePosted(status.getCreatedAt());
		m.setSource(null);
		log.trace("StatusToMessage"+m.toString());
		return m;
	}
	
	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy/HH/mm/ss");
	public static final int numListItems = 4;
	
	@Converter
	public static Message listToMessage(List<String> list) {
		if (list.size() != numListItems) {
			throw new RuntimeException("Invalid number of list items. Should be "+numListItems+" but was "+list.size());
		}
		
		Message m = new Message();
		m.setMessage(list.get(0));
		m.setAuthor(list.get(1));
		m.setSource(list.get(2));
		try {
			m.setTimePosted(dateFormat.parse(list.get(3)));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		log.trace("StatusToMessage"+m.toString());
		return m;
	}
	
	@Converter
	public static List<String> messageToList( Message message) {
		List<String> out = new ArrayList<String>();
		out.add(message.getMessage());
		out.add(message.getAuthor());
		out.add(message.getSource());
		out.add(dateFormat.format(message.getTimePosted()));
		return out;
	}
	
	@Converter
	public static List<List<String>> messagesToLists( List<Message> messages) {
		List<List<String>> out = new ArrayList<List<String>>();
		for (Message m : messages) {
			out.add(Message.messageToList(m));
		}
		return out;
	}
	
	@Converter
	public static List<Message> listsToMessages( List<List<String>> lists) {
		List<Message> out = new ArrayList<Message>();
		for (List<String> l : lists) {
			out.add(Message.listToMessage(l));
		}
		return out;
	}
	
	@Converter
	public static Map<String, Object> messageToMap( Message message) {
		Map<String, Object> csv = new HashMap<>();
		List<String>data = Message.messageToList(message);
		csv.put(message.getAuthor()+"-"+data.get(3), data);
		return csv;
	}
	
	@Converter
	public static Map<String, Object> messagesToMap( List<Message> messages) {
		Map<String, Object> out = new HashMap<>();
		for (Message m : messages) {
			List<String>data = Message.messageToList(m);
			out.put(m.getAuthor()+"-"+data.get(3), data);
		}
		
		return out;
	}
	
	@Converter
	public static List<Message> mapToMessages( Map<String, List<String>> map) {
		List<Message> out = new ArrayList<Message>();
		for (List<String> l : map.values()) {
			out.add(Message.listToMessage(l));
		}
		return out;
	}
	
	/*public Map<String, Object> doHandleCsvData(Message message)
	{
	    // To DO: some magic
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		log.trace("Making Map: ");
		Map<String, Object> csv = new HashMap<>();
		List<String>data = new ArrayList<>();
		data.add(message.getAuthor());
		data.add(message.getMessage());
		data.add(message.getSource());
		csv.put(df.format(message.getTimePosted()), data);
		log.trace("Map: " + csv);
		return csv;
	}*/
}
