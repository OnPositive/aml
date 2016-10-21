#RAML Types to Java classes conversion

#Programmatic conversion

This is a minimalistic example of converting RAML library to Java classes programmatically.
```java
TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(new BufferedInputStream(new FileInputStream(ramlFile)),
						new FileResourceLoader(ramlFile.getParentFile()), ramlFile.getName());
JavaWriter wr = new JavaWriter();
wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
wr.setDefaultPackageName(defaultPackageName);
wr.write(build);
wr.getModel().build(outputFolder);
```

#Using as maven plugin

Simply add plugin configuration to your maven build: 
```xml
			<plugin>
				<version>0.0.3</version>
				<groupId>com.onpositive.aml</groupId>
				<configuration>
					<ramlFiles><value>./types.raml</value></ramlFiles>
					<outputFolder>${basedir}/main/java</outputFolder>
				</configuration>
				<artifactId>raml2java-maven-plugin</artifactId>
				<executions>
					<execution>
						<goals>
							<goal>generateJava</goal>
						</goals>
						<phase>generate-sources</phase>
					</execution>
				</executions>
			</plugin>
```

The RAML definition will be processed and the Java code will be generated when running `mvn compile` or `mvn package` during `generate-sources` execution phase

####Eclipse usage

When developing in Eclipse, you must manage lifecycle mapping. For this purpose your `pom.xml` must have following child element in `pluginExecutions` section:
``` xml
<pluginManagement>
			<plugins>
				<!--This plugin's configuration is used to store Eclipse m2e settings only. It has no influence on the Maven build itself.-->
				<plugin>
					<groupId>org.eclipse.m2e</groupId>
					<artifactId>lifecycle-mapping</artifactId>
					<version>1.0.0</version>
					<configuration>
						<lifecycleMappingMetadata>
							<pluginExecutions>
								<pluginExecution>
									<pluginExecutionFilter>
										<groupId>
											com.onpositive.aml
										</groupId>
										<artifactId>
											raml2java-maven-plugin
										</artifactId>
										<versionRange>
											[0.0.1,)
										</versionRange>
										<goals>
											<goal>generateJava</goal>
										</goals>
									</pluginExecutionFilter>
									<action>
										<ignore></ignore>
									</action>
								</pluginExecution>
							</pluginExecutions>
						</lifecycleMappingMetadata>
					</configuration>
				</plugin>
			</plugins>
		</pluginManagement>
```


#Configuration Options

This chapter uses names of parameters used in Maven Plugin, however same options are accessible for programmatical access through `JavaGenerationConfig` class.

##Input and output related options

* `outputFolder` - folder to write Java files
* `<ramlFiles><value>`path to raml file`</value></ramlFiles>` - list of RAML files to process

###Serialization framework related parameters

* `gsonSupport` - if set to true raml2java will generate annotations required for serialization/deserialization with gson. default false
* `jacksonSupport` - if set to true raml2java will generate annotations required for serialization/deserialization with jackson. default true
* `jaxbSupport` - if set to true raml2java will generate annotations required for serialization/deserialization with jaxb. default true

###Types representation

* `containerStrategyCollection` - allows to configure default strategy for representing array types. By default array types are represented with lists. Setting to false will map array types to Java arrays
* `integerFormat` - allows to configure default representation for `integer` type. Valid values: `INT,LONG,BIGINT`. Default `INT`. 
* `numberFormat`- allows to configure default representation for `number` type. Valid values: `DOUBLE,BIGDECIMAL`. Default `DOUBLE`
* `wrappedTypesStrategy` - allows to choose strategy for wrrapping number and boolean types. Valid values: `NONE,OPTIONAL,ALWAYS`. Default `NONE`
* `addGenerated` - allows to choose if `Generated` annotation should be added to generated Java types. Default `true`

###Generated types customization related parameters

* `hashCodeAndEquals` - if set to true raml2java will generate `hashCode` and `equals` methods for generated classes (note generated implementation uses, org.apache.commons.lang classes)
* `implementSerializable` - if set to true generated beans will implement `Serializable` interface
* `implementClonable` - if set to true generated beans will implement `Clonable` interface, public `clone` method will also be generated
* `generateBuilderMethods` - if set to true generated beans will have builder methods. Default `true`
* `includeJsr303Annotations` - if set to true generated beans will have `javax.validation` annotations representing RAML types constraints when needed/possible. Default `true`


###Annotations processing

* `annotationNamespacesToSkipDefinition` - allows to configure namespaces for annotation types which should not be defined by RAML2Java generator (assumed as preexisting)
* `annotationNamespacesToSkipReference` - allows to configure namespaces of  annotation  which should not be translated to Java code
* `annotationIdsToSkipDefinition`  - allows to configure ids of  annotation types (simple or including namespace) which should not be defined by RAML2Java generator (assumed as preexisting)
* `annotationIdsToSkipReference` - allows to configure ids of  annotation  which should not be translated to Java code
* `skipAllAnnotationDefinitions` - shortcut to prevent tool from annotation types generation
* `skipAllAnnotationReferences` - shortcut to prevent tool from annotations generation

###Annotations on RAML types that are supported

[Currently Supported Annotations](https://petrochenko-pavel-a.github.io/raml-explorer/#https://raw.githubusercontent.com/OnPositive/aml/master/raml2java.raml)

###External types

External  types are converted by JSONSchema2Pojo (JSON Schema) and JAXB (XSD)

#Examples

There are following example projects at this moment:

* [Simple Example using types from Instagram API definition](https://github.com/OnPositive/aml/tree/master/examples/org.aml.example.raml2java.simple) 
* [More complex example, demoing annotations, multiple inheritance and unions](https://github.com/OnPositive/aml/tree/master/examples/org.aml.example.raml2java.annotations)
* [Fine grained target package configuration](https://github.com/OnPositive/aml/tree/master/examples/org.aml.example.multiPackage)

To run example project you should go to project directory and execute `mvn compile` from command line, Java classes will be generated to `/generated-sources/main/java`

#FAQ
 
 ###How I can configure raml files custom packages?
 
 You may annotate files or types with r2j annotations, as in this sample:
 
 ```raml
 #%RAML 1.0 Library
uses:
  lib: ./libs/lib.raml
  j2r: https://raw.githubusercontent.com/OnPositive/aml/master/raml2java.raml
annotationTypes:
  FieldConfig:
    type: object
    (j2r.package): org.aml.annotations #only affects FieldConfig class
    properties:
      order: int
      caption: string
(j2r.package): org.aml.models.vehicles #per file default setting
types:
  
  VehiclesAtLocation:
    properties:
      location: string
      vehicles: Vehicle[]
 ```
 
 ###How I can customize Java name of property or generated type
 
 You may annotate files or types with r2j annotation `javaName`, as in this sample:
 
 ```raml
 #%RAML 1.0
title: Sample
uses:
  r2j: raml2java.raml
types:

  Exchange:
    (r2j.javaName): JavaExchange
    type: object
    properties:
      value: integer
      in:
        type: integer
        (r2j.javaName): inputProperty
      out: integer
      
###How I can to map RAML type to existing Java type, How I can to specify existing super class or existing super interfaces
You should use `mapsToExisting` , `extendExisting` , `implementsExisting` r2j annotations

Example: 

```raml 
#%RAML 1.0 Library
uses:
  r2j: raml2java.raml
types:
  QMap:
    (r2j.mapsToExisting): java.util.LinkedHashMap
    type: object
  W:
    (r2j.extendExisting): javax.swing.JFrame
    (r2j.implementsExisting): [javax.swing.WindowConstants]
    properties:
      map: QMap
 
```      