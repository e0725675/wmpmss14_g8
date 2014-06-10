package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;

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
		unmarshal(formatMessage).
		beanRef("redditConvertor", "getMessage").
		//to("log:RedditTester").
		to("direct:incomingMessages");
	}
	
}
