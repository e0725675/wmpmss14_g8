package at.tuwien.sentimentanalyzer.beans;

import org.apache.camel.CamelContext;
import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.routebuilders.MailRouteBuilder;
import at.tuwien.sentimentanalyzer.routebuilders.MessageReporterRouteBuilder;
import at.tuwien.sentimentanalyzer.routebuilders.RedditRouteBuilder;
import at.tuwien.sentimentanalyzer.routebuilders.SwearCheckerRouteBuilder;
import at.tuwien.sentimentanalyzer.routebuilders.TwitterRouteBuilder;
/**
 * A helper class since this is the only way to get access to the camel context
 * in this setup.
 * All java programmed routes can be configured here
 * they will be configured at program startup (whenever this bean gets initialized)
 * @author CLF
 *
 */
public class RouteCreator  {
	private static Logger log = Logger.getLogger(RouteCreator.class);
	
	public RouteCreator(CamelContext context) {
		log.info("RouteCreator created ");
		
		try {
			// Add routes from the following RouteBuilders
			context.addRoutes(new MessageReporterRouteBuilder());
			//context.addRoutes(new TwitterRouteBuilder());
			//context.addRoutes(new RedditRouteBuilder());
			//context.addRoutes(new SwearCheckerRouteBuilder());
			context.addRoutes(new MailRouteBuilder());
			// YOU CAN ADD MORE ROUTEBUILDERS HERE!!!
			
			
		} catch (Exception e) {
			log.error("Error creating a route", e);
		}
	}

}
