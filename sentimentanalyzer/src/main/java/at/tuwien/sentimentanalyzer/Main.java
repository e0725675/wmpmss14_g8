package at.tuwien.sentimentanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	private static Logger log = Logger.getLogger(Main.class);
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("camel-config.xml");
		log.info("Starting server...");
		Server server = new Server(8080);
		
        try {
            WebAppContext context = new WebAppContext();
            //context.setDescriptor("/WEB-INF/web.xml");
            context.setDescriptor("WEB-INF/web.xml");
            context.setResourceBase("src/webapp/");
            context.setContextPath("");
            context.setParentLoaderPriority(true);
            log.info(context.toString());
            server.setHandler(context);
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Type \"exit\" to exit.");
		while (true) {
			try {
				line = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your name!");
				System.exit(1);
			}
			if (line.equalsIgnoreCase("exit")) {
				break;
			}
		}
		try {
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctx.registerShutdownHook();
	}

}
