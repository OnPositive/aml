#Java Classes to RAML types conversion



Using from maven plugin:


#Configuration Options

##Input and output related options

* `packagesToProcess` - list of java package names to process and convert to RAML library (Required)
* `outputFile` - path where to store generated raml library. Defaults to `${project.build.directory}/generated-sources/raml/types.raml`
* `classMasksToIgnore` - list of regular expressions. If class name matches to expression this class is excluded from generation
* `ignoreUnreferencedAnnotationDeclarations` - set it to true if you want to ignore annotation type declarations in the processed packages.


##Conversion Style Related options

* `memberMode` - allows to configure what kind of Java type members should be converted to RAML Type properties. Allowed values: FIELDS , PROPERTIES .Defaults to FIELDS
* `optionalityMode` - allows to configure how Java Object types should be represented by default with respect to `required` property facet and `nill` type. Allowed values: PRIMITIVES_ARE_REQUIRED , EVERYTHING_IS_REQUIRED , OBJECTS_ARE_NULLABLE. Defaults to: EVERYTHING_IS_REQUIRED
* `annotationsBehavior` - this parameter allows to configure how Java annotations should be represented. Allowed values: IGNORE_ALL_EXCEPT_EXPLICIT_PACKAGES , GENERATE_ALL. Defaults to IGNORE_ALL_EXCEPT_EXPLICIT_PACKAGES.
* `annotationPackages` - this parameter allows to set a list of packages which contains declarations of annotations which should be converted to RAML.


##Annotation Profiles

Java annotations may oftenly affect on desired RAML types representation of Java types. This effects depends from used frameworks as well from developer preference and style. 

We support customization of this effects with `Annotation Profiles` feature which allows you to customize conversion behavior depending from set of Java member annotations. Annotations Profile are  xml configuration files which contains mappings of Java annotation parameters to RAML types facets as well as providing control on nullability, optionality, name and presense of generated RAML property in containing type.

The following snippet shows a built-in annotation profile which we use to customize generation of members depending from JAXB annotations present.

```xml
<annotations>
	<annotation name="javax.xml.bind.annotation.XmlElement" skipFromRaml="true">
		<member name="required" target="required"/>
		<member name="name" target="xml.name"/>
		<member name="namespace" target="xml.namespace"/>
		<member name="defaultValue" target="default"/>
		<member name="nillable" target="nullable"/>			
	</annotation>
	<annotation name="javax.xml.bind.annotation.XmlAttribute" skipFromRaml="true">
		<member name="required" target="required"/>
		<member name="name" target="xml.name"/>
		<member name="namespace" target="xml.namespace"/>
		<member target="xml.attribute" value="true"/>		
	</annotation>
	<annotation name="javax.xml.bind.annotation.XmlTransient" skipFromRaml="true">
		<member value="true" target="skipMember"/>	
	</annotation>		
	<annotation name="javax.xml.bind.annotation.XmlRootElement" skipFromRaml="true">
		<member name="name" target="xml.name"/>
		<member name="namespace" target="xml.namespace"/>
	</annotation>
	<annotation name="javax.xml.bind.annotation.XmlElementWrapper" skipFromRaml="true">
		<member target="xml.wrapped" value="true"/>
	</annotation>	
</annotations>
```
In addition to built in  RAML facets annotation profiles has following special targets:
* `skipMember` - allows to skip member depending from annotation presense or member value
* `required` - allows to configure if property is required depending from annotation presense or annotation member value
* `nullable` - allows to configure if resulting property type should allow `null` value
* `propName` - allows to configure resulting property name depending from member value

At this moment project contains following annotation  profiles which are applied by default:
  * `jaxb` - profile which customizes generation behavior depending from JAXB annotations (`javax.xml.bind`)
  * `javax.validation` - this profile converts `javax.validation` annotation to corresponding RAML types constraints
  * `lang` - settings to always ignore built-in java annotations

##Annotation Profiles related configuration parameters

* `annotationProfiles` - this parameter allows to path list of files containing profiles for a custom handling of Java annotations. Alternatively you may pass names of built in annotation profiles to it. 
* `ignoreDefaultAnnotationProfiles` - turns of default annotation profiles. By default following built in profiles are turned on `javax.validaton`, `jaxb`, `lang`

##Extensions

Some times you need to do further customization of the generation process in this case you may pass a set of classNames into `extensions` plugin parameter. This classes should implement one of our extension interface, provide default constructor and be available on plugin class path.




#Examples

[Simple Example](/examples/org.aml.example.simple)  
