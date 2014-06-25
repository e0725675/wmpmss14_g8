//package at.tuwien.sentimentanalyzer.sample;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.camel.component.mock.MockEndpoint;
//import org.apache.camel.test.junit4.CamelTestSupport;
//import org.springframework.context.support.AbstractApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//public class FacebookTest extends CamelTestSupport {
//	
//	protected AbstractApplicationContext createApplicationContext() {
//		return new ClassPathXmlApplicationContext("FacebookContext.xml");
//	}
//
//	public void testFacebookRoute() {
//		String token = System.getProperty("facebook.UserToken");
//		try {
//			token = URLEncoder.encode(token, "UFF-8");
//			MockEndpoint end = getMockEndpoint("mock:finish");
//			end.setExpectedMessageCount(1);
//			Map<String, Object> headers = new HashMap<String, Object>();
//			headers.put("AccesToken", token);
//			sendBody("direct:start", null, headers);
//			end.assertIsSatisfied();
//		} catch (UnsupportedEncodingException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//}
