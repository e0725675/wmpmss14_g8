package at.tuwien.sentimentanalyzer.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.tuwien.sentimentanalyzer.entities.FacebookPOJO;
import at.tuwien.sentimentanalyzer.entities.FacebookPOJO.Child;
import at.tuwien.sentimentanalyzer.entities.FacebookPOJO.Data;
import at.tuwien.sentimentanalyzer.entities.Message;

public class FacebookConverter {
	
	public List<Message> getMessage(FacebookPOJO fb){
	
	//Gson gson = new Gson();
	List<Message> out = new ArrayList<Message>();
	List<Child> data = fb.getData().getChildren();
	Message msg;
	
	for (int i = 0; i < data.size(); i++) {
		msg = new Message();
		msg.setMessage(data.get(i).get_message());
		msg.setAuthor(data.get(i).get_from().get_id());
		
		//String fb_date = data.get(i).get_created_time();
		//Date date;
		//, new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" )
		//msg.setTimePosted(date);
		
		out.add(msg);
	}
	return out;
}

}
