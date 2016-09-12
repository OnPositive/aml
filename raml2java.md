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

##Input and output related options

* `outputFolder` - folder to write Java files
* `<ramlFiles><value></ramlFiles>` - list of RAML files to process

#Examples

There are following example projects at this moment:

* [Simple Example using types from Instagram API definition](https://github.com/OnPositive/aml/tree/master/examples/org.aml.example.raml2java.simple) 
* [More complex example, demoing annotations, multiple inheritance and unions](https://github.com/OnPositive/aml/tree/master/examples/org.aml.example.raml2java.annotations)

To run example project you should go to project directory and execute `mvn compile` from command line, Java classes will be generated to `/generated-sources/main/java` 
