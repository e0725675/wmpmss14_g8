package at.tuwien.sentimentanalyzer.beans;

import org.apache.camel.CamelContext;
import org.apache.log4j.Logger;

import at.tuwien.sentimentanalyzer.routebuilders.MessageReporterRouteBuilder;
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
			// add routes from the twitter routebuilder
			//context.addRoutes(new TwitterRouteBuilder());
			// add routes from the messagereporter routebuilder
			context.addRoutes(new MessageReporterRouteBuilder());
			
			// YOU CAN ADD MORE ROUTEBUILDERS HERE!!!
			
			
		} catch (Exception e) {
			log.error("Error creating a route", e);
		}
	}

}
