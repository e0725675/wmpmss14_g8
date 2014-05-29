package at.tuwien.sentimentanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {
	private static Server jettyServer = null;
	private static Logger log = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;

		try {
			initJetty();
		} catch (Exception e1) {
			log.error("Error on initializing Jetty", e1);
			System.exit(1);
		}
		
		
		System.out.println("Type \"exit\" to exit.");
		while (true) {
			try {
				line = br.readLine();
			} catch (IOException e) {
				log.error("IOError on readLine",e);
				System.exit(1);
			}
			if (line.equalsIgnoreCase("exit")) {
				break;
			}
		}
		try {
			jettyServer.stop();
		} catch (Exception e) {
			log.error("Error on stopping Jetty",e);
		}
		
	}
	
	private static void initJetty() throws Exception{
		
		log.info("Starting server...");
		jettyServer = new Server(8889);

		WebAppContext context = new WebAppContext();
		context.setDescriptor("WEB-INF/web.xml");
		context.setResourceBase("src/webapp/");
		context.setContextPath("");
		context.setParentLoaderPriority(true);
		log.info(context.toString());
		jettyServer.setHandler(context);
		
		jettyServer.start();
	}
}
