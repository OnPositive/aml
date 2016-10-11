package org.aml.typesystem.ramlwriter;

import java.util.Arrays;
import java.util.List;

import org.aml.apimodel.Api;
import org.aml.apimodel.impl.ActionImpl;
import org.aml.apimodel.impl.ApiImpl;
import org.aml.apimodel.impl.NamedParamImpl;
import org.aml.apimodel.impl.SecuritySchemeImpl;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.aml.typesystem.yamlwriter.RamlWriter;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.loader.CompositeResourceLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import junit.framework.TestCase;

public class WriterTest extends TestCase{

	static class CLS{
		public String name;
		public boolean isAttr;
	}
	
	public void test() {
		AbstractType deriveObjectType = TypeOps.deriveObjectType("Person");
		deriveObjectType.declareProperty("name", BuiltIns.STRING,false);
		deriveObjectType.declareProperty("lastName", BuiltIns.STRING,false);
		deriveObjectType.declareProperty("age", BuiltIns.INTEGER,false);		
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(FlowStyle.BLOCK);		
		RamlWriter w=new RamlWriter();
		String result=w.store(deriveObjectType);
		RamlModelResult buildApi = new RamlModelBuilder().buildApi(result, "");
		TestCase.assertTrue(!buildApi.hasErrors());
	}

	public void test1(){
		ApiImpl model=new ApiImpl();
		model.setTitle("Hello");
		ActionImpl orCreateMethod = model.getOrCreateResource("persons").getOrCreateMethod("get");
		orCreateMethod.addBody("application/json", BuiltIns.OBJECT);
		AbstractType deriveObjectType = TypeOps.deriveObjectType("");
		deriveObjectType.declareProperty("name", BuiltIns.STRING,false);
		deriveObjectType.declareProperty("lastName", BuiltIns.STRING,false);
		deriveObjectType.declareProperty("age", BuiltIns.INTEGER,false);	
		orCreateMethod.addResponse("200","application/json", deriveObjectType);
		orCreateMethod.addQueryParameter(new NamedParamImpl("count",BuiltIns.STRING, true, true));
		orCreateMethod.addQueryParameter(new NamedParamImpl("offset",BuiltIns.STRING, false, true));
		String store = new RamlWriter().store(model);
		RamlModelResult buildApi = new RamlModelBuilder().buildApi(store, "");
		
		TestCase.assertTrue(!buildApi.hasErrors());
		Api api=(Api) new TopLevelRamlModelBuilder().build(store, new CompositeResourceLoader(),"");
		TestCase.assertTrue(api.resources()[0].methods().get(0).queryParameters().get(0).isRequired());
		TestCase.assertTrue(!api.resources()[0].methods().get(0).queryParameters().get(1).isRequired());
	}
	
	public void test2(){
		ApiImpl model=new ApiImpl();
		model.setTitle("Hello");
		ActionImpl orCreateMethod = model.getOrCreateResource("persons").getOrCreateMethod("get");
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
		RamlModelResult buildApi = new RamlModelBuilder().buildApi(store, "");
		
		TestCase.assertTrue(!buildApi.hasErrors());
		Api api=(Api) new TopLevelRamlModelBuilder().build(store, new CompositeResourceLoader(),"");
		TestCase.assertTrue(api.resources()[0].methods().get(0).queryParameters().get(0).isRequired());
		TestCase.assertTrue(!api.resources()[0].methods().get(0).queryParameters().get(1).isRequired());
		TestCase.assertTrue(api.resources()[0].methods().get(0).responses().get(0).body().get(0).getTypeModel().name().equals("Person"));
	}
	
	public void test3(){
		ApiImpl model=new ApiImpl();
		model.setTitle("Hello");
		ActionImpl orCreateMethod = model.getOrCreateResource("persons").getOrCreateMethod("get");
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
		RamlModelResult buildApi = new RamlModelBuilder().buildApi(store, "");		
		TestCase.assertTrue(!buildApi.hasErrors());
		Api api=(Api) new TopLevelRamlModelBuilder().build(store, new CompositeResourceLoader(),"");
		TestCase.assertTrue(api.resources()[0].methods().get(0).queryParameters().get(0).isRequired());
		TestCase.assertTrue(!api.resources()[0].methods().get(0).queryParameters().get(1).isRequired());
		TestCase.assertTrue(api.resources()[0].methods().get(0).responses().get(0).body().get(0).getTypeModel().name().equals("Person"));
	}
	
	public void test4(){
		ApiImpl model=new ApiImpl();
		model.setTitle("Hello");
		model.addAnnotationType(TypeOps.derive("HelloAnnotation", BuiltIns.STRING));
		model.annotations().add(new Annotation("HelloAnnotation", "Hello"));
		String store = new RamlWriter().store(model);		
		RamlModelResult buildApi = new RamlModelBuilder().buildApi(store, "");		
		TestCase.assertTrue(!buildApi.hasErrors());		
		Api api=(Api) new TopLevelRamlModelBuilder().build(store, new CompositeResourceLoader(),"");
		AbstractType tp=api.annotations().get(0).annotationType();
		TestCase.assertTrue(tp.name().equals("HelloAnnotation"));
	}
	
	public void test5(){
		ApiImpl model=new ApiImpl();
		model.setTitle("Hello");
		model.addAnnotationType(TypeOps.derive("HelloAnnotation", BuiltIns.STRING));
		ActionImpl orCreateMethod = model.getOrCreateResource("persons").getOrCreateMethod("get");
		orCreateMethod.annotations().add(new Annotation("HelloAnnotation", "Hello"));
		String store = new RamlWriter().store(model);		
		RamlModelResult buildApi = new RamlModelBuilder().buildApi(store, "");		
		TestCase.assertTrue(!buildApi.hasErrors());		
		Api api=(Api) new TopLevelRamlModelBuilder().build(store, new CompositeResourceLoader(),"");
		TestCase.assertTrue(api.resources()[0].methods().get(0).annotations().get(0).value().equals("Hello"));
	}
	
	@SuppressWarnings("unchecked")
	public void test6(){
		ApiImpl model=new ApiImpl();
		model.setTitle("hello");
		SecuritySchemeImpl e = new SecuritySchemeImpl();
		e.setName("o2");
		e.settings().put("accessTokenUri","http");
		e.settings().put("authorizationUri","http");
		e.settings().put("authorizationGrants","password");
		e.setType("OAuth 2.0");
		e.settings().put("scopes", new String[]{"a","b"});
		model.securityDefinitions().add(e);
		String store = new RamlWriter().store(model);		
		RamlModelResult buildApi = new RamlModelBuilder().buildApi(store, "");	
		TestCase.assertTrue(!buildApi.hasErrors());		
		Api api=(Api) new TopLevelRamlModelBuilder().build(store, new CompositeResourceLoader(),"");
		TestCase.assertTrue(Arrays.equals((Object[])((List<Object>) api.securityDefinitions().get(0).settings().get("scopes")).toArray(), new Object[]{"a","b"}));
	}
	
}
