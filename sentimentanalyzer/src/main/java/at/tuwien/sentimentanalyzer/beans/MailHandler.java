package at.tuwien.sentimentanalyzer.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.log4j.Logger;

public class MailHandler {
	private static Logger log = Logger.getLogger(MailHandler.class);
	
	public class InvalidMailFormatException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -162122605832294487L;
		public InvalidMailFormatException(String msg) {
			super(msg);
		}
	}
	public MailHandler() {
	}
	public Set<String> dailyMail = new HashSet<String>();
	public Set<String> weeklyMail = new HashSet<String>();
	
	/**
	 * mail address must be in Header "mail"
	 * @param exchange
	 * @throws InvalidMailFormatException 
	 */
	public void addRecipientToDailyList(Exchange exchange, @Header("mail") String mail) throws InvalidMailFormatException {
		exchange.setOut(exchange.getIn());
		if (mail == null) {
			throw new InvalidMailFormatException("Email is null");
		}
		if (!Pattern.matches( "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", mail )) {
			throw new InvalidMailFormatException("Invalid email "+mail);
		}
		log.info("Adding "+mail+" to daily recipient list.");
		this.dailyMail.add(mail);
	}
	/**
	 * @param exchange
	 * @throws InvalidMailFormatException 
	 */
	public void removeRecipientFromDailyList(Exchange exchange, @Header("mail") String mail) throws InvalidMailFormatException {
		exchange.setOut(exchange.getIn());
		log.info("Removed "+mail+" from daily recipient list.");
		this.dailyMail.remove(mail);
	}
	/**
	 * mail address must be in Header "mail"
	 * @param exchange
	 * @throws InvalidMailFormatException 
	 */
	public void addRecipientToWeeklyList(Exchange exchange, @Header("mail") String mail) throws InvalidMailFormatException {
		exchange.setOut(exchange.getIn());
		if (mail == null) {
			throw new InvalidMailFormatException("Email is null");
		}
		if (!Pattern.matches( "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", mail )) {
			throw new InvalidMailFormatException("Invalid email "+mail);
		}
		log.info("Adding "+mail+" to weekly recipient list.");
		this.weeklyMail.add(mail);
	}
	/**
	 * @param exchange
	 * @throws InvalidMailFormatException 
	 */
	public void removeRecipientFromWeeklyList(Exchange exchange, @Header("mail") String mail) throws InvalidMailFormatException {
		exchange.setOut(exchange.getIn());
		log.info("Removed "+mail+" from weekly recipient list.");
		this.weeklyMail.remove(mail);
	}
	/**
	 * Adds body as mail file attachment. The body must be the filepath as String
	 * @param exchange
	 * @throws FileNotFoundException 
	 */
	public void addAttachment(Exchange exchange, @Header("reportfilename") String reportfilename) throws FileNotFoundException {
		exchange.setOut(exchange.getIn());
		if(reportfilename == null) {
			throw new RuntimeException("reportfilename is null");
		}	
		log.info("addAttachment: filename:"+reportfilename);
		File f = new File(reportfilename);
		if(!f.exists()) {
			throw new FileNotFoundException("The file "+reportfilename+" does not exist");
		}
		exchange.getOut().addAttachment(f.getName(), new DataHandler(new FileDataSource(f)));
	}
	/**
	 * recipient list uri will be in the header "touri"
	 * @param exchange
	 */
	public void recipientListDaily(Exchange exchange) {
		exchange.setOut(exchange.getIn());
		String recipients = "";//"&BCC=";
		if (this.dailyMail.isEmpty()) {
			recipients = "";
		}
		boolean first = true;
		for (String mail : this.dailyMail) {
			if (!first) {
				recipients+=",";
			} else {
				first = false;
			}
			recipients+=mail;
		}
		String toUri;
		toUri = recipients;
		//toUri = "smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv"+recipients;
		log.info("touri string: "+toUri);
		exchange.getOut().setHeader("touri", toUri);
	}
	/**
	 * recipient list uri will be in the header "touri"
	 * @param exchange
	 */
	public void recipientListWeekly(Exchange exchange) {
		exchange.setOut(exchange.getIn());
		String recipients = "&BCC=";
		if (this.dailyMail.isEmpty()) {
			recipients = "";
		}
		boolean first = true;
		for (String mail : this.weeklyMail) {
			if (!first) {
				recipients+=",";
			} else {
				first = false;
			}
			recipients+=mail;
		}
		//String toUri = "smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv"+recipients;
		exchange.getOut().setHeader("touri", recipients);
	}
}
