package at.tuwien.sentimentanalyzer.beans;

import at.tuwien.sentimentanalyzer.entities.Message;
import at.tuwien.sentimentanalyzer.entities.SocialMessages;

/**
 * @author LG
 * Generates the social bean to use with the database
 */
public class SocialBean {


    public SocialMessages generateSocial(Message message) {
    	
    	SocialMessages answer = new SocialMessages();
    	answer.setauthor(message.getAuthor());
    	answer.setMessage(message.getMessage());
    	answer.setSource(message.getSource());
    	answer.setTimePosted(message.getTimePosted());
    	return answer;
        
    }

}
