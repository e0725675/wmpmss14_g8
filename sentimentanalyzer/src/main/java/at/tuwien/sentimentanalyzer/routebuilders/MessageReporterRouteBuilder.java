package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.builder.RouteBuilder;
/**
 * Twitter polling
 * @author CLF
 *
 */
public class MessageReporterRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		//TODO: reactivate twitter
		//from("twitter://search?type=polling&numberOfPages=1&delay=60&keywords={{search.term}}&consumerKey=hEnR5kZzziT64SMb1IZjmJw9f&consumerSecret=LZc6bLhihxVCicn9JBi28EHC73C6u1llQ2PjA9W6uEUastACrv&accessToken=2493103232-0ath7kWee8wC98OryubP1TvLa0SmDQM3sfm8jlU&accessTokenSecret=m4PwikkabfFBgagebNZW1h8qYG25cks8K6uyNvOX89AAE").
		//to("bean:messageConverter?method=statusToMessage").
		//from("timer:messagemockerTimer2?period=20000").
		//from("servlet:///makereport").
		from("direct:outGoingAggregator").
		beanRef("messageMocker", "nextAggregatedMessage").
		beanRef("reportGenerator", "generateAggregatedMessagesPDFReport").
		beanRef("mailHandler","recipientListDaily").
		beanRef("mailHandler","addAttachment").
		setBody(simple("Daily Sentiment Report")).
		//to("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv").
		choice().
		when(header("touri").isEqualTo("")).
		//to("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv").
		//to("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv&subject=report&from=workflow@applepublic.tv").
		otherwise().
		recipientList(";;;"). //not actuallly recipient list. needed so i can dynamically create URL
		simple("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv&subject=report&from=workflow@applepublic.tv&to=${header.touri}");
		//to("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv&BCC=${header.touri}");
		//to("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv&subject=report&from=workflow@applepublic.tv&to=${header.touri}&debugMode=true");
		//to("mock:reportgenerator");
		/*
		 * beanRef("mailHandler","addRecipientToDailyList").
		to("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv&BCC=${header.touri}");
		 */

	}
	
}
