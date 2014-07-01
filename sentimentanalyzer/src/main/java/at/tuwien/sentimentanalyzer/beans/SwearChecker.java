package at.tuwien.sentimentanalyzer.beans;
/*
 * Author: matt
 * Customized sorting of messages (i.i. checks message, flags swear words in message, flags account if swearing exceeds daily limit or list.)
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.beans.ReportGenerator.ReportGeneratorException;
import at.tuwien.sentimentanalyzer.beans.ReportGenerator.SwearwordReportContent;
import at.tuwien.sentimentanalyzer.beans.ReportGenerator.SwearwordReportContent.SwearInformation;
import at.tuwien.sentimentanalyzer.entities.AggregatedMessages.Author;
import at.tuwien.sentimentanalyzer.entities.Message;
import at.tuwien.sentimentanalyzer.entities.Message.Source;

public class SwearChecker {
	private static final int MAXCONSECUTIVESWEARCOUNT = 5;
	private static final int MAXTOTAlSWEARCOUNT = 10;
	private static final int PASTDAYS = 2;
	
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
		PreparedStatement stmt = con.prepareStatement("CREATE TABLE Users ("
				+ "id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
				+ "username VARCHAR(200) NOT NULL, "
				+ "source VARCHAR(200) NOT NULL, "
				+ "timeposted DATE NOT NULL, "
				+ "hasswears BOOLEAN NOT NULL,"
				+ "PRIMARY KEY(ID))");
		stmt.execute();
		stmt.close();
	}
	
	public void logSwearChecker(Exchange exchange) throws SQLException {
		log.debug("logSwearChecker start");
		exchange.setOut(exchange.getIn());
		Message message = (Message) exchange.getIn().getBody();
		log.debug("Checking for swear word in the following message: " +message);
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
				log.debug("Attention! We have detected a swear word: " +word );
			}
		}
		if (containsCussword) {
//			When match is found, entry is stored in table 'Users'.
//			Added Column item 'containsCussword is then set to 'TRUE'.			
			PreparedStatement stmt = this.con.prepareStatement("INSERT INTO Users (username, source, timeposted, hasswears) VALUES (?,?,?,?)");
			stmt.setString(1, message.getAuthor().toString());
			stmt.setString(2, message.getSource().toString());
			java.sql.Date tp = new java.sql.Date(message.getTimePosted().getTime());
			stmt.setDate(3, tp);
			stmt.setBoolean(4, containsCussword);
			stmt.executeUpdate();
			stmt.getResultSet();
			stmt.close();
			//log.debug("We have " +rs0);
		}
		
//		String source = "MessageMocker";
//		String testUser = "paleaccepting";
//		
//		if(message.getAuthor().equals(testUser) && message.getSource().equals(source)){
//			if(isUserBlocked(message.getSource().toString(), message.getAuthor().toString())){
//				log.debug(testUser+ "THE FUCKER SWORE!");
//			}
//		}
		log.trace("logSwearChecker end");
	}

	public boolean isUserBlocked(Exchange exchange) throws SQLException {
		exchange.setOut(exchange.getIn());
		if (!(exchange.getIn().getBody() instanceof Message)) {
			throw new RuntimeException("Input exchange body is not a Message");
		}
		Message m = (Message) exchange.getIn().getBody();
		String user = m.getAuthor();
		String source = m.getSource().toString();
		return this.checkUserBlocked(source, user);
	}
	public boolean checkUserBlocked(String source, String username) throws SQLException {
		if (source == null) throw new RuntimeException("Input source is null");
		if (source.isEmpty()) throw new RuntimeException("Input source is empty");
		if (username == null) throw new RuntimeException("Input username is null");
		if (username.isEmpty()) throw new RuntimeException("Input username is empty");
		
		log.debug("isUserBlocked: " +username+" "+source);
//		Checks to see if the user has 10 total swears in db using variable 'ResultSet rs'.
//		if so, logs it.
//		Then checks to see if the user has 5 consecutive swears in db using variable 'ResultSet rs2'.

		
		java.util.Date date = new java.util.Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -PASTDAYS);
		date = cal.getTime();
		// Get the amount of swearposts from the user from the past 2 days
		PreparedStatement stmt = this.con.prepareStatement("SELECT count(*) FROM Users WHERE "
				+ "username = ? AND "
				+ "source = ? AND "
				+ "timeposted > ? AND "
				+ "hasswears = TRUE",
				ResultSet.TYPE_SCROLL_INSENSITIVE, 
				ResultSet.CONCUR_READ_ONLY);
		stmt.setString(1, username);
		stmt.setString(2, source);
		stmt.setDate(3, new java.sql.Date(date.getTime()));
		stmt.execute();
		ResultSet rs = stmt.executeQuery();
		rs.next();
		int count1 = rs.getInt(1);
		// if user has posted more than 10 times during the past 2 days he is blocked
		if (count1 > MAXTOTAlSWEARCOUNT) {
			log.trace("Result set for "+MAXTOTAlSWEARCOUNT+" entries with swears: "+rs);
			log.debug("User "+username+" from "+source+" is blocked");
			return true;
		}
		rs.close();
		stmt.close();
		
		// Check whether the user has had 5 consecutive swearposts in the past 2 days
		// get all posts from the past 2 days
		PreparedStatement stmt2 = this.con.prepareStatement("SELECT hasswears FROM Users WHERE "
				+ "username = ? AND "
				+ "source = ? AND "
				+ "timeposted > ? "
				+ "ORDER BY timeposted DESC",
				ResultSet.TYPE_SCROLL_INSENSITIVE, 
				ResultSet.CONCUR_READ_ONLY);
		stmt2.setString(1, username);
		stmt2.setString(2, source);
		stmt2.setDate(3, new java.sql.Date(date.getTime()));
		stmt2.execute();
		ResultSet rs2 = stmt2.executeQuery();
		int consecutivecounter = 0;
		while(rs2.next()) {
			boolean hasswears = rs2.getBoolean(1);
			if (hasswears) {
				consecutivecounter++;
			} else {
				consecutivecounter = 0;
			}
			if (consecutivecounter>MAXCONSECUTIVESWEARCOUNT) {
				log.debug("User "+username+" from "+source+" has more than "+MAXCONSECUTIVESWEARCOUNT+
						" consecutive swear posts and is blocked");
				return true;
			}
		}
		
		rs2.close();
		stmt2.close();
		log.debug("User "+username+" from "+source+" is not blocked");
		return false;
		
	}
	
	public SwearwordReportContent getSwearReport(@Header("start") String from, @Header("end") String to) throws SQLException {
		if (from == null) throw new RuntimeException("start Date is null");
		if (to == null) throw new RuntimeException("end Date is null");
		
		DateFormat inDfFull = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		DateFormat inDfSimple = new SimpleDateFormat("yyyy.MM.dd");
		Date start;
		try {
			start = inDfFull.parse(from);
		} catch (ParseException e) {
			try {
				start = inDfSimple.parse(from);
			} catch(ParseException e1) {
				throw new ReportGeneratorException("Parameter 'start' has an invalid format: "+from);
			}
		}
		Date end;
		try {
			end = inDfFull.parse(to);
		} catch (ParseException e) {
			try {
				end = inDfSimple.parse(to);
			} catch(ParseException e1) {
				throw new ReportGeneratorException("Parameter 'end' has an invalid format: "+to);
			}
		}
		
		
		log.debug("getSwearReport start");
		SwearwordReportContent out = new SwearwordReportContent();
		
//		java.util.Date date = new java.util.Date();
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		cal.add(Calendar.DATE, -PASTDAYS);
//		date = cal.getTime();
		
		HashMap<Author, SwearInformation> userInformation = new HashMap<Author, SwearInformation>();

		HashMap<Author,Integer> totalSwearCounts = new HashMap<Author,Integer>();
		HashMap<Author,Integer> consecutiveSwearCounts = new HashMap<Author,Integer>();
		HashMap<Author,Boolean> isBlocked = new HashMap<Author,Boolean>();
		
		
		PreparedStatement stmt2 = this.con.prepareStatement("SELECT username,source,hasswears FROM Users WHERE "
				+ "timeposted > ? AND "
				+ "timeposted < ? "
				+ "ORDER BY timeposted DESC",
				ResultSet.TYPE_SCROLL_INSENSITIVE, 
				ResultSet.CONCUR_READ_ONLY);
		stmt2.setDate(1, new java.sql.Date(start.getTime()));
		stmt2.setDate(2, new java.sql.Date(end.getTime()));
		stmt2.execute();
		
		ResultSet rs2 = stmt2.getResultSet();
		
		while(rs2.next()) {
			String user = rs2.getString(1);
			String source = rs2.getString(2);
			boolean hasswears = rs2.getBoolean(3);
			Author author = new Author(user, new Source(source));
			
			if (hasswears) {
				Boolean blocked = isBlocked.get(author);
				if ( blocked== null || blocked == false ) {
					// consecutive
					Integer consec = consecutiveSwearCounts.get(author);
					if (consec == null) {
						consec = 0;
					}
					consec++;
					if (consec >MAXCONSECUTIVESWEARCOUNT) {
						log.debug("User "+source+":"+user+" has more than "+MAXCONSECUTIVESWEARCOUNT+" consecutive swears and is blocked.");
						isBlocked.put(author, true);
						continue;
					} else {
						consecutiveSwearCounts.put(author, consec);
					}
					
					
					// total
					
					Integer total = totalSwearCounts.get(author);
					if (total == null) {
						total = 0;
					}
					total++;
					if (total > MAXTOTAlSWEARCOUNT) {

						log.debug("User "+source+":"+user+" has more than "+MAXTOTAlSWEARCOUNT+" total swears and is blocked.");
						isBlocked.put(author, true);
						continue;
					} else {
						totalSwearCounts.put(author, total);
					}
				}
			} else {
				consecutiveSwearCounts.put(author, 0);
			}
		}
		rs2.close();
		stmt2.close();
		Set<Author> authors = isBlocked.keySet();
		
		log.debug("There are "+authors.size()+ " blocked users");
		
		for (Author author : authors) {
			boolean blocked = isBlocked.get(author);
			if (blocked) {
				SwearInformation sw = new SwearInformation();
				PreparedStatement stmt = this.con.prepareStatement("SELECT count(*) FROM Users WHERE "
						+ "hasswears = TRUE AND "
						+ "username = ? AND "
						+ "source = ? AND "
						+ "timeposted > ? AND"
						+ "timeposted < ?",
						ResultSet.TYPE_SCROLL_INSENSITIVE, 
						ResultSet.CONCUR_READ_ONLY);
				stmt.setString(1, author.getName());
				stmt.setString(2, author.getSource().toString());
				stmt.setDate(3, new java.sql.Date(start.getTime()));
				stmt.setDate(4, new java.sql.Date(end.getTime()));
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				rs.next();
				int count = rs.getInt(1);
				log.debug("User "+author.getSource()+":"+author.getName()+" has "+count+" total swears.");
				sw.recordedOffences = count;
				userInformation.put(author, sw);
				rs.close();
				stmt.close();
			}
		}
		out.userInformation = userInformation;
		log.debug("getSwearReport end");
		return out;
	}
}