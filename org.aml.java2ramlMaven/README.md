#Configuration Options

##Input and output related options

* `packagesToProcess` - list of java package names to process and convert to RAML library (Required)
* `outputFile` - path where to store generated raml library. Defaults to `${project.build.directory}/generated-sources/raml/types.raml`
* `classMasksToIgnore` - list of regular expressions. If class name matches to expression this class is excluded from generation
* `ignoreUnreferencedAnnotationDeclarations` - set it to true if you want to ignore annotation type declarations in the processed packages.


##Conversion Style Related options

* `memberMode` - allows to configure what kind of Java type members should be converted to RAML Type properties. Allowed values: FIELDS , PROPERTIES .Defaults to FIELDS
* `optinalityMode` - allows to configure how Java Object types should be represented by default with respect to `required` property facet and `nill` type. Allowed values: PRIMITIVES_ARE_REQUIRED , EVERYTHING_IS_REQUIRED , OBJECTS_ARE_NULLABLE. Defaults to: EVERYTHING_IS_REQUIRED
* `annotationsBehavior` - this parameter allows to configure how Java annotations should be represented. Allowed values: IGNORE_ALL_EXCEPT_EXPLICIT_PACKAGES , GENERATE_ALL. Defaults to IGNORE_ALL_EXCEPT_EXPLICIT_PACKAGES.
* `annotationPackages` - this parameter allows to set a list of packages which contains declarations of annotations which should be converted to RAML.

* `annotationProfiles` - this parameter allows to path list of files containing profiles for a custom handling of Java annotations. Alternatively you may pass names of built in annotation profiles to it. 
* `ignoreDefaultAnnotationProfiles` - turns of default annotation profiles. By default following built in profiles are turned on javax.validaton, jaxb, lang



