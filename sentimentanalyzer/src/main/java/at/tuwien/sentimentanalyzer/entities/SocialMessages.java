package at.tuwien.sentimentanalyzer.entities;

import static javax.persistence.GenerationType.AUTO;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Map messages to the database
 * @author LG
 *
 */
@Entity
@Table
@Deprecated
public class SocialMessages {
	@Id
	@GeneratedValue(strategy = AUTO)
	@Column
	private long id;
	
	@Column(length = 300, nullable = true)
	private String message;
	
	@Column(length = 50, nullable = true)
	private String author;
	
	@Column(length = 200, nullable = true)
	private Date timePosted;
	
	@Column(length = 20, nullable = true)
	private String source;
	
	public SocialMessages() {

	}
	public SocialMessages(String message, String author, Date timePosted, String source) {
		this.message = message;
		this.author = author;
		this.timePosted = timePosted;
		this.source = source;
		
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getauthor() {
		return author;
	}

	public void setauthor(String author) {
		this.author = author;
	}
	
	public Date getTimePosted() {
		return timePosted;
	}

	public void setTimePosted(Date timePosted) {
		this.timePosted = timePosted;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	
}