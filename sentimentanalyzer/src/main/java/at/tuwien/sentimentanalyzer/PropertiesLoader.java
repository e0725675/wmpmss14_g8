package at.tuwien.sentimentanalyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;


/**
 * Wrapper for properties file which also allows for multiple valus per key
 * @author CLF
 *
 */
public class PropertiesLoader extends PropertiesConfiguration {
	
	public PropertiesLoader(String filePath) throws ConfigurationException {
		super(filePath);
	}

	private static Logger log = Logger.getLogger(PropertiesLoader.class);
	
	@Deprecated
	private static Properties props = init();
	
	@Deprecated
	public static Properties getProperties() {
		return props;
	}
	
	@Deprecated
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
