### OpenMUC Configurator
Distribution of configuration properties from a file to OSGi bundles using the ConfigAdmin service.

This component reads configuration properties from a file and distributes the artifacts using the OSGi ConfigurationAdmin service. The following code shows an example of a valid configuration file:

```xml
<configuration>
  <component name="org.example.component1">
    <property1>value1</property1>
    <property2>value2</property2>
  </component>
  <component name="org.example.component2">
    ...
  </component>
</configuration>
```

The example defines configuration properties for two OSGi components. The components must implement the [ManagedService Interface](https://osgi.org/javadoc/r4v42/org/osgi/service/cm/ManagedService.html) defined in the OSGi compendium. The default filename for the configuration file is `conf/configuration.xml`, but can be changed by setting the system property `org.openmuc.extensions.core.configurator.filename` in the file `conf/system.properties`:

```
org.openmuc.extensions.core.configurator.filename=conf/myconfigfile.xml
```

