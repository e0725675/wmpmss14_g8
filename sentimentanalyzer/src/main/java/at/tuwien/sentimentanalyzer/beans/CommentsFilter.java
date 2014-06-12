package at.tuwien.sentimentanalyzer.beans;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Author: Pia
 * Customized filtering of comments (f.e. cut out links, comments from client)
 */

public class CommentsFilter {

	public HashMap<String, List<String>> filterURL (HashMap<String, List<String>> csv){
		boolean url_exists;
		
		
		for(Map.Entry<String, List<String>> entry: csv.entrySet())
		{
			//separate input by spaces (URLs don't have spaces)
	        String [] words = entry.getValue().get(1).split("\\s");
	        url_exists = false;
	        
	        //attempt to convert each item into an URL
	        for( String item : words ) try {
	            @SuppressWarnings("unused")
				URL url = new URL(item);
	            url_exists = true;
	                
	        } catch (MalformedURLException e) {
	        }
			
	        if (url_exists)
				csv.remove(entry.getKey());
		}
		return csv;	
	}
	
	public HashMap<String, List<String>> filterClient (HashMap<String, List<String>> csv){
		for(Map.Entry<String, List<String>> entry: csv.entrySet())
		{
			if ( entry.getValue().get(0).equals("") ) //TODO: add clientname
				csv.remove(entry.getKey());
		}
		
		return csv;	
	}
}
