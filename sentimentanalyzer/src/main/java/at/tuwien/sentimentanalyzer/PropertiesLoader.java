package at.tuwien.sentimentanalyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * For program configuration
 * @author CLF
 *
 */
public class PropertiesLoader {
	private static Logger log = Logger.getLogger(PropertiesLoader.class);
	
	private static Properties props = init();
	
	public static Properties getProperties() {
		return props;
	}
	
	private static Properties init() {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("config.properties"));
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException config.properties",e);
		} catch (IOException e) {
			log.error("IOException config.properties",e);
		}
		return p;
	}
}
