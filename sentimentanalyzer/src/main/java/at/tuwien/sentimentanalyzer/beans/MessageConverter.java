package at.tuwien.sentimentanalyzer.beans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import twitter4j.Status;
import at.tuwien.sentimentanalyzer.entities.Message;
import at.tuwien.sentimentanalyzer.entities.Message.Source;

public class MessageConverter {

	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy/HH/mm/ss");
	public static final int numListItems = 4;
	
	public static Logger log = Logger.getLogger(MessageConverter.class);
	
	public static String messageToString(Message message) {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	public static Message stringToMessage(String string) {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	
	public static Message statusToMessage(Status status) {
		if (status == null) {
			throw new RuntimeException("input Status is null");
		}
		Message m = new Message();

		m.setAuthor(status.getUser().getName());
		m.setMessage(status.getText());
		m.setTimePosted(status.getCreatedAt());
		m.setSource(null);
		log.trace("StatusToMessage"+m.toString());
		return m;
	}
	
	
	public static Message listToMessage(List<String> list) {
		if (list == null) {
			throw new RuntimeException("input list is null");
		}
		if (list.size() != numListItems) {
			throw new RuntimeException("Invalid number of list items. Should be "+numListItems+" but was "+list.size());
		}
		
		Message m = new Message();
		m.setMessage(list.get(0));
		m.setAuthor(list.get(1));
		m.setSource(new Source(list.get(2)));
		try {
			m.setTimePosted(dateFormat.parse(list.get(3)));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		log.trace("StatusToMessage"+m.toString());
		return m;
	}
	
	public static List<String> messageToList( Message message) {
		if (message == null) {
			throw new RuntimeException("input message is null");
		}
		List<String> out = new ArrayList<String>();
		out.add(message.getMessage());
		out.add(message.getAuthor());
		out.add(message.getSource().toString());
		out.add(dateFormat.format(message.getTimePosted()));
		return out;
	}
	
	public static List<List<String>> messagesToLists( List<Message> messages) {
		if (messages == null) {
			throw new RuntimeException("input list of messages is null");
		}
		List<List<String>> out = new ArrayList<List<String>>();
		for (Message m : messages) {
			out.add(MessageConverter.messageToList(m));
		}
		return out;
	}
	
	public static List<Message> listsToMessages( List<List<String>> lists) {
		if (lists == null) {
			throw new RuntimeException("input list is null");
		}
		log.debug(lists);
		List<Message> out = new ArrayList<Message>();
		for (List<String> l : lists) {
			out.add(MessageConverter.listToMessage(l));
		}
		return out;
	}
	public static List<Map<String, Object>> messagesToMaplist( List<Message> messages) {
		if (messages == null) {
			throw new RuntimeException("input list of messages is null");
		}
		List<Map<String, Object>> out = new ArrayList<Map<String,Object>>();
		for (Message m : messages) {
			out.add(MessageConverter.messageToMap(m));
		}
		return out;
	}
	
	public static Map<String, Object> messageToMap( Message message) {
		if (message == null) {
			throw new RuntimeException("input message is null");
		}
		Map<String, Object> csv = new TreeMap<String,Object>();
		//List<String>data = MessageConverter.messageToList(message);
		//csv.put(message.getAuthor()+"-"+data.get(3), data);
		
		csv.put("1message",message.getMessage());
		csv.put("2author",message.getAuthor());
		csv.put("3source",message.getSource());
		csv.put("4timeposted",dateFormat.format(message.getTimePosted()));
		return csv;
	}
	
	public static Map<String, Object> messagesToMap( List<Message> messages) {
		if (messages == null) {
			throw new RuntimeException("input list ofmessages is null");
		}
		Map<String, Object> out = new HashMap<>();
		for (Message m : messages) {
			List<String>data = MessageConverter.messageToList(m);
			out.put(m.getAuthor()+"-"+data.get(3), data);
		}
		
		return out;
	}
	
	public static List<Message> mapToMessages( Map<String, List<String>> map) {
		if (map == null) {
			throw new RuntimeException("input map is null");
		}
		List<Message> out = new ArrayList<Message>();
		for (List<String> l : map.values()) {
			out.add(MessageConverter.listToMessage(l));
		}
		return out;
	}
}
