package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import at.tuwien.sentimentanalyzer.PropertiesLoader;
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
		if (PropertiesLoader.configProperties == null) throw new RuntimeException("configProperties is null");
		// split incoming messages to daily and weekly aggregators
		from("direct:aggregatorQueue").
		log(LoggingLevel.DEBUG,"aggregatorQueue").
		multicast().
		to("direct:aggregatorDaily", "direct:aggregatorWeekly", "direct:aggregatorPoll");
		
		from("direct:aggregatorDaily").
			routeId("aggregateMessagesDaily").
		log(LoggingLevel.TRACE, "aggregateMessagesDaily in").
		aggregate(constant(true)).
			aggregationStrategyRef("messageAggregationStrategy").
			completionInterval(PropertiesLoader.configProperties.getLong("mail.dailyInterval")).
			beanRef("aggregatorConvertor","messagesToAggregateMessages").
			log(LoggingLevel.INFO, "Creating Daily Report").
			to("direct:outGoingAggregatorDaily");
		
		from("direct:aggregatorWeekly").
			routeId("aggregateMessagesWeekly").
		log(LoggingLevel.TRACE, "aggregateMessagesWeekly in").
		aggregate(constant(true)).
			aggregationStrategyRef("messageAggregationStrategy").
			completionInterval(PropertiesLoader.configProperties.getLong("mail.weeklyInterval")).
			beanRef("aggregatorConvertor","messagesToAggregateMessages").
			log(LoggingLevel.INFO, "Creating Weekly Report").
			to("direct:outGoingAggregatorWeekly");
		
		from("direct:aggregatorPoll").
			routeId("aggregateMessagesPoll").
		log(LoggingLevel.TRACE, "aggregateMessagesPoll in").
		aggregate(constant(true)).
			aggregationStrategyRef("messageAggregationStrategy").
			completionPredicate(method("reportGenerator", "shouldSendMessageReport")).
			beanRef("aggregatorConvertor","messagesToAggregateMessages").
			log(LoggingLevel.INFO, "Creating Poll Report").
			to("direct:outGoingAggregatorPoll");
		
		
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
			log(LoggingLevel.INFO, "Daily report mail string: ${header.touri}...").
				recipientList(";;;"). //not actuallly recipient list. needed so i can dynamically create URL
					simple("smtps://{{mail.host}}:{{mail.port}}?password={{mail.password}}&username={{mail.username}}&subject=Sentimentreport&from={{mail.from}}&BCC=${header.touri}");
		
		
		
		
		from("direct:outGoingAggregatorWeekly").
			routeId("generateWeeklyReport").
		beanRef("reportGenerator", "generateAggregatedMessagesPDFReport").
		beanRef("mailHandler","recipientListWeekly").
		beanRef("mailHandler","addAttachment").
		setBody(simple("Weekly Sentiment Report")).
		choice().
			when(header("touri").isEqualTo("")).
			otherwise().
			log(LoggingLevel.INFO, "Weekly report mail string: ${header.touri}...").
				recipientList(";;;"). //not actuallly recipient list. needed so i can dynamically create URL
					simple("smtps://{{mail.host}}:{{mail.port}}?password={{mail.password}}&username={{mail.username}}&subject=Sentimentreport&from={{mail.from}}&BCC=${header.touri}");
	
		
		from("direct:outGoingAggregatorPoll").
			routeId("generatePollReport").
		//beanRef("messageMocker", "nextAggregatedMessage").
		beanRef("reportGenerator", "generateAggregatedMessagesPDFReport").
		beanRef("mailHandler","addAttachment").
		setBody(simple("Poll Sentiment Report")).
		log(LoggingLevel.INFO, "Poll report mail string: {{mail.email}}...").
		recipientList(";;;"). //not actuallly recipient list. needed so i can dynamically create URL
			simple("smtps://{{mail.host}}:{{mail.port}}?password={{mail.password}}&username={{mail.username}}&subject=Sentimentreport&from={{mail.from}}&to={{mail.email}}");

	
	
	}
	
}
