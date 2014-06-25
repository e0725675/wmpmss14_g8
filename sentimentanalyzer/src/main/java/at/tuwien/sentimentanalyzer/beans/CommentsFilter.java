package at.tuwien.sentimentanalyzer.beans;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import at.tuwien.sentimentanalyzer.entities.Message;

/*
 * Author: Pia
 * Customized filtering of comments (f.e. cut out links, comments from client)
 */

public class CommentsFilter {

	public boolean filterURL (Message msg){
	//public HashMap<String, List<String>> filterURL (HashMap<String, List<String>> csv){
		boolean url_exists;
		
		//for(Map.Entry<String, List<String>> entry: csv.entrySet())
		//{
			//separate input by spaces (URLs don't have spaces)
	        //String [] words = entry.getValue().get(1).split("\\s");
		    String [] words = msg.getMessage().split("\\s");
	        url_exists = false;
	        
	        //attempt to convert each item into an URL
	        for( String item : words ) try {
	            @SuppressWarnings("unused")
				URL url = new URL(item);
	            url_exists = true;
	                
	        } catch (MalformedURLException e) {
	        }
			
	        //if (url_exists)
				//csv.remove(entry.getKey());
	    //}
		return url_exists;	
	}
	
	public boolean filterClient (Message msg){
//	public HashMap<String, List<String>> filterClient (HashMap<String, List<String>> csv){
//		for(Map.Entry<String, List<String>> entry: csv.entrySet())
//		{
//			if ( entry.getValue().get(0).equals("") ) //TODO: add clientname
//				csv.remove(entry.getKey());
//		}
		Properties properties = new Properties();
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream("client.properties"));
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String client = properties.getProperty("reddit.client");
		
		boolean isNotClient = true;
		
		//if (msg.getSource())
		if (msg.getAuthor().equals(client))
			isNotClient = false;
		
		return isNotClient;	
	}
}
