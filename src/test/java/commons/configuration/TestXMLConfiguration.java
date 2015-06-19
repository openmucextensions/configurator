package commons.configuration;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXMLConfiguration {

	public static Logger logger = LoggerFactory.getLogger(TestXMLConfiguration.class);
	
	// set log level to debug: -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
	
	public static void main(String[] args) throws ConfigurationException {
		
		File configFile = new File("./src/test/resources/test.xml");
		
		if(configFile.exists()) {
			
			XMLConfiguration config = new XMLConfiguration(configFile);
			List<HierarchicalConfiguration> componentConfigs = config.configurationsAt("component");
			logger.debug("found {} component configuration(s)", componentConfigs.size());
			
			for (HierarchicalConfiguration componentConfig : componentConfigs) {
				
				// get the name of the component from XML attribute name
				String componentName = componentConfig.getString("[@name]");
				logger.debug("Component name={}", componentName);
				
				// logger.debug("username={}, password={}", componentConfig.getString("username"), componentConfig.getString("password"));
				
				// get a list of properties
				Iterator<String> keys = componentConfig.getKeys();
				
				while(keys.hasNext()) {
					String key = keys.next();
					logger.debug("key={}, value={}", key, componentConfig.getProperty(key));
				}
			}
			
			
		} else {
			logger.error("config file not found");
		}
		

	}

}
