package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
/**
 * Twitter polling
 * @author LG
 *
 */
public class RedditRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("http4://www.reddit.com/search.json?q=ferrari").
		process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				String message = exchange.getIn().getBody(String.class);
				//System.out.println("Message: " + message);
				
			}
		}).
		to("log:RedditTester");
	}
	
}
