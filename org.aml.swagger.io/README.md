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

###Major Limitations

 - Swagger can not represent union types so union types are converted to the types without restrictions at all. (better mapping scheme may be implemented later)
 - Swagger does not support full json schema and xsd schemas so they are converted to the types without restrictions (this should be improved)
 - Swagger does not support OAuth 1.0 as well as does not support full capabilities of Path Through security schemes, so Oauth1.0 is not represented correctly, and Path Through security schemes are represented only if described by allows conversion of them to Swagger Apikey security scheme type.
 - Swagger does not supports  different bodies for different media types so in the cases when body payloads are different first one is used.

###Project status

 `Swagger->RAML`: Project is capable to convert [microsoft azure swagger specifications](https://github.com/Azure/azure-rest-api-specs.git) as well as absolute most selected APIs (not microsoft and not google) from [API gurus registry of apis](https://apis.guru)

Succesfully converted:129 of 141 api gurus apis (absolute most of errors seemed to be caused by totally broken swagger files) and all microsoft apis.

 
 `RAML->Swagger`: Project was able to succesfully convert 511 of 546 specification in [our Raml api registry](https://github.com/apiregistry/registry).  
 
 Absolute most of problems is caused by inability of Java Parser to parse specification 
  - RAML Java parser failed with exception:27
  - Errored:6  (this are actual bugs to solve)
  - Incorrect swagger:2  (both are Oath 1.0 APIs)
  
So it seems that the status of both converters may be considered as stable beta. (Any issues and suggestions are greatly apreciated)
 
 
 
