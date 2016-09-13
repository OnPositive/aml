package org.aml.raml2java;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

import com.google.gson.Gson;

import junit.framework.TestCase;

public class SerializationTests extends CompilerTestCase {

	@Test
	public void test0(){
		Object val=loadObjectGson("t1.raml", "Manager","/s0.json");
		assertProperty(val, "name", "Pavel");
		assertProperty(val, "lastName", "Petrochenko");
		Object[] manages=(Object[]) getProperty(val, "manages");
		assertProperty(manages[0],"name","Ada");
	}
	
	public Object getProperty(Object obj,String name){
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
	
	public  Object loadObjectGson(String ramlPath,String className,String jsonPath){
		Class<?> clazz=compileAndLoadClass(ramlPath,className);
		Object fromJson = new Gson().fromJson(new InputStreamReader(SerializationTests.class.getResourceAsStream(jsonPath)), clazz);
		return fromJson;
	}
	
	
	
	public static void assertProperty(Object obj, String pName,Object value){
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
