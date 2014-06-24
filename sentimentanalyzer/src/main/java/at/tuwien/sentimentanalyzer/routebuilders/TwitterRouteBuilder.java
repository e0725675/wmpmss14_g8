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
		//from("twitter://search?type=polling&numberOfPages=1&delay=60&keywords={{search.term}}&consumerKey=hEnR5kZzziT64SMb1IZjmJw9f&consumerSecret=LZc6bLhihxVCicn9JBi28EHC73C6u1llQ2PjA9W6uEUastACrv&accessToken=2493103232-0ath7kWee8wC98OryubP1TvLa0SmDQM3sfm8jlU&accessTokenSecret=m4PwikkabfFBgagebNZW1h8qYG25cks8K6uyNvOX89AAE").
		//to("bean:messageConverter?method=statusToMessage").
		from("timer:messageockerTimer?period=5000").
		beanRef("messageMocker", "nextMessage").
		to("direct:incomingMessages");
	}
	
}
