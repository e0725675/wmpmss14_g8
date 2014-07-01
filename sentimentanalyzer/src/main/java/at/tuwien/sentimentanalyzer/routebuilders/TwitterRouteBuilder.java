package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;

import twitter4j.Status;
import at.tuwien.sentimentanalyzer.entities.Message;
/**
 * Twitter polling
 * @author CLF
 *
 */
public class TwitterRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("timer:twitterTimer?period={{twitter.period}}").
		routeId("twitterSearchRoute").
		to("twitter://search?type=direct&numberOfPages={{twitter.numberOfPages}}&delay={{twitter.delay}}&keywords={{search.term}}&consumerKey={{twitter.APIKey}}&consumerSecret={{twitter.APISecret}}&accessToken={{twitter.AccessToken}}&accessTokenSecret={{twitter.AccessTokenSecret}}").
		to("direct:twitterFilter");
		

		from("direct:twitterFilter").
		routeId("twitterIdempotentFilterRoute").
		split(body()).
		process(new Processor() { // set message header ID
			@Override
			public void process(Exchange exchange) throws Exception {
				Status msg = exchange.getIn().getBody(Status.class);
				exchange.getIn().setHeader("messageId", msg.getId());
				exchange.setOut(exchange.getIn());
			}
		}).
		idempotentConsumer(header("messageId"), MemoryIdempotentRepository.memoryIdempotentRepository(1000)).
		skipDuplicate(false).
		filter(property(Exchange.DUPLICATE_MESSAGE).isEqualTo(true)).
			log(LoggingLevel.DEBUG,"duplicate message:").
			to("log:duplicateTwitterMessages?level=DEBUG").stop().end().
		to("direct:twitter");
		
		from("direct:twitter").
		routeId("twitterFollowUpRoute").
				doTry().
					to("bean:messageConverter?method=statusToMessageCamel").
					to("direct:incomingMessages").
				doCatch(RuntimeException.class).
					to("log:twitterError?level=WARN");

	}
	
}