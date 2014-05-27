package at.tuwien.sentimentanalyzer.beans;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/*
 * Author: Pia Liszt
 * pia.liszt@gmail.com
 * Customized filtering of comments (f.e. cut out links, comments from client)
 */

public class CommentsFilter {

	public List<String> filterURL (List<String> textList){
		boolean url_exists;
		
		for (String text: textList)
		{
			//separate input by spaces (URLs don't have spaces)
	        String [] words = text.split("\\s");
	        url_exists = false;
	        
	        //attempt to convert each item into an URL
	        for( String item : words ) try {
	            URL url = new URL(item);
	            url_exists = true;
	                
	        } catch (MalformedURLException e) {
	        }
			
	        if (url_exists)
				textList.remove(text);
		}
		return textList;	
	}
	
	public List<String> filterClient (List<String> textList){
		//TODO
		return textList;	
	}
	
}
