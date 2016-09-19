package org.aml.raml2java;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.commons.beanutils.PropertyUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.raml.v2.internal.utils.StreamUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JacksonUtils;
import com.google.gson.Gson;

import junit.framework.TestCase;

public class SerializationTests extends CompilerTestCase {

	@Test
	public void test0() {
		assertValue("t1.raml", "Manager", "/s0.json", "/s0.json", "/s0.xml");
	}

	@Test
	public void test1() {
		assertValue("t4.raml", "Manager", "/s1.json", "/s1.json", "/s1.xml");
	}

	@Test
	public void test2() {
		assertValue("t20.raml", "HasPet", "/s2.json", "/s2s.json", null);
	}

	@Test
	public void test3() {
		assertValue("t20.raml", "HasPet", "/s3.json", "/s3s.json", null);
	}
	
	@Test
	public void test4() {
		assertValue("t21.raml", "Person", "/s4.json", "/s4s.json", null);
	}
	@Test
	public void test5() {
		assertValue("t23.raml", "Next", "/s5.json", "/s5.json", "/s5.xml");
	}
	@Test
	public void test6() {
		assertValue("t24.raml", "Extend", "/s6.json", "/s6.json",null);
	}
	
	@Test
	public void test7() {
		assertValue("t25.raml", "Person", "/s7.json", "/s7.json","/s7.xml");
	}
	@Test
	public void test8() {
		assertValue("t26.raml", "Person", "/s7.json", "/s7.json","/s8.xml");
	}
	@Test
	public void test9() {
		assertValue("t27.raml", "Manager", "/s0.json", "/s0.json","/s9.xml");
	}
	@Test
	public void test10() {
		assertValue("t28.raml", "Manager", "/s0.json", "/s0.json","/s10.xml");
	}
	
	
	@Test
	public void test11() {
		assertValue("t29.raml", "com.test.annotations.Manager", "/s0.json", "/s0.json","/s0.xml");
	}
	@Test
	public void test12() {
		assertValue("t30.raml", "com.test.annotations.Manager", "/s0.json", "/s0.json","/s0.xml");
	}
	@Test
	public void test13() {
		assertValue("t31.raml", "Manager", "/s0.json", "/s0.json","/s0.xml");
	}
	@Test
	public void test14() {
		assertValue("t32.raml", "JsonType", "/s8.json", "/s8.json",null);
	}
	@Test
	public void test15() {
		assertValue("t33.raml", "com.test.annotations.JsonType", "/s8.json", "/s8.json",null);
	}
	
	@Test
	public void test16() {
		assertValue("t33.raml", "com.test.annotations.XSDType", "/s19.json", "/s19.json","/s19.xml");
	}
	@Test
	public void test17() {
		Class<?> assertValue = assertValue("t34.raml", "com.test.annotations.Manager", "/s0.json", "/s0.json","/s0.xml");
		try {
			TestCase.assertTrue(List.class.isAssignableFrom(assertValue.getDeclaredField("manages").getType()));
		} catch (NoSuchFieldException | SecurityException e) {
			TestCase.assertFalse(true);
		}
	}
	@Test
	public void test18() {
		assertValue("t22.raml", "Person", "/s18.json", "/s18.json", "/s18.xml");
	}
	private Class<?> assertValue(String ramlPath, String className, String jsonPath, String plainJsonPath, String xmlPath) {
		Class<?> clazz = compileAndLoadClass(ramlPath, className, true);
		Object object = loadObjectGson(clazz, jsonPath);
		assertAgainstJSON(object, plainJsonPath);
		String json = new Gson().toJson(object);
		Object fromJson = new Gson().fromJson(json, clazz);
		assertAgainstJSON(fromJson, plainJsonPath);
		object = loadObjectJackson(clazz, jsonPath);
		assertAgainstJSON(object, plainJsonPath);
		try {
			String writeValueAsString = new ObjectMapper().writeValueAsString(object);
			object = loadObjectJacksonFromString(clazz, writeValueAsString);
			assertAgainstJSON(object, plainJsonPath);
		} catch (JsonProcessingException e) {
			TestCase.assertFalse(true);
		}		
		if (xmlPath != null) {
			object = loadObjectJAXB(xmlPath, clazz);
			assertAgainstJSON(object, plainJsonPath);
		}
		return clazz;
	}

	public static Object getProperty(Object obj, String name) {
		try {
			return PropertyUtils.getProperty(obj, name);
		} catch (IllegalAccessException e) {
			TestCase.assertTrue(false);
		} catch (InvocationTargetException e) {
			TestCase.assertTrue(false);
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		}
		return null;
	}

	
	public Object loadObjectGson(Class<?> cl, String jsonPath) {
		Object fromJson = new Gson()
				.fromJson(new InputStreamReader(SerializationTests.class.getResourceAsStream(jsonPath)), cl);
		return fromJson;
	}

	public Object loadObjectJackson(Class<?> clazz, String jsonPath) {
		try {
			return JacksonUtils.getReader().forType(clazz)
					.readValue(new InputStreamReader(SerializationTests.class.getResourceAsStream(jsonPath)));
		} catch (JsonProcessingException e) {
			TestCase.assertTrue(false);
		} catch (IOException e) {
			TestCase.assertTrue(false);
		}
		return null;
	}
	
	public Object loadObjectJacksonFromString(Class<?> clazz, String value) {
		try {
			return JacksonUtils.getReader().forType(clazz)
					.readValue(value);
		} catch (JsonProcessingException e) {
			TestCase.assertTrue(false);
		} catch (IOException e) {
			TestCase.assertTrue(false);
		}
		return null;
	}

	static void assertAgainstJSON(Object obj, String jsonPath) {
		JSONObject ob = new JSONObject(StreamUtils.toString(SerializationTests.class.getResourceAsStream(jsonPath)));
		assertObject(ob, obj);
	}

	private static void assertObject(Object json, Object obj) {

		if (json instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) json;
			jsonObj.keySet().forEach(x -> {
				Object property = getProperty(obj, x);
				Object treeNode = jsonObj.get(x);
				assertObject(treeNode, property);
			});
		} else if (json instanceof JSONArray) {
			JSONArray arr = (JSONArray) json;
			if (obj instanceof List){
				List<?>l=(List<?>) obj;
				for (int i = 0; i < arr.length(); i++) {
					Object val = l.get(i);
					Object treeVal = arr.get(i);
					assertObject(treeVal, val);
				}
			}
			else
			for (int i = 0; i < arr.length(); i++) {
				Object val = Array.get(obj, i);
				Object treeVal = arr.get(i);
				assertObject(treeVal, val);
			}
		} else {
			if (obj instanceof Enum<?>) {
				Enum<?> e = (Enum<?>) obj;
				TestCase.assertEquals(json, e.name());
			} else
			{
				Object tv=obj;
				if (obj instanceof BigInteger){
					tv=((BigInteger) obj).intValue();
				}
				if (obj instanceof BigDecimal){
					tv=((BigDecimal) obj).doubleValue();
				}
				if (obj instanceof byte[]){
					tv=Base64.getEncoder().encodeToString((byte[]) obj);
				}
				TestCase.assertEquals(json, tv);
			}
		}

	}

	public Object loadObjectJAXB(String xmlPath, Class<?> clazz) {
		return JAXB.unmarshal(new InputStreamReader(SerializationTests.class.getResourceAsStream(xmlPath)), clazz);
	}

	public static void assertProperty(Object obj, String pName, Object value) {
		try {
			TestCase.assertEquals(PropertyUtils.getProperty(obj, pName), value);
		} catch (IllegalAccessException e) {
			TestCase.assertFalse(true);
		} catch (InvocationTargetException e) {
			TestCase.assertFalse(true);
		} catch (NoSuchMethodException e) {
			TestCase.assertFalse(true);
		}
	}
}