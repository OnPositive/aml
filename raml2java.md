#RAML Types to Java classes conversion

#Programmatic conversion

Look in [`org.aml.java2raml.Java2Raml`](https://github.com/OnPositive/aml/blob/master/org.aml.java2raml/src/main/java/org/aml/java2raml/Java2Raml.java) and [`org.aml.java2raml.Config`](https://github.com/OnPositive/aml/blob/master/org.aml.java2raml/src/main/java/org/aml/java2raml/Config.java) classes to get the idea of how you may use `org.aml.java2raml` to convert java class definitions to RAML programmatically

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

* `packagesToProcess` - list of java package names to process and convert to RAML library (Required)
* `outputFile` - path where to store generated raml library. Defaults to `${project.build.directory}/generated-sources/raml/types.raml`
* `classMasksToIgnore` - list of regular expressions. If class name matches to expression this class is excluded from generation
* `ignoreUnreferencedAnnotationDeclarations` - set it to true if you want to ignore annotation type declarations in the processed packages.


#Limitations

* At this moment only annotations with runtime retention policy will be represented in generated raml library.
* conversion to date types as well as to file type is not supported yet.


#Examples

There are following example projects at this moment:

* [Simple Example](/examples/org.aml.example.simple) 
* [Java Annotations moving to RAML](https://github.com/OnPositive/aml/tree/master/examples/org.aml.example.ramlannotations)
* [Using Annotation Profile to configure optionality and default values](https://github.com/OnPositive/aml/tree/master/examples/org.aml.example.customannotationprofile)  

To run example project you should go to project directory and execute `mvn compile` from command line, raml library will be generated to `target/generated-sources/raml/types.raml` 

