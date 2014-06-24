package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.builder.RouteBuilder;

public class SwearFilterRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("direct:ProfaneRoutes").
			routeId("SwearFilterRoutes").
	    beanRef("messageMocker", "nextMessage").
	    filter().method("swearChecker", "isUserBlocked").to("direct:incomingMessages");
	}
}

