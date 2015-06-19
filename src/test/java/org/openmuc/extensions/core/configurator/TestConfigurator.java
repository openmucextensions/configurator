package org.openmuc.extensions.core.configurator;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class TestConfigurator {

	private final String componentName1 = "org.example.component1";
	private final String componentName2 = "org.example.component2";
	private final String propertyKey1 = "property1";
	private final String propertyValue1 = "value1";
	private final String propertyKey2 = "property2";
	private final String propertyValue2 = "value2";
	
	private Configurator instance;
	
	@Before
	public void setUp() {
		instance = new Configurator();
	}
	
	@Test
	public void testActivate() throws Throwable {
		
		// test with 2 component configurations, 2 properties each
		File configFile = createTemporaryConfigFile();
		System.setProperty("org.openmuc.extensions.core.configurator.filename", configFile.getCanonicalPath());
		
		// setup mock objects
		ConfigurationAdmin configAdmin = mock(ConfigurationAdmin.class);
		Configuration configuration1 = mock(Configuration.class);
		Configuration configuration2 = mock(Configuration.class);
		when(configAdmin.getConfiguration(eq(componentName1), isNull(String.class))).thenReturn(configuration1);
		when(configAdmin.getConfiguration(eq(componentName2), isNull(String.class))).thenReturn(configuration2);
		
		instance.setConfigurationAdmin(configAdmin);
		instance.activate(null);
		
		Dictionary<String, String> properties1 = new Hashtable<String, String>();
		properties1.put("[@name]", componentName1);
		properties1.put(propertyKey1, propertyValue1);
		properties1.put(propertyKey2, propertyValue2);
		
		Dictionary<String, String> properties2 = new Hashtable<String, String>();
		properties2.put("[@name]", componentName2);
		properties2.put(propertyKey1, propertyValue1);
		properties2.put(propertyKey2, propertyValue2);
		
		verify(configuration1, times(1)).update(properties1);	
		verify(configuration2, times(1)).update(properties2);
		
	}
	
	private File createTemporaryConfigFile() throws Throwable {
		
		File configFile = File.createTempFile("temp", "config");
		configFile.deleteOnExit();
		
		PrintWriter writer = new PrintWriter(configFile);
		
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<configuration>");
		
		writer.println("<component name=\"" + componentName1 + "\">");
		writer.println("<" + propertyKey1 + ">" + propertyValue1 + "</" + propertyKey1 + ">");
		writer.println("<" + propertyKey2 + ">" + propertyValue2 + "</" + propertyKey2 + ">");
		writer.println("</component>");
		
		writer.println("<component name=\"" + componentName2 + "\">");
		writer.println("<" + propertyKey1 + ">" + propertyValue1 + "</" + propertyKey1 + ">");
		writer.println("<" + propertyKey2 + ">" + propertyValue2 + "</" + propertyKey2 + ">");
		writer.println("</component>");
		
		writer.println("</configuration>");
		
		writer.flush();
		writer.close();
		
		return configFile;
	}

}
