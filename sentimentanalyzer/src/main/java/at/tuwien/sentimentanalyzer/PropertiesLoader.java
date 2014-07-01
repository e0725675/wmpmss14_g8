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
	public static PropertiesLoader configProperties = init("config.properties");
	private static PropertiesLoader init(String filePath) {
		if (log == null) {
			log = Logger.getLogger(PropertiesLoader.class);
		}
		try {
			PropertiesLoader out = new PropertiesLoader(filePath);
			log.info("PropertiesLoader "+filePath+" created");
			return out;
		} catch (ConfigurationException e) {
			log.fatal(e);
			return null;
		}
	}
	public PropertiesLoader(String filePath) throws ConfigurationException {
		super(filePath);
		if (log == null) {
			log = Logger.getLogger(PropertiesLoader.class);
		}
		log.info("Creating propertiesloader for "+filePath);
		this.setThrowExceptionOnMissing(true); //please leave this
	}

	private static Logger log = null;//Logger.getLogger(PropertiesLoader.class);

}
