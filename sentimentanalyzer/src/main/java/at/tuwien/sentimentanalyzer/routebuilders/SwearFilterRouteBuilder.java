package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class SwearFilterRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("direct:filterMessageQueue").
			routeId("SwearFilterRoutes").
			log(LoggingLevel.DEBUG,"beforeFilter").
			filter(method("swearChecker", "isUserBlocked").isNotEqualTo(true)).
				log(LoggingLevel.DEBUG,"inFilter").
			filter(method("commentsFilter", "filterClient")).
				log(LoggingLevel.DEBUG,"inFilterClient").
		    filter(method("commentsFilter", "filterURL").isNotEqualTo(true)).
		    	log(LoggingLevel.DEBUG,"inFilterURL").
				to("direct:filteredMessages");
	}
}