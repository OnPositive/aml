package org.aml.typesystem.ramlreader;

import java.util.List;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.Description;
import org.aml.typesystem.meta.facets.FacetDeclaration;
import org.aml.typesystem.meta.restrictions.minmax.MaxProperties;
import org.aml.typesystem.meta.restrictions.minmax.Minimum;
import org.aml.typesystem.raml.model.TopLevelRaml;
import org.junit.Test;
import org.raml.v2.internal.impl.RamlBuilder;
import org.raml.v2.internal.utils.StreamUtils;
import org.raml.yagi.framework.nodes.Node;

import junit.framework.TestCase;

public class BasicTests extends TestCase{

	@Test
	public void test() {
		TopLevelRaml raml = parse("/t1.raml");
		AbstractType type = raml.types().getType("Z");
		List<IProperty> allProperties = type.toPropertiesView().allProperties();
		for (IProperty p:allProperties){
			TestCase.assertEquals(p.getDeclaredAt().name(),"Animal");
		}
		TestCase.assertTrue(allProperties.size()==2);
	}
	
	@Test
	public void test2() {
		TopLevelRaml raml = parse("/t2.raml");
		AbstractType type = raml.types().getType("Animal");
		
		TestCase.assertTrue(type.oneMeta(MaxProperties.class).value().intValue()==3);
	}

	@Test
	public void test3() {
		TopLevelRaml raml = parse("/t1.raml");
		AbstractType type = raml.types().getType("Z");
		AbstractType next = type.superTypes().iterator().next();
		TestCase.assertEquals(next.name(), "Animal");
		TestCase.assertEquals(next.subTypes().size(), 2);
		TestCase.assertEquals(type.superTypes().size(), 1);
		TestCase.assertEquals(next.superTypes().iterator().next(),BuiltIns.OBJECT);
	}
	
	@Test
	public void test4() {
		TopLevelRaml raml = parse("/t3.raml");
		AbstractType type = raml.types().getType("Animal");
		TestCase.assertEquals(type.oneMeta(Description.class).value(), "Small nice description");
	}
	
	@Test
	public void test5() {
		TopLevelRaml raml = parse("/t4.raml");
		AbstractType type = raml.types().getType("Animal");		
		TestCase.assertEquals(type.oneMeta(FacetDeclaration.class).getName(), "q?");
	}
	@Test
	public void test6() {
		TopLevelRaml raml = parse("/t1.raml");
		AbstractType type = raml.annotationTypes().getType("Hello");		
		TestCase.assertEquals(type.oneMeta(Minimum.class).value().intValue(), 5);
	}
	
	@Test
	public void test7() {
		TopLevelRaml raml = parse("/t5.raml");
		AbstractType type = raml.types().getType("Z");
		Set<Annotation> meta = type.meta(Annotation.class);
		TestCase.assertEquals(meta.size(), 1);
		for (Annotation a:meta){
			TestCase.assertEquals(a.value(), 7);
			TestCase.assertEquals(a.annotationType(), raml.annotationTypes().getType("Hello"));
		}		
	}
	
	@Test
	public void test8() {
		TopLevelRaml raml = parse("/t6.raml");
		AbstractType type = raml.types().getType("Z");
		Set<Annotation> meta = type.meta(Annotation.class);
		TestCase.assertEquals(meta.size(), 1);
		for (Annotation a:meta){
			TestCase.assertEquals(a.value(), 7);
			TestCase.assertEquals(a.annotationType(), raml.uses().get("t5").annotationTypes().getType("Hello"));
		}		
	}
	
	private TopLevelRaml parse(String res) {
		String string = StreamUtils.toString(BasicTests.class.getResourceAsStream(res));
		Node build = new RamlBuilder().build(string);
		TopLevelRaml raml = new TopLevelRamlModelBuilder().build(build);
		return raml;
	}

}
