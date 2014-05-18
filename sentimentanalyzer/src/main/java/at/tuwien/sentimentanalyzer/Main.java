package at.tuwien.sentimentanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	private static ClassPathXmlApplicationContext springContext = null;
	private static Server jettyServer = null;
	private static Logger log = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		
		initSpring();
		
		try {
			initJetty();
		} catch (Exception e1) {
			log.error(e1);
			System.exit(1);
		}
		
		
		System.out.println("Type \"exit\" to exit.");
		while (true) {
			try {
				line = br.readLine();
			} catch (IOException e) {
				log.error(e);
				System.exit(1);
			}
			if (line.equalsIgnoreCase("exit")) {
				break;
			}
		}
		try {
			jettyServer.stop();
		} catch (Exception e) {
			log.error(e);
		}
		
	}
	
	private static void initSpring() {
		log.info("Initializing Spring context...");
		springContext = new ClassPathXmlApplicationContext("camel-config.xml");
		springContext.registerShutdownHook();
	}
	
	private static void initJetty() throws Exception{
		log.info("Starting server...");
		jettyServer = new Server(8080);

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
