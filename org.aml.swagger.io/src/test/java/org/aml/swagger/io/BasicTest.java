package org.aml.swagger.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.aml.apimodel.Api;
import org.aml.registry.internal.LocalRegistry;
import org.aml.registry.model.ApiDescription;
import org.aml.registry.model.Registry;
import org.aml.registry.operations.LoadRegistry;
import org.aml.registry.operations.RegistryMaterialize;
import org.aml.swagger.writer.SwaggerWriter;
import org.aml.typesystem.ramlreader.TopLevelRamlImpl;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.raml.v2.api.loader.FileResourceLoader;
import org.raml.v2.internal.utils.StreamUtils;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.SwaggerDeserializationResult;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class BasicTest extends TestCase{


	public void test0(){
		assertCorrectTransformation("/t1.raml","/t1.yaml");		
	}
	public void test1(){
		assertCorrectTransformation("/t2.raml","/t2.yaml");		
	}
	public void test2(){
		assertCorrectTransformation("/apigateway-aws-overlay.raml","/apigateway-aws-overlay.yaml");
	}
	public void test3(){
		assertCorrectTransformation("/t3.raml","/t3.yaml");
	}
	public void test4(){
		assertCorrectTransformation("/t4.raml","/t4.yaml");
	}
	public void test5(){
		assertCorrectTransformation("/t5.raml","/t5.yaml");
	}
	public void test6(){
		assertCorrectTransformation("/t6.raml","/t6.yaml");
	}
	public void test7(){
		assertCorrectTransformation("/t7.raml","/t7.yaml");
	}
	public void test8(){
		assertCorrectTransformation("/t8.raml","/t8.yaml");
	}
	public void test9(){
		assertCorrectTransformation("/t9.raml","/t9.yaml");
	}
	public void test10(){
		assertCorrectTransformation("/test10.raml","/test10.yaml");
	}
	
	
	public void test11(){
		if (true){
			return;
		}
		Registry loadRegistry = new LoadRegistry(
				//"https://raw.githubusercontent.com/apiregistry/registry/master/oldApis.raml");
				"https://raw.githubusercontent.com/apiregistry/registry/gh-pages/registry-resolved.json").get();
		String property = System.getProperty("user.home");
		File fs=new File(property,".aml_registry");
		LocalRegistry reg = new LocalRegistry(fs.getAbsolutePath());
		Registry apply = new RegistryMaterialize(reg).apply(loadRegistry);
		int successfullCount=0;
		int errorCount=0;
		for (ApiDescription d:apply.getApis()){
			File localFileFor = reg.getLocalFileFor(d.getLocation());
			try {
				TopLevelRamlImpl build2 = new TopLevelRamlModelBuilder().build(new FileInputStream(localFileFor), new FileResourceLoader(localFileFor.getParentFile()), localFileFor.getName());
				if (!(build2 instanceof Api)){
					continue;
				}
				Api build = (Api) build2;				
				try{
				String store = new SwaggerWriter().store((Api) build);
				Swagger assertParsableSwagger = assertParsableSwagger(store);
				TestCase.assertTrue(build.allResources().size()==assertParsableSwagger.getPaths().size());
				successfullCount++;
				}catch (AssertionFailedError e) {
					System.out.println(d.getName()+":"+d.getLocation()+" failed");
				}
				catch (Exception e) {
					System.out.println(d.getName()+":"+d.getLocation()+" errored");
					errorCount++;
				}
			} catch (FileNotFoundException e) {
				throw new IllegalStateException(e);
			}			
		}
		System.out.println("Successful:"+successfullCount+" of "+apply.getApis().size()+" Errored:"+errorCount);
		
	}
	
	public void test12(){
		assertCorrectTransformation("/t11.raml","/t11.yaml");
	}
	public void test13(){
		assertCorrectTransformation("/t12.raml","/t12.yaml");
	}
	public void test14(){
		assertCorrectTransformation("/t13.raml","/t13.yaml");
	}
	protected void assertCorrectTransformation(String resource,String testAgainst){
		Api build = (Api) TopLevelRamlModelBuilder.build(StreamUtils.toString(BasicTest.class.getResourceAsStream(resource)));
		String store = new SwaggerWriter().store((Api) build);
		Swagger assertParsableSwagger = assertParsableSwagger(store);
		TestCase.assertTrue(build.allResources().size()==assertParsableSwagger.getPaths().size());
		if (testAgainst!=null){
			String string = StreamUtils.toString(BasicTest.class.getResourceAsStream(testAgainst));
			TestCase.assertEquals(cleanWhiteSpace(string),cleanWhiteSpace(store));
		}		
	}
	
	private String cleanWhiteSpace(String string) {
		StringBuilder bld=new StringBuilder();
		for (int i=0;i<string.length();i++){
			char c=string.charAt(i);
			if (!Character.isWhitespace(c)){
				bld.append(c);
			}
		}
		return bld.toString();
	}
	protected Swagger assertParsableSwagger(String content){
		SwaggerDeserializationResult info = new SwaggerParser().readWithInfo(content);
		if (info.getMessages().size()>0){
			TestCase.assertEquals(info.getMessages().toString(), "");
		}
		return info.getSwagger();
	}
}
