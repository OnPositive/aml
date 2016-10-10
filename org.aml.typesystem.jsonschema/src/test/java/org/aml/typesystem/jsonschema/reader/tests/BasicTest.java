package org.aml.typesystem.jsonschema.reader.tests;

import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.jsonschema.reader.JSONSchemaParser;
import org.aml.typesystem.jsonschema.reader.ObjectPropertiesResolver;
import org.aml.typesystem.jsonschema.reader.SimplePathReferenceResolver;
import org.json.JSONObject;
import org.json.JSONTokener;

import junit.framework.TestCase;

public class BasicTest extends TestCase{

	public void test0(){
		JSONObject ob = new JSONObject(new JSONTokener(BasicTest.class.getResourceAsStream("/simpleSchema.json")));
		JSONSchemaParser parser=new JSONSchemaParser(new ObjectPropertiesResolver(ob));
		parser.addType("A", ob);
		AbstractType type = parser.getResult().getType("A");
		Set<String> propertySet = type.propertySet();
		TestCase.assertTrue(propertySet.contains("age"));
		TestCase.assertTrue(propertySet.contains("firstName"));
		TestCase.assertTrue(propertySet.contains("lastName"));
		for (IProperty p:type.toPropertiesView().properties()){
			if (p.id().equals("lastName")){
				TestCase.assertTrue(p.isRequired());
				TestCase.assertEquals(p.description(), null);
			}
			if (p.id().equals("age")){
				TestCase.assertTrue(!p.isRequired());
				TestCase.assertEquals(p.description(), "Age in years");
			}
		}
	}
	
	public void test1(){
		JSONObject ob = new JSONObject(new JSONTokener(BasicTest.class.getResourceAsStream("/fs.json")));
		JSONSchemaParser parser=new JSONSchemaParser(new SimplePathReferenceResolver(ob));
		parser.addType("A", ob);
		AbstractType type = parser.getResult().getType("A");
		Set<String> propertySet = type.propertySet();
		TestCase.assertTrue(propertySet.contains("readonly"));
		TestCase.assertTrue(propertySet.contains("options"));
		TestCase.assertTrue(propertySet.contains("storage"));
		for (IProperty p:type.toPropertiesView().properties()){
			if (p.id().equals("storage")){
				TestCase.assertTrue(p.isRequired());
				TestCase.assertTrue(p.range().isUnion());
				TestCase.assertEquals(p.range().unionTypeFamily().size(),4);
			}
			if (p.id().equals("options")){
				TestCase.assertTrue(p.range().isArray());
				TestCase.assertTrue(p.range().componentType().isString());
			}
		}
	}
	
	public void test2(){
		JSONObject ob = new JSONObject(new JSONTokener(BasicTest.class.getResourceAsStream("/g.json")));
		ob=ob.getJSONObject("schemas");
		JSONSchemaParser parser=new JSONSchemaParser(new ObjectPropertiesResolver(ob));
		for (String s:JSONObject.getNames(ob)){
			parser.addType(s, ob.getJSONObject(s));
		}
		TestCase.assertEquals(parser.getResult().types().size(), 7);
		TestCase.assertTrue(parser.getResult().getType("Customer").isObject());
	}
}
