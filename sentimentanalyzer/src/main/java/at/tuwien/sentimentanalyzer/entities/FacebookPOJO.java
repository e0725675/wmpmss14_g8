package at.tuwien.sentimentanalyzer.entities;

import java.util.ArrayList;
import java.util.List;


import at.tuwien.sentimentanalyzer.entities.reddit.Data;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

public class FacebookPOJO {
	  public class Data {
		  private List<Child> children;

		public List<Child> getChildren() {
			return children;
		}

		public void setChildren(List<Child> children) {
			this.children = children;
		}

	  }
	  
	  public class Child {
			 @SerializedName("from")
			 private From _from;

			 @SerializedName("message")
			 String _message;
			  
			 @SerializedName("created_time")
			 String _created_time;
			  
			public From get_from() {
				return _from;
			}

			public void set_from(From _from) {
				this._from = _from;
			}

			public String get_message() {
				return _message;
			}

			public void set_message(String _message) {
				this._message = _message;
			}

			public String get_created_time() {
				return _created_time;
			}

			public void set_created_time(String _created_time) {
				this._created_time = _created_time;
			}
	  }
	  
	  public class From {
		  private String _id;
		  private String _name;
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String get_name() {
			return _name;
		}
		public void set_name(String _name) {
			this._name = _name;
		}
	  }
	  
	  @SerializedName("data")
	  private Data _data;

	public Data getData() {
		return _data;
	}

	public void setData(Data _data) {
		this._data = _data;
	}
	  

	
//		public List<Message> getMessage(Gson gson){
//			
//			//Gson gson = new Gson();
//			List<Message> out = new ArrayList<Message>();
//			JsonArray data = gson.fromJson (toString(), JsonElement.class).getAsJsonArray(); // Convert the Json string to JsonArray
//			JsonObject post; //= data.get(0).getAsJsonObject(); //Get the first element of array and convert it to Json object
//			JsonObject from;
//			Message msg;
//			
//			for (int i = 0; i < data.size(); i++) {
//				msg = new Message();
//				post = data.get(i).getAsJsonObject();
//				from = post.get("from").getAsJsonObject();
//				
//				msg.setMessage(post.get("message").getAsString());
//				msg.setAuthor(from.get("id").getAsString());
//				msg.setTimePosted(post.get("created_time").getAsString());
//				
//				out.add(msg);
//			}
//			return out;
//		}
		
		
		
//	 public class User {
//		   2     public enum Gender { MALE, FEMALE };
//		   3 
//		   4     public static class Name {
//		   5       private String _first, _last;
//		   6 
//		   7       public String getFirst() { return _first; }
//		   8       public String getLast() { return _last; }
//		   9 
//		  10       public void setFirst(String s) { _first = s; }
//		  11       public void setLast(String s) { _last = s; }
//		  12     }
//		  13 
//		  14     private Gender _gender;
//		  15     private Name _name;
//		  16     private boolean _isVerified;
//		  17     private byte[] _userImage;
//		  18 
//		  19     public Name getName() { return _name; }
//		  20     public boolean isVerified() { return _isVerified; }
//		  21     public Gender getGender() { return _gender; }
//		  22     public byte[] getUserImage() { return _userImage; }
//		  23 
//		  24     public void setName(Name n) { _name = n; }
//		  25     public void setVerified(boolean b) { _isVerified = b; }
//		  26     public void setGender(Gender g) { _gender = g; }
//		  27     public void setUserImage(byte[] b) { _userImage = b; }
//		  28 }
}
