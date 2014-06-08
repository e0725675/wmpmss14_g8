package at.tuwien.sentimentanalyzer.entities.reddit;

import java.util.Date;
import java.util.List;

public class Data {
	private String after;
   	private String before;
   	private List<Children> children;
   	private String modhash;
   	private String created;
//   	private String selftext_html;
	private String author;
   	
 	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	
 	public String getAfter(){
		return this.after;
	}
//	public String getSelftext_html() {
//		return selftext_html;
//	}
//	public void setSelftext_html(String selftext_html) {
//		this.selftext_html = selftext_html;
//	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
//	public Date getCreated_utc() {
//		return created_utc;
//	}
//	public void setCreated_utc(Date created_utc) {
//		this.created_utc = created_utc;
//	}
	public void setAfter(String after){
		this.after = after;
	}
 	public String getBefore(){
		return this.before;
	}
	public void setBefore(String before){
		this.before = before;
	}
 	public List<Children> getChildren(){
		return this.children;
	}
	public void setChildren(List<Children> children){
		this.children = children;
	}
 	public String getModhash(){
		return this.modhash;
	}
	public void setModhash(String modhash){
		this.modhash = modhash;
	}
}
