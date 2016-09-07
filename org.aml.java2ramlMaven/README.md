#Configuration Options

##Input and output related options

* `packagesToProcess` - list of java package names to process and convert to RAML library (Required)
* `outputFile` - path where to store generated raml library. Defaults to `${project.build.directory}/generated-sources/raml/types.raml`
* `classMasksToIgnore` - list of regular expressions. If class name matches to expression this class is excluded from generation
* `ignoreUnreferencedAnnotationDeclarations` - set it to true if you want to ignore annotation type declarations in the processed packages.



