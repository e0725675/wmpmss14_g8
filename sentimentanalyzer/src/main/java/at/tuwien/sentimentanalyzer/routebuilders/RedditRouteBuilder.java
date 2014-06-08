package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.IdempotentConsumer;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;

import at.tuwien.sentimentanalyzer.entities.reddit.RedditMessage;
/**
 * Twitter polling
 * @author LG
 *
 */
public class RedditRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		GsonDataFormat formatMessage = new GsonDataFormat(RedditMessage.class);
		from("timer:redditTimer?period=5000").
		to("http4://www.reddit.com/search.json?q={{search.term}}&sort=new&limit=1&t=hour").
		

		
//		process(new Processor() {
//			
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				Message msg = exchange.getIn();
//				String message = msg.getBody(String.class);
//				//System.out.println("Message: " + message);
//				
//				exchange.getOut().setBody(message);
//				exchange.getOut().setHeaders(msg.getHeaders());
//				
//			}
//		}).
		unmarshal(formatMessage).
		beanRef("redditConvertor", "getMessage").
		to("log:RedditTester").
		to("direct:incomingMessages");
	}
	
}
