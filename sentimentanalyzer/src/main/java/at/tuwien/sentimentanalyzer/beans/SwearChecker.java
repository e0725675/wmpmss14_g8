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
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.entities.Message;

public class SwearChecker {
	private static Logger log = Logger.getLogger(SwearChecker.class);
//	Create local logging element 'log'
	private ArrayList<String> cussWords;
//	Local variable of stored cusswords
	private Connection con;
//	local datasource connection variable
	public SwearChecker(DataSource dataSource) throws IOException, SQLException {
		log.info("Creating SwearChecker");
//		Create public method for putting cusswords into database
		this.cussWords = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("mock_swearwordlist.txt"));
//		Create file linereader variable and iterate through each line storing it in 'con'
//		with the preparedStatement
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
		stmt.close();
	}
	
	public void logSwearChecker(Message message) throws SQLException {
		log.info("Checking for swear word in the following message: " +message);
//		If no message logs error as SQL Exception
		String m = message.getMessage();
//		Variable to step through the message removing
		m = m.replaceAll("[^\\w\\s]", "");
//		non-word, non-whitespace see http://www.regexr.com
		String[] words = m.split("\\s");
		boolean containsCussword = false;
//		Boolean variable tests if word found via m.split is found in the database imported
//		'mock_swearwordlist.txt' stored in 'this.cussWords'.
		
		for (String word : words) {
			if (this.cussWords.contains(word.toUpperCase())) {
				containsCussword = true;
				log.info("Attention! We have detected a swear word: " +word );
			}
		}
		if (containsCussword) {
//			When match is found, entry is stored in table 'Users'.
//			Added Column item 'containsCussword is then set to 'TRUE'.
			log.info("Swear entry in DB attempted for: "+message.getAuthor());
			PreparedStatement stmt = this.con.prepareStatement("INSERT INTO Users (username, source, timeposted, hasswears) VALUES (?,?,?, ?)");
			stmt.setString(1, message.getAuthor());
			stmt.setString(2, message.getSource());
			java.sql.Date tp = new java.sql.Date(message.getTimePosted().getTime());
			stmt.setDate(3, tp);
			stmt.setBoolean(4, containsCussword);
			stmt.executeUpdate();
			stmt.getResultSet();
			stmt.close();
		}
	}
//	Nothing is being logged from here below. Why?
// Clemente: because this method is never called from anywhere
//                   |	
//	                 |    
//					 |
//	                 |
//					 V
	public boolean isUserBlocked(String source, String username) throws SQLException {
		log.info("The following user swore: " +username);
//		Checks to see if the user has 10 total swears in db using variable 'ResultSet rs'.
//		if so, logs it.
//		Then checks to see if the user has 5 consecutive swears in db using variable 'ResultSet rs2'.

		java.util.Date date = new java.util.Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -2);
		date = cal.getTime();
		PreparedStatement stmt = this.con.prepareStatement("SELECT count(*) FROM Users WHERE username = ? AND source = ? AND timeposted > ? AND hasswears = ?");
		stmt.setString(1, username);
		stmt.setString(2, source);
		stmt.setDate(3, new java.sql.Date(date.getTime()));
		stmt.setBoolean(4, true);
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		rs.next();
		int count1 = rs.getInt(1);
//		What is rs.getInt(1)? See it in the log.txt
		
		if (count1 > 10) {
			log.info("Result set for 10 entries with swears: "+rs);
			return true;
		}
		rs.close();
		stmt.close();
		
		PreparedStatement stmt2 = this.con.prepareStatement("SELECT count(*) FROM (SELECT * FROM Users WHERE username = ? AND source = ? AND hasswears = TRUE ORDER BY timeposted DESC FETCH FIRST 10 ROWS ONLY) as X");
		stmt2.setString(1, username);
		stmt2.setString(2, source);
		stmt2.execute();
		ResultSet rs2 = stmt2.getResultSet();
		rs2.next();
		
		int count2 = rs2.getInt(1);
//		What is rs2.getInt(1)? See it in the log.txt
		rs2.close();
		stmt2.close();
		if (count2 > 5) {
			log.info("Result set for 5 consecutive swear detection: " +count2);
			return true;
		}
		
		else {
			PreparedStatement stmt3 = this.con.prepareStatement(
					"SELECT username FROM Users WHERE hasswears = TRUE",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			//stmt3.setString(1, username);
			//stmt3.setString(2, source);
			ResultSet rs3 = stmt3.executeQuery();

			List<String> blockedUsers = new ArrayList<String>();
			rs3.next();
			while (!rs3.isAfterLast()) {
				blockedUsers.add(rs3.getString(1));
				rs3.next();
			}
			rs3.close();
			stmt3.close();
			log.info("These users currently have swear entries: "+blockedUsers);
			return false;
		}
	}
	
}