package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.Logger;
/**
 * Twitter polling
 * @author LG
 * 
 * Use the http4 component so every 5 seconds poll Reddit.com for the search term (Can be sorted to new, hot etc. limits e.g day. month, year
 * Unmarshal the json response to the bean to convert the json to the custom message type. 
 * Make an idempotent consumer. Take a hash of the message and source and set this as the id. 
 * Filter out the duplicates to the logger. (So you can see they weren't added)
 */
public class RedditRouteBuilder extends RouteBuilder{
	private static Logger log = Logger.getLogger(RedditRouteBuilder.class);
	@Override
	public void configure() throws Exception {
//		GsonDataFormat formatMessage = new GsonDataFormat(RedditMessage.class);
//		from("timer:redditTimer?period=5000").
//			routeId("redditRoute").
//		to("http4://www.reddit.com/search.json?q={{search.term}}&sort=new&limit=1&t=hour").
//		unmarshal(formatMessage).
//		beanRef("redditConvertor", "getMessage").
//		process(new Processor() {
//			
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				Message msg = exchange.getIn().getBody(Message.class);
//				
//				StringBuilder s = new StringBuilder();
//				s.append(msg.getMessage());
//				s.append(msg.getSource());				
//				String newID = String.valueOf(s.toString().hashCode());
//				exchange.getIn().setHeader("messageId", newID);
//				String id = exchange.getIn().getHeader("messageId", String.class);
//				log.trace("reddit headerid: " +  id);
//				
//			}
//		}).
//		idempotentConsumer(header("messageId"), MemoryIdempotentRepository.memoryIdempotentRepository(1000)).
//		skipDuplicate(false).
//		filter(property(Exchange.DUPLICATE_MESSAGE).isEqualTo(true)).
//			to("log:duplicateRedditMessages?level=DEBUG").stop().end().
//		to("direct:incomingMessages");
	}
	
}
