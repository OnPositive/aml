#Building API model from RAML

AML features common model on top of RAML 0.8 and RAML 1.0. This model is tightly binded to typesystem implementation located at [TypeSystem](https://github.com/OnPositive/aml/tree/master/org.aml.typesystem/src/main/java/org/aml/typesystem)

and is modeled by the following interfaces [CommonInterfaces](https://github.com/OnPositive/aml/tree/master/org.aml.model/src/main/java/org/aml/apimodel)

This model is not and AST (this means that it is not possible to get back to orginal RAML text from it), and was primary built with a convinience of usage in a most common usecases (generating RAML from some other model, or analizying API or Library structure/types)

Building an API model is handled by `org.aml.typesystem.yaml` artifact and is as simple as following sample

```java
Api api=(Api) new TopLevelRamlModelBuilder().build(store, new CompositeResourceLoader(),"some.raml");
TestCase.assertTrue(api.resources().get(0).methods().get(0).queryParameters().get(0).isRequired());
TestCase.assertTrue(!api.resources().get(0).methods().get(0).queryParameters().get(1).isRequired());
```

Key class here is [TopLevelRamlModelBuilder](https://github.com/OnPositive/aml/blob/master/org.aml.typesystem.yaml/src/main/java/org/aml/typesystem/ramlreader/TopLevelRamlModelBuilder.java)

browsing model is as simple as
```java
IProperty ps=build.getResource("/world/states/{stateId}").method("get").response("200").body("application/json").getTypeModel().toPropertiesView().property("abbr");
Pattern oneMeta = ps.range().oneMeta(Pattern.class);
ps=build.getResource("/world/states/{stateId}").method("put").body("application/json").getTypeModel().toPropertiesView().property("abbr");
		oneMeta = ps.range().oneMeta(Pattern.class);
 ```   


