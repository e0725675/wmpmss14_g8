package at.tuwien.sentimentanalyzer.sample;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class TestProcessor implements Processor{
	public void process(Exchange exchange) throws Exception {
	    System.out.println("Hello!");
	  }
}
