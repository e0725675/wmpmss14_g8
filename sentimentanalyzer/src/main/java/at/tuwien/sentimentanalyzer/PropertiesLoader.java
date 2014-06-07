package at.tuwien.sentimentanalyzer;


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
		log.info("Creating propertiesloader for "+filePath);
	}

	private static Logger log = Logger.getLogger(PropertiesLoader.class);

}
