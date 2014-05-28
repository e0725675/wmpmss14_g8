package at.tuwien.sentimentanalyzer.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.connectors.Message;

public class CSVHandler {
	public static Logger log = Logger.getLogger(CSVHandler.class);
	
	public Map<String, Object> doHandleCsvData(Message message)
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
	}
}
