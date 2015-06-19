/*  OpenMUC Configurator
 *  Copyright (C) 2015 Mike Pichler
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openmuc.extensions.core.configurator;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configurator {
	
	public static Logger logger = LoggerFactory.getLogger(Configurator.class);
	
	private ConfigurationAdmin configAdmin = null;
	
	protected void activate(ComponentContext context) {
		
		String configFileName = System.getProperty("org.openmuc.extensions.core.configurator.filename");
		if (configFileName == null) {
			configFileName = "conf/configuration.xml";
		}
		
		File configFile = new File(configFileName);
		if(configFile.exists()) {
			try {
				updateConfigurations(configFile);
			} catch (ConfigurationException e) {
				logger.error("error loading configurations", e);
			} catch (IOException e) {
				logger.error("error while updating configurations", e);
			}
		} else {
			try {
				logger.error("could not find configuration file {}", configFile.getCanonicalPath());
			} catch (IOException ignore) { }
		}
	}
	
	protected void setConfigurationAdmin(ConfigurationAdmin service) {
		this.configAdmin = service;
	}
	
	protected void unsetConfigurationAdmin(ConfigurationAdmin service) {
		this.configAdmin = null;
	}
	
	private void updateConfigurations(File configFile) throws ConfigurationException, IOException {
		
		if(configAdmin == null) {
			logger.error("cannot update configurations because OSGi ConfigurationAdmin service is not available");
			return;
		}
		
		XMLConfiguration config = new XMLConfiguration(configFile);
		
		List<HierarchicalConfiguration> componentConfigs = config.configurationsAt("component");
		
		for (HierarchicalConfiguration componentConfig : componentConfigs) {
			String componentName = componentConfig.getString("[@name]");
			
			if(componentName != null) {
				
				Configuration osgiConfig = configAdmin.getConfiguration(componentName, null);
				
				Iterator<String> keys = componentConfig.getKeys();
				Dictionary<String, String> properties = new Hashtable<String, String>();
				
				while (keys.hasNext()) {
					String key = keys.next();
					properties.put(key, componentConfig.getString(key, ""));
				}
				
				osgiConfig.update(properties);
				
			} else {
				logger.warn("found component configuration, but name attribute is missing");
			}			
		} // foreach component config
	} // update
	
}
