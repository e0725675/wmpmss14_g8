package at.tuwien.sentimentanalyzer.sample;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import at.tuwien.sentimentanalyzer.beans.MessageMocker;
import at.tuwien.sentimentanalyzer.beans.SwearChecker;
import at.tuwien.sentimentanalyzer.entities.Message;
import at.tuwien.sentimentanalyzer.entities.Message.Source;

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
			Message m = new Message();
			m.setAuthor("MessageMocker");
			Source s = new Source("MessageMocker");
			m.setSource(s);
			boolean blocked = swearChecker.isUserBlocked(m);
			out+=user+" blocked: "+blocked+"\n";
		}
		return out;
	}
}
