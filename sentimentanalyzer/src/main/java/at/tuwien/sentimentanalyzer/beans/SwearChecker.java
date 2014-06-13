package at.tuwien.sentimentanalyzer.beans;
/*
 * Author: matt
 * Customized sorting of messages (i.i. checks message, flags swear words in message, flags account if swearing exceeds daily limit or list.)
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;

import at.tuwien.sentimentanalyzer.entities.Message;

public class SwearChecker {
	private static Logger log = Logger.getLogger(SwearChecker.class);
	private ArrayList<String> cussWords;
	
	private Connection con;
	
	public SwearChecker(DataSource dataSource) throws IOException, SQLException {
		
		this.cussWords = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("mock_swearwordlist.txt"));
		try {
			String line = br.readLine();
			while (line != null) {
				this.cussWords.add(line.toUpperCase());
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		
		
		con = dataSource.getConnection();
		PreparedStatement stmt = con.prepareStatement("CREATE TABLE Users (id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), username VARCHAR(200) NOT NULL, source VARCHAR(200) NOT NULL, timeposted DATE NOT NULL, hasswears BOOLEAN NOT NULL,PRIMARY KEY(ID))");
		stmt.execute();
	}
	
	public void logSwearChecker(Message message) throws SQLException {
		log.info(message);
		String m = message.getMessage();
		m = m.replaceAll("[^\\w\\s]", "");
		String[] words = m.split("\\s");
		boolean containsCussword = false;
		for (String word : words) {
			if (this.cussWords.contains(word.toUpperCase())) {
				containsCussword = true;
			}
		}
		if (containsCussword) {
			PreparedStatement stmt = this.con.prepareStatement("INSERT INTO Users (username, source, timeposted, hasswears) VALUES (?,?,?, ?)");
			stmt.setString(1, message.getAuthor());
			stmt.setString(2, message.getSource());
			java.sql.Date tp = new java.sql.Date(message.getTimePosted().getTime());
			stmt.setDate(3, tp);
			stmt.setBoolean(4, containsCussword);
			stmt.executeUpdate();
		}
	}
	public boolean isUserBlocked(String source, String username) throws SQLException {
		java.util.Date date = new java.util.Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -2);
		date = cal.getTime();
		PreparedStatement stmt = this.con.prepareStatement("SELECT count(*) FROM user WHERE username = ? AND source = ? AND timeposted > ? AND hasswears = ?");
		stmt.setString(1, username);
		stmt.setString(2, source);
		stmt.setDate(3, new java.sql.Date(date.getTime()));
		stmt.setBoolean(4, true);
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		int count1 = rs.getInt(1);
		log.info("count1 "+count1);
		if (count1 > 10) {
			
			return true;
		}
		PreparedStatement stmt2 = this.con.prepareStatement("SELECT count(*) FROM (SELECT * FROM Users WHERE username = ? AND source = ? ORDER BY timeposted DESC LIMIT 10) WHERE hasswears = TRUE");
		stmt2.setString(1, username);
		stmt2.setString(2, source);
		stmt2.execute();
		ResultSet rs2 = stmt2.getResultSet();
		int count2 = rs2.getInt(1);
		log.info("count2 "+count2);
		if (count2 > 5) {
			return true;
		}
		
		return false;
	}
	
//	public void doSomething() {
		
//	}
//	
//	private static Message  msg = Message.getMessage();
//	
//	public void logSwearChecker ( Message swearsIn){
//		log.info("Unfiltered message -> " +swearsIn);
//	}
	
}