package at.tuwien.sentimentanalyzer.sample;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import at.tuwien.sentimentanalyzer.beans.MessageMocker;
import at.tuwien.sentimentanalyzer.beans.SwearChecker;

public class ExpCheckUsersBlocked {
	private static Logger log = Logger.getLogger(ExpCheckUsersBlocked.class);
	List<String> users;
	public ExpCheckUsersBlocked() throws IOException {
		log.info("Creating ExpCheckUsersBlocked");
		users = MessageMocker.fileToList(new File("mock_userlist.txt"));
		log.info("Creating ExpCheckUsersBlocked "+users.size());
	}
	
	@Autowired
	private SwearChecker swearChecker;
	
	public String checkAllUsersBlocked() throws SQLException {
		log.info("checkAllUsersBlocked:");
		if (swearChecker == null) {
			throw new RuntimeException("SwearChecker bean was not autowired and is null");
		}
		String out = "";
		for (String user : users) {
			boolean blocked = swearChecker.checkUserBlocked("MessageMocker", user);
			out+=user+" blocked: "+blocked+"\n";
		}
		return out;
	}
}
