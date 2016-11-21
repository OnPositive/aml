#RAML 1.0 <-> Swagger
 
This module provides an API to transform RAML 1.0 to Swagger 2.0 and back from Swagger to RAML.

##Usage:   


###Transforming RAML to Swagger

```java
SwaggerWriter swaggerWriter = new SwaggerWriter();
String store = swaggerWriter.store(api);
```

###Transforming Swagger to RAML

```
ApiImpl rs = new SwaggerReader().read(content);//content is string containing swagger api definition
```

###Limitations

 