package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.builder.RouteBuilder;
/**
 * Message Swear filter route 
 * @author matt
 *
 */
public class SwearCheckerRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		/* message filter for strong cusswords.
		 * 
		 */
		from("direct:ProfaneRoutes").
			routeId("profaneRoute").
		beanRef("messageMocker", "nextMessage").
		  	choice()
		   	.when(body().contains("fuck")).to("direct:incomingMessages")
		  		.when(body().contains("shit")).to("direct:incomingMessages")
		  		.when(body().contains("asshole")).to("direct:incomingMessages")
		  		.when(body().contains("cunt")).to("direct:incomingMessages")
		  		.when(body().contains("motherfucker")).to("direct:incomingMessages")
		  		.otherwise().to("mock:result")
		  	.end();
		  	//.to("mock:result");
		
	}
	
}
