package at.tuwien.sentimentanalyzer.sample;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
@Deprecated
public class TestProcessor implements Processor{
	public static Logger log = Logger.getLogger(TestProcessor.class);
	public void process(Exchange exchange) throws Exception {
	    System.out.println("Hello!");
	  }
}
