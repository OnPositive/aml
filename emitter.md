#AML RAML Emitter

AML features an emitter for RAML files which allows to transfer instances of [common model](https://github.com/OnPositive/aml/tree/master/org.aml.model/src/main/java/org/aml/apimodel)
into RAML text.

using this model is as simple as:

```java
ApiImpl model=new ApiImpl();
model.setTitle("Hello");
ActionImpl orCreateMethod = model.getOrCreateResource("/api/persons").getOrCreateMethod("get");
orCreateMethod.addBody("application/json", BuiltIns.OBJECT);
AbstractType deriveObjectType = TypeOps.deriveObjectType("Person");
deriveObjectType.declareProperty("name", BuiltIns.STRING,false);
deriveObjectType.declareProperty("lastName", BuiltIns.STRING,false);
deriveObjectType.declareProperty("age", BuiltIns.INTEGER,false);	
orCreateMethod.addResponse("200","application/json", deriveObjectType);
model.addType(deriveObjectType);
orCreateMethod.addQueryParameter(new NamedParamImpl("count",BuiltIns.STRING, true, true));
orCreateMethod.addQueryParameter(new NamedParamImpl("offset",BuiltIns.STRING, false, true));
String store = new RamlWriter().store(model);

```
Which will produce following RAML code:

```raml
#%RAML 1.0
title: Hello
types:
  Person:
    type: object
    properties:
      name: string
      lastName: string
      age: integer
/api:      
  /persons:
    get:
      queryParameters:
        count: string
        offset?: string
      body:
        application/json: object
      responses:
        200:
          body:
            application/json: Person
```            

Key class here is [RAML Writer](https://github.com/OnPositive/aml/blob/master/org.aml.typesystem.yaml/src/main/java/org/aml/typesystem/yamlwriter/RamlWriter.java)


