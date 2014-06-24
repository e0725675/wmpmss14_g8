package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.builder.RouteBuilder;

public class SwearFilterRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("direct:filterMessageQueue").
			routeId("SwearFilterRoutes").
	    filter().method("swearChecker", "isUserBlocked").to("direct:filteredMessages");
	}
}

