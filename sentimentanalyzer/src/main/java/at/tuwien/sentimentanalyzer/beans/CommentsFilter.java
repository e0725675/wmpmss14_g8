package at.tuwien.sentimentanalyzer.beans;

import java.net.MalformedURLException;
import java.net.URL;
import at.tuwien.sentimentanalyzer.PropertiesLoader;
import at.tuwien.sentimentanalyzer.entities.Message;

/*
 * Author: Pia
 * Customized filtering of comments (f.e. cut out links, comments from client)
 */

public class CommentsFilter {

	public boolean filterURL (Message msg){
		boolean url_exists;
		
			//separate input by spaces (URLs don't have spaces)
		    String [] words = msg.getMessage().split("\\s");
	        url_exists = false;
	        
	        //attempt to convert each item into an URL
	        for( String item : words ) try {
	            @SuppressWarnings("unused")
				URL url = new URL(item);
	            url_exists = true;
	                
	        } catch (MalformedURLException e) {
	        }
	        
		return url_exists;	
	}
	
	public boolean filterClient (Message msg){
		boolean isNotClient = true;
		
		if (msg.getSource().equals("facebook")) {
			if (!PropertiesLoader.configProperties.getString("facebook.client").equals("NULL")) {
				if(msg.getAuthor().equals(PropertiesLoader.configProperties.getString("facebook.client")))
					isNotClient = false;
			}
		} else if (msg.getSource().equals("twitter")) {
			if (!PropertiesLoader.configProperties.getString("twitter.client").equals("NULL")) {
				if (msg.getAuthor().equals(PropertiesLoader.configProperties.getString("twitter.client")))
					isNotClient = false;
			}
		} else if (msg.getSource().equals("reddit")) {
			if (!PropertiesLoader.configProperties.getString("reddit.client").equals("NULL")) {
				if (msg.getAuthor().equals(PropertiesLoader.configProperties.getString("reddit.client")))
					isNotClient = false;
			}
		}
		
		return isNotClient;	
	}
}
