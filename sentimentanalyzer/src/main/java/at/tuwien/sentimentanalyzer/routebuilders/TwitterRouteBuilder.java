package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.builder.RouteBuilder;
/**
 * Twitter polling
 * @author CLF
 *
 */
public class TwitterRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		//from("{{twitter.path}}").
		//to("bean:messageConverter?method=statusToMessage").
		from("timer:messageockerTimer?period={{twitter.period}}").
			routeId("messageMockerGenerationRoute").
		beanRef("messageMocker", "nextMessage").
		to("direct:incomingMessages");
	}
	
}