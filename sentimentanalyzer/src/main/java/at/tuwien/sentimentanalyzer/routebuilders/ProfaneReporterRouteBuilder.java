package at.tuwien.sentimentanalyzer.routebuilders;

import static org.apache.camel.builder.PredicateBuilder.and;
import static org.apache.camel.builder.PredicateBuilder.not;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import at.tuwien.sentimentanalyzer.beans.MailHandler;
import at.tuwien.sentimentanalyzer.converters.MessageAggregationStrategy;
/**
 * Twitter polling
 * @author CLF
 *
 */
public class ProfaneReporterRouteBuilder extends RouteBuilder{
	//@Autowired
	//MessageAggregationStrategy messageAggregationStrategy;
	@Override
	public void configure() throws Exception {
		
		///getSwearwordReport?email=mymail@mail.com&from=2014.06.01&to=2014.06.02
		//from("servlet:///getSwearwordReport").
		//to("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv&subject=swearreport&from=workflow@applepublic.tv&to=e0725675@student.tuwien.ac.at");
		//to("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv&subject=report&from=workflow@applepublic.tv&to=${header.touri}");
//		
		//from("servlet:///getSwearwordReport").
		from("timer:timerXX?period=10000").
		to("smtps://smtp.gmail.com:465?to=fuzzy&password=wmpmSS2014&username=workflow@applepublic.tv&subject=Swearreport&from=workflow@applepublic.tv");
//		routeId("swearwordReportRoute").
//		setHeader("email",simple("e0725675@student.tuwien.ac.at")).
////		choice().
////			when(not(and(
////					header("email").regex(MailHandler.REGEXMAIL),
////					header("from"),
////					header("to")
////					))).
////				transform(simple("Invalid or missing parameter(s).\nExample: ../getSwearwordReport?email=mymail@mail.com&from=2014.06.01&to=2014.06.02")).
////			otherwise().
//			//beanRef("swearChecker","getSwearReport").
//			//beanRef("reportGenerator", "generateSwearwordPDFReport").
//			log(LoggingLevel.INFO, "Creating Swearreport").
//			//beanRef("mailHandler","addAttachment").
//			setBody(simple("Swear Report")).
//			choice().
//			when(header("email").isEqualTo("")).
//			otherwise().
//				to("log:toumessage").
//				log(LoggingLevel.INFO, "email=${header.email}...").
//				log("smtps://smtp.gmail.com:465?to=${header.email}&password=wmpmSS2014&username=workflow@applepublic.tv&subject=Swearreport&from=workflow@applepublic.tv").
//				//simple("smtps://smtp.gmail.com:465?password=wmpmSS2014&username=workflow@applepublic.tv&subject=report&from=workflow@applepublic.tv&to=${header.touri}");
//				recipientList(";;;"). //not actuallly recipient list. needed so i can dynamically create URL
//					simple("smtps://smtp.gmail.com:465?to=&password=wmpmSS2014&username=workflowapplepublic.tv&subject=Swearreport&from=workflow@applepublic.tv");
	}
	
}
