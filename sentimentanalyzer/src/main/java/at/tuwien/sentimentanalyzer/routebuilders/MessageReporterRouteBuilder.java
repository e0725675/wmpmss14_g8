package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import at.tuwien.sentimentanalyzer.converters.MessageAggregationStrategy;
/**
 * Twitter polling
 * @author CLF
 *
 */
public class MessageReporterRouteBuilder extends RouteBuilder{
	@Autowired
	MessageAggregationStrategy messageAggregationStrategy;
	@Override
	public void configure() throws Exception {
		
		// split incoming messages to daily and weekly aggregators
		from("direct:aggregatorQueue").
		multicast().
		to("direct:aggregatorDaily", "direct:aggregatorWeekly");
		
		from("direct:aggregatorDaily").
			routeId("aggregateMessagesDaily").
		log(LoggingLevel.TRACE, "aggregateMessagesDaily in").
		aggregate(constant(true)).
			aggregationStrategyRef("messageAggregationStrategy").
			completionInterval(30000).
			beanRef("aggregatorConvertor","messagesToAggregateMessages").
			log(LoggingLevel.INFO, "Creating Daily Report").
			to("direct:outGoingAggregatorDaily");
		
		from("direct:aggregatorWeekly").
			routeId("aggregateMessagesWeekly").
		log(LoggingLevel.TRACE, "aggregateMessagesWeekly in").
		aggregate(constant(true)).
			aggregationStrategyRef("messageAggregationStrategy").
			completionInterval(500000).
			beanRef("aggregatorConvertor","messagesToAggregateMessages").
			log(LoggingLevel.INFO, "Creating Weekly Report").
			to("direct:outGoingAggregatorWeekly");

		
		
		//TODO: reactivate twitter
		//from("twitter://search?type=polling&numberOfPages=1&delay=60&keywords={{search.term}}&consumerKey=hEnR5kZzziT64SMb1IZjmJw9f&consumerSecret=LZc6bLhihxVCicn9JBi28EHC73C6u1llQ2PjA9W6uEUastACrv&accessToken=2493103232-0ath7kWee8wC98OryubP1TvLa0SmDQM3sfm8jlU&accessTokenSecret=m4PwikkabfFBgagebNZW1h8qYG25cks8K6uyNvOX89AAE").
		//from("servlet:///makereport").
		from("direct:outGoingAggregatorDaily").
			routeId("generateDailyReport").
		//beanRef("messageMocker", "nextAggregatedMessage").
		beanRef("reportGenerator", "generateAggregatedMessagesPDFReport").
		beanRef("mailHandler","recipientListDaily").
		beanRef("mailHandler","addAttachment").
		setBody(simple("Daily Sentiment Report")).
		choice().
			when(header("touri").isEqualTo("")).
			otherwise().
				recipientList(";;;"). //not actuallly recipient list. needed so i can dynamically create URL
					simple("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv&subject=report&from=workflow@applepublic.tv&to=${header.touri}");

		
		
		
		from("direct:outGoingAggregatorWeekly").
			routeId("generateWeeklyReport").
		beanRef("reportGenerator", "generateAggregatedMessagesPDFReport").
		beanRef("mailHandler","recipientListWeekly").
		beanRef("mailHandler","addAttachment").
		setBody(simple("Weekly Sentiment Report")).
		choice().
			when(header("touri").isEqualTo("")).
			otherwise().
				recipientList(";;;"). //not actuallly recipient list. needed so i can dynamically create URL
					simple("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv&subject=report&from=workflow@applepublic.tv&to=${header.touri}");
	}
	
}
