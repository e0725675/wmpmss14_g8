package at.tuwien.sentimentanalyzer.beans;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.entities.Message;
import at.tuwien.sentimentanalyzer.entities.Message.Source;
import at.tuwien.sentimentanalyzer.sample.ExpJSON;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FacebookParser {

	public FacebookParser() {
	}
	private static Logger log = Logger.getLogger(FacebookParser.class);
	private static DateFormat facebookDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+0000'");
	private static Source source = new Source("Facebook");
	public void parseJson(Exchange exchange) throws IOException, ParseException {
		log.debug("parseJson");
		exchange.setOut(exchange.getIn());
		
		String file = ReportGenerator.fileToString(new File("sample.facebook.txt"));
		HashMap<String,Message> out = new HashMap<String,Message>();
		ObjectMapper mapper = new ObjectMapper();
        // use the ObjectMapper to read the json string and create a tree
        JsonNode root = mapper.readTree(file);
        JsonNode data = root.get("data");

        for (Iterator<JsonNode> it = data.elements();it.hasNext();) {
        	out = parseFacebookMessage(it.next(), out);
        }
        log.debug("parseJson parsed "+out.size()+" messages");
		exchange.getOut().setBody(new ArrayList<Message>(out.values()));
	}
	private static HashMap<String,Message> parseFacebookMessage(JsonNode js, HashMap<String,Message> out) throws ParseException {
		for (Iterator<JsonNode> it = js.elements();it.hasNext();) {
        	out = parseFacebookMessage(it.next(), out);
        }

		Message m = new Message();m.setSource(source);
		JsonNode id = js.get("id");
		if (id == null) return out;
		JsonNode message = js.get("message");
		if (message == null) return out;
		JsonNode from = js.get("from");
		if (from == null) return out;
		JsonNode name = from.get("name");
		if (name == null) return out;
		JsonNode idname = from.get("id");
		if (idname == null) return out;
		JsonNode created_time = js.get("created_time");
		if (created_time == null) return out;
		
    	m.setId(id.asText());
    	m.setAuthor(name.asText()+"_"+idname.asText());
    	m.setTimePosted(facebookDateFormat.parse((created_time.asText())));
    	m.setMessage(message.asText());
    	m.setOriginalMessage(m.getMessage());
    	out.put(m.getId(),m);

		return out;
	}
	
	public void extractMessageIdToHeader(Exchange exchange) {
		exchange.setOut(exchange.getIn());
		exchange.getOut().setHeader("messageId", exchange.getOut().getBody(Message.class).getId());
		log.trace("extractMessageIdToHeader "+exchange.getOut().getBody(Message.class).getId());
	}
}
