package at.tuwien.sentimentanalyzer;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;

public class Configuration {
	public CamelContext context = null;
	private static Configuration instance = null;
//	private Configuration(CamelContext context) {
//		this.context = context;
//		PropertiesComponent pc = new PropertiesComponent();
//		pc.setCamelContext(context);
//		
//		pc.setLocation("file:src/main/resources/config.properties");
//		context.addComponent("properties", pc);
//	}
	
//	 public static void Init(CamelContext context) {
//        if (instance == null) {
//            instance = new Configuration(context);
//        }
//	 }
	
//	public static int ResolveInt(String Key){
//		try {
//			return Integer.parseInt(instance.context.resolvePropertyPlaceholders(Key));
//		}catch (Exception e) {
//			e.printStackTrace();
//		}return -1;
//	}
	// make sure properties are not missing when file is loaded!!
	
	
	//list of users that the client owns (username, source)
	
//email account for sending messages
//email, password, smtp(s) server(smtp.gmail.com:465)
//time-interval for daily report generation in seconds (default: 1 day)
//time-interval for weekly report generation in seconds (default: 1 week)
	
//http4://www.reddit.com/search.json?q={{search.term}}&sort=new&limit=20&t=hour"
//1 or more search terms for reddit
//limit
//"t"
// time between queries (in seconds)
	
	
//twitter://search?type=polling&numberOfPages=1&delay=60&keywords={{search.term}}&consumerKey=hEnR5kZzziT64SMb1IZjmJw9f&consumerSecret=LZc6bLhihxVCicn9JBi28EHC73C6u1llQ2PjA9W6uEUastACrv&accessToken=2493103232-0ath7kWee8wC98OryubP1TvLa0SmDQM3sfm8jlU&accessTokenSecret=m4PwikkabfFBgagebNZW1h8qYG25cks8K6uyNvOX89AAE"
// 1 ore more search terms
// numberOfPages
// delay
// consumerKey
// consumerSecret
// accessToken
// accessTokenSecret
	// time between queries (in seconds)
	
//Facebook:
//appId
//appSecret
	// time between queries (in seconds)
	// facebook page (string)
	
}
