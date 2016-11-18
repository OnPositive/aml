package org.aml.swagger.io;

import org.aml.apimodel.Api;
import org.aml.apimodel.TopLevelModel;
import org.aml.swagger.writer.SwaggerWriter;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.raml.v2.internal.utils.StreamUtils;

import junit.framework.TestCase;

public class BasicTest extends TestCase{


	public void test0(){
		TopLevelModel build = TopLevelRamlModelBuilder.build(StreamUtils.toString(BasicTest.class.getResourceAsStream("/t1.raml")));
		String store = new SwaggerWriter().store((Api) build);
		System.out.println(store);
	}
	public void test1(){
		TopLevelModel build = TopLevelRamlModelBuilder.build(StreamUtils.toString(BasicTest.class.getResourceAsStream("/t2.raml")));
		String store = new SwaggerWriter().store((Api) build);
		System.out.println(store);
	}
	public void test2(){
		TopLevelModel build = TopLevelRamlModelBuilder.build(StreamUtils.toString(BasicTest.class.getResourceAsStream("/apigateway-aws-overlay.raml")));
		String store = new SwaggerWriter().store((Api) build);
		System.out.println(store);
	}
}
