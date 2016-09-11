package org.aml.raml2java;

import java.util.HashMap;

import org.aml.jav2raml.JavaGenerationConfig;
import org.aml.jav2raml.JavaWriter;
import org.aml.typesystem.ramlreader.TopLevelRamlImpl;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.junit.Test;
import org.raml.v2.api.loader.ClassPathResourceLoader;

import junit.framework.TestCase;

public class BasicTests extends CompilerTestCase{

	@Test
	public void test0(){
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTests.class.getResourceAsStream("/t1.raml"), new ClassPathResourceLoader(), "t1");
		JavaGenerationConfig cfg = new JavaGenerationConfig();
		cfg.setDefaultPackageName("org.aml.test");
		JavaWriter wr=new JavaWriter(cfg);		
		wr.write(build.types());
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(),"org.aml.test.Person","org.aml.test.Manager");
		TestCase.assertEquals(compileAndTest.size(), 2);
	}
}
