package at.tuwien.sentimentanalyzer.connectors;

import java.io.UnsupportedEncodingException;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

import twitter4j.Status;

@Converter
public class TwitterConverter {
	public static Logger log = Logger.getLogger(TwitterConverter.class);
	
	@Converter
	public static Message toMessage(String string) {
		log.info(string);
		return new Message();
	}
	
	//twitter4j.internal.json.StatusJSONImpl
	@Converter
	public static Message toMessage(Status string) {
		log.info("StatusToMessage");
		return new Message();
		
	}
	@Converter
    public static Message toMessage(byte[] data, Exchange exchange) {
		Message out = new Message();
		log.info("toMessage");
        if (exchange != null) {
            String charsetName = exchange.getProperty(Exchange.CHARSET_NAME, String.class);
            if (charsetName != null) {
                try {
                	out.setMessage(new String(data, charsetName));
                    return out;
                } catch (UnsupportedEncodingException e) {
                    log.warn("Can't convert the byte to String with the charset " + charsetName, e);
                }
            }
        }
        out.setMessage(new String(data));
        return out;
    }
}
