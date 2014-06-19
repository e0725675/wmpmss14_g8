package at.tuwien.sentimentanalyzer.routebuilders;

import org.apache.camel.builder.RouteBuilder;
/**
 * Twitter polling
 * @author CLF
 *
 */
public class MailRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("servlet:///addtodailylist").
			routeId("addtodailylist").
		choice().
			when(header("mail")).
				transform(simple("Added ${header.mail} to the daily recipient list.")).
			otherwise().
				transform(constant("Missing parameter: mail")).
			end().
		to("bean:mailHandler?method=addRecipientToDailyList");
		
		from("servlet:///removefromdailylist").
			routeId("removefromdailylist").
		choice().
			when(header("mail")).
				transform(simple("Removed ${header.mail} from the daily recipient list.")).
			otherwise().
				transform(constant("Missing parameter: mail")).
			end().
		to("bean:mailHandler?method=removeRecipientFromDailyList");
		
		
		from("servlet:///addtoweeklylist").
			routeId("addtoweeklylist").
		choice().
			when(header("mail")).
				transform(simple("Added ${header.mail} to the weekly recipient list.")).
			otherwise().
				transform(constant("Missing parameter: mail")).
			end().
		to("bean:mailHandler?method=addRecipientToWeeklyList");
		
		from("servlet:///removefromweeklylist").
			routeId("removefromweeklylist").
		choice().
			when(header("mail")).
				transform(simple("Removed ${header.mail} from the weekly recipient list.")).
			otherwise().
				transform(constant("Missing parameter: mail")).
			end().
		to("bean:mailHandler?method=removeRecipientFromWeeklyList");
	}
	
}
