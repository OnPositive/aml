package org.aml.raml2java;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.aml.java.mapping.Package;
import org.aml.raml2java.JavaGenerationConfig.MultipleInheritanceStrategy;
import org.aml.typesystem.ramlreader.TopLevelRamlImpl;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.junit.Test;
import org.raml.v2.api.loader.ClassPathResourceLoader;

import junit.framework.TestCase;

public class BasicTest extends CompilerTestCase {

	
	
	@Test
	public void test0() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at0.raml"),
				new ClassPathResourceLoader(), "at0.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Author");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.Author");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 2);
	}

	@Test
	public void test1() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at1.raml"),
				new ClassPathResourceLoader(), "at1.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Author");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.Author");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 2);
		for (Method m : class1.getDeclaredMethods()) {
			TestCase.assertTrue(m.getDefaultValue().equals(""));
		}
	}

	@Test
	public void test2() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at2.raml"),
				new ClassPathResourceLoader(), "at2.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Author");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.Author");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 2);
		for (Method m : class1.getDeclaredMethods()) {
			if (m.getName().equals("Name")) {
				TestCase.assertTrue(m.getDefaultValue().equals("Name"));
			}
		}
	}

	@Test
	public void test3() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at3.raml"),
				new ClassPathResourceLoader(), "at3.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Author");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.Author");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 2);
		for (Method m : class1.getDeclaredMethods()) {
			if (m.getName().equals("Name")) {
				TestCase.assertTrue(m.getDefaultValue().equals("Name"));
			}
			if (m.getName().equals("books")) {
				TestCase.assertTrue(
						m.getReturnType().isArray() && m.getReturnType().getComponentType() == String.class);
				TestCase.assertTrue(m.getDefaultValue() != null);
			}
		}
	}

	@Test
	public void test4() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at4.raml"),
				new ClassPathResourceLoader(), "at4.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Author");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.Author");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 2);
		for (Method m : class1.getDeclaredMethods()) {
			if (m.getName().equals("Name")) {
				TestCase.assertTrue(m.getDefaultValue().equals("Name"));
			}
			if (m.getName().equals("books")) {
				TestCase.assertTrue(
						m.getReturnType().isArray() && m.getReturnType().getComponentType() == String.class);
				TestCase.assertTrue(m.getDefaultValue() == null);
			}
		}
	}

	@Test
	public void test5() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at5.raml"),
				new ClassPathResourceLoader(), "at5.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.HumanName");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.HumanName");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 1);
		TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	
	@Test
	public void test6() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at6.raml"),
				new ClassPathResourceLoader(), "at6.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.ImportantItem");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.ImportantItem");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 0);
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	
	@Test
	public void test7() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at7.raml"),
				new ClassPathResourceLoader(), "at7.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.AuthorWithKind");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.AuthorWithKind");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 1);
		try {
			Method method = class1.getMethod("kind");
			TestCase.assertTrue(method.getReturnType().isEnum());
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	
	@Test
	public void test8() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at8.raml"),
				new ClassPathResourceLoader(), "at8.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.AuthorWithKind");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.AuthorWithKind");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 1);
		try {
			Method method = class1.getMethod("kind");
			TestCase.assertTrue(method.getReturnType().isEnum());
			TestCase.assertTrue(method.getReturnType().getSimpleName().equals("Kind"));
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	
	@Test
	public void test9() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at9.raml"),
				new ClassPathResourceLoader(), "at9.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.KindAnnotated");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.KindAnnotated");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 1);
		try {
			Method method = class1.getMethod("value");
			TestCase.assertTrue(method.getReturnType().isEnum());
			TestCase.assertTrue(method.getReturnType().getSimpleName().equals("Kind"));
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	@Test
	public void test10() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at10.raml"),
				new ClassPathResourceLoader(), "at10.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Kind");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.Kind");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 1);
		try {
			Method method = class1.getMethod("value");
			TestCase.assertTrue(method.getReturnType().isEnum());
			TestCase.assertTrue(method.getReturnType().getSimpleName().equals("Kind2"));
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	
	@Test
	public void test11() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/at11.raml"),
				new ClassPathResourceLoader(), "at11.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.KindAnnotated");
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get("org.aml.test.KindAnnotated");
		TestCase.assertTrue(class1.isAnnotation());
		TestCase.assertTrue(class1.getDeclaredMethods().length == 1);
		try {
			Method method = class1.getMethod("value");
			TestCase.assertTrue(method.getReturnType().isEnum());
			TestCase.assertTrue(method.getReturnType().getSimpleName().equals("Kind"));
			TestCase.assertTrue(method.getDefaultValue()!=null);
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	
	@Test
	public void test12() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t1.raml"),
				new ClassPathResourceLoader(), "t1.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==4);
			beanInfo = Introspector.getBeanInfo(class1.getSuperclass());
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==3);
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	@Test
	public void test13() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t2.raml"),
				new ClassPathResourceLoader(), "t2.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==6);
			beanInfo = Introspector.getBeanInfo(class1.getSuperclass());
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==5);
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	@Test
	public void test14() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t3.raml"),
				new ClassPathResourceLoader(), "t3.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==7);
			beanInfo = Introspector.getBeanInfo(class1.getSuperclass());
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==5);
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	@Test
	public void test15() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t4.raml"),
				new ClassPathResourceLoader(), "t4.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==7);			
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}	
	@Test
	public void test16() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t5.raml"),
				new ClassPathResourceLoader(), "t5.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==7);			
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	@Test
	public void test17() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t5.raml"),
				new ClassPathResourceLoader(), "t5.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==7);
			beanInfo = Introspector.getBeanInfo(class1.getSuperclass());
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==5);
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}	
	
	@Test
	public void test18() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t6.raml"),
				new ClassPathResourceLoader(), "t6.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==7);
			beanInfo = Introspector.getBeanInfo(class1.getSuperclass());
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==5);
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	
	@Test
	public void test19() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t7.raml"),
				new ClassPathResourceLoader(), "t7.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==3);
			boolean hasInnerType=false;
			for (PropertyDescriptor d:beanInfo.getPropertyDescriptors()){
				if (d.getName().equals("innerType")){
					hasInnerType=true;
					Class<?> propertyType = d.getPropertyType();
					BeanInfo b=Introspector.getBeanInfo(propertyType);
					TestCase.assertEquals(b.getPropertyDescriptors().length, 3);
				}
			}
			TestCase.assertTrue(hasInnerType);
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	
	@Test
	public void test20() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t8.raml"),
				new ClassPathResourceLoader(), "t8.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==3);
			boolean hasInnerType=false;
			for (PropertyDescriptor d:beanInfo.getPropertyDescriptors()){
				if (d.getName().equals("innerType")){
					hasInnerType=true;
					Class<?> propertyType = d.getPropertyType();
					BeanInfo b=Introspector.getBeanInfo(propertyType);
					TestCase.assertEquals(b.getPropertyDescriptors().length, 4);
				}
			}
			TestCase.assertTrue(hasInnerType);
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	
	@Test
	public void test21() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t9.raml"),
				new ClassPathResourceLoader(), "t9.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==3);
			boolean hasInnerType=false;
			for (PropertyDescriptor d:beanInfo.getPropertyDescriptors()){
				if (d.getName().equals("innerType")){
					hasInnerType=true;
					Class<?> propertyType = d.getPropertyType();
					BeanInfo b=Introspector.getBeanInfo(propertyType);
					TestCase.assertEquals(b.getPropertyDescriptors().length, 3);
				}
			}
			TestCase.assertTrue(hasInnerType);
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}
	
	@Test
	public void test22() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t9.raml"),
				new ClassPathResourceLoader(), "t9.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(class1);
			TestCase.assertTrue(beanInfo.getPropertyDescriptors().length==3);
			boolean hasInnerType=false;
			for (PropertyDescriptor d:beanInfo.getPropertyDescriptors()){
				if (d.getName().equals("innerType")){
					hasInnerType=true;
					Class<?> propertyType = d.getPropertyType();
					BeanInfo b=Introspector.getBeanInfo(propertyType);
					TestCase.assertEquals(b.getPropertyDescriptors().length, 3);
				}
			}
			TestCase.assertTrue(hasInnerType);
		} catch (IntrospectionException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getDeclaredMethods()[0].getName().equals("value"));
	}

//	
	@Test
	public void test23() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t10.raml"),
				new ClassPathResourceLoader(), "t10.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			Method declaredMethod = class1.getDeclaredMethod("getName");
			Annotation[] annotations = declaredMethod.getAnnotations();
			TestCase.assertTrue(annotations.length==1);
			TestCase.assertTrue(annotations[0].annotationType().getSimpleName().equals("Important"));
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		}

	}
	
	@Test
	public void test24() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t11.raml"),
				new ClassPathResourceLoader(), "t11.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			Method declaredMethod = class1.getDeclaredMethod("getName");
			Annotation[] annotations = declaredMethod.getAnnotations();
			TestCase.assertTrue(annotations.length==1);
			TestCase.assertTrue(annotations[0].annotationType().getSimpleName().equals("Label"));
			
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		}

	}
	
	@Test
	public void test25() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t12.raml"),
				new ClassPathResourceLoader(), "t12.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			Method declaredMethod = class1.getDeclaredMethod("getName");
			Annotation[] annotations = declaredMethod.getAnnotations();
			TestCase.assertTrue(annotations.length==1);
			TestCase.assertTrue(annotations[0].annotationType().getSimpleName().equals("Order"));
			Object invoke = annotations[0].getClass().getMethod("value").invoke(annotations[0]);
			TestCase.assertEquals(invoke, 2);
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalAccessException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		} catch (InvocationTargetException e) {
			TestCase.assertTrue(false);
		}

	}
	
	@Test
	public void test26() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t13.raml"),
				new ClassPathResourceLoader(), "t13.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			Method declaredMethod = class1.getDeclaredMethod("getName");
			Annotation[] annotations = declaredMethod.getAnnotations();
			TestCase.assertTrue(annotations.length==1);
			TestCase.assertTrue(annotations[0].annotationType().getSimpleName().equals("Skip"));
			Object invoke = annotations[0].getClass().getMethod("value").invoke(annotations[0]);
			TestCase.assertEquals(invoke, true);
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalAccessException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		} catch (InvocationTargetException e) {
			TestCase.assertTrue(false);
		}

	}
	@Test
	public void test27() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t14.raml"),
				new ClassPathResourceLoader(), "t14.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			Method declaredMethod = class1.getDeclaredMethod("getName");
			Annotation[] annotations = declaredMethod.getAnnotations();
			TestCase.assertTrue(annotations.length==1);
			TestCase.assertTrue(annotations[0].annotationType().getSimpleName().equals("Version"));
			Object invoke = annotations[0].getClass().getMethod("majour").invoke(annotations[0]);
			TestCase.assertEquals(invoke, 3.0);
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalAccessException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		} catch (InvocationTargetException e) {
			TestCase.assertTrue(false);
		}

	}
	
	@Test
	public void test28() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t15.raml"),
				new ClassPathResourceLoader(), "t15.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			Method declaredMethod = class1.getDeclaredMethod("getName");
			Annotation[] annotations = declaredMethod.getAnnotations();
			TestCase.assertTrue(annotations.length==1);
			TestCase.assertTrue(annotations[0].annotationType().getSimpleName().equals("Version"));
			double[] invoke = (double[]) annotations[0].getClass().getMethod("value").invoke(annotations[0]);
			TestCase.assertEquals(invoke[0], 3.0);
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalAccessException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		} catch (InvocationTargetException e) {
			TestCase.assertTrue(false);
		}

	}
	
	@Test
	public void test29() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t16.raml"),
				new ClassPathResourceLoader(), "t16.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Manager");
		Class<?> class1 = compileAndTest.get("org.aml.test.Manager");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			Method declaredMethod = class1.getDeclaredMethod("getName");
			Annotation[] annotations = declaredMethod.getAnnotations();
			TestCase.assertTrue(annotations.length==1);
			TestCase.assertTrue(annotations[0].annotationType().getSimpleName().equals("Version"));
			double[] invoke = (double[]) annotations[0].getClass().getMethod("q").invoke(annotations[0]);
			TestCase.assertEquals(invoke[0], 3.0);
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalAccessException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		} catch (InvocationTargetException e) {
			TestCase.assertTrue(false);
		}

	}
	
	@Test
	public void test30() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t17.raml"),
				new ClassPathResourceLoader(), "t17.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.OkStatus");
		Class<?> class1 = compileAndTest.get("org.aml.test.OkStatus");
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			Method declaredMethod = class1.getDeclaredMethod("getGeo");
			Class<?> returnType = declaredMethod.getReturnType();
			System.out.println(returnType);
			try {
				Field declaredField = returnType.getDeclaredField("address");
				String simpleName = declaredField.getType().getSimpleName();
				TestCase.assertTrue(simpleName.equals("OkStatusgeoaddress"));				
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			TestCase.assertTrue(false);
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}

	}
	@Test
	public void test31() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t18.raml"),
				new ClassPathResourceLoader(), "t18.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.CatOrDog");
		Class<?> class1 = compileAndTest.get("org.aml.test.CatOrDog");
		try{
		class1.getDeclaredMethod("getCat");
		class1.getDeclaredMethod("getDog");
		}catch (Exception e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}

	}
	@Test
	public void test32() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t19.raml"),
				new ClassPathResourceLoader(), "t19.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.HasPet");
		Class<?> class1 = compileAndTest.get("org.aml.test.HasPet");
		try{
		class1=class1.getDeclaredMethod("getPet").getReturnType();	
		class1.getDeclaredMethod("getCat");
		class1.getDeclaredMethod("getDog");
		}catch (Exception e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}

	}
	@Test
	public void test33() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t20.raml"),
				new ClassPathResourceLoader(), "t20.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.HasPet");
		Class<?> class1 = compileAndTest.get("org.aml.test.HasPet");
		try{
		TestCase.assertEquals(class1.getAnnotations().length,1);	
		class1=class1.getDeclaredMethod("getPet").getReturnType();
		TestCase.assertEquals(class1.getAnnotations().length,1);	
		class1.getDeclaredMethod("getCat");
		class1.getDeclaredMethod("getDog");
		}catch (Exception e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}

	}
	
	@Test
	public void test34() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t21.raml"),
				new ClassPathResourceLoader(), "t21.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Person");
		Class<?> class1 = compileAndTest.get("org.aml.test.Person");
		try{
		class1=class1.getDeclaredMethod("get__").getReturnType();
		TestCase.assertTrue(Map.class.isAssignableFrom(class1));	
		}catch (Exception e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {
			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}

	}
	

	@Test
	public void test35() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t35.raml"),
				new ClassPathResourceLoader(), "t21.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Person");
		Class<?> class1 = compileAndTest.get("org.aml.test.Person");
		try{
		Pattern annotation = class1.getMethod("getName").getAnnotation(Pattern.class);	
		TestCase.assertTrue(annotation!=null);
		Size annotation1 = class1.getMethod("getCell").getAnnotation(Size.class);	
		TestCase.assertTrue(annotation1!=null);
		TestCase.assertTrue(annotation1.max()==8);
		
		DecimalMax annotation2 = class1.getMethod("getAge").getAnnotation(DecimalMax.class);	
		TestCase.assertTrue(annotation1!=null);
		TestCase.assertTrue(annotation2.value().equals("140.0"));
		}catch (Exception e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
	}
	
	@Test
	public void test36() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t29.raml"),
				new ClassPathResourceLoader(), "t29.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "com.test.annotations2.Person");
		Class<?> class1 = compileAndTest.get("com.test.annotations2.Person");
		TestCase.assertTrue(class1.getAnnotation(Package.class)==null);
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
	}
	
	@Test
	public void test37() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t29.raml"),
				new ClassPathResourceLoader(), "t29.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
		wr.getConfig().getAnnotationConfig().addIdToSkipDefinition("Hello");
		wr.getConfig().getAnnotationConfig().addIdToSkipReference("Hello");
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "com.test.annotations2.Person");
		Class<?> class1 = compileAndTest.get("com.test.annotations2.Person");
		TestCase.assertTrue(class1.getAnnotation(Package.class)==null);
		try {
			Method method = class1.getMethod("getName");
			for (Annotation a:method.getAnnotations()){
				if (a.annotationType().getSimpleName().equals("Hello")){
					TestCase.assertTrue(false);
				}
			}
		} catch (NoSuchMethodException | SecurityException e1) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
	}
	
	@Test
	public void test38() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t37.raml"),
				new ClassPathResourceLoader(), "t37.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.MyType");
		Class<?> class1 = compileAndTest.get("org.aml.test.MyType");
		try {
			Method method = class1.getMethod("getQ");
			TestCase.assertTrue(method.getReturnType()==long.class);
			method = class1.getMethod("getQ1");
			TestCase.assertTrue(method.getReturnType()==int.class);
			method = class1.getMethod("getQ2");
			TestCase.assertTrue(method.getReturnType()==float.class);
			method = class1.getMethod("getQ3");
			TestCase.assertTrue(method.getReturnType()==byte.class);
			method = class1.getMethod("getQ4");
			TestCase.assertTrue(method.getReturnType()==long.class);
		} catch (NoSuchMethodException | SecurityException e1) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
	}
	
	@Test
	public void test39() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t37.raml"),
				new ClassPathResourceLoader(), "t37.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.AnotherType");
		Class<?> class1 = compileAndTest.get("org.aml.test.AnotherType");
		try {
			Method method = class1.getMethod("getZ");
			TestCase.assertTrue(method.getReturnType()==long.class);
			method = class1.getMethod("getV");
			TestCase.assertTrue(method.getReturnType()==float.class);
			
		} catch (NoSuchMethodException | SecurityException e1) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
	}
	@Test
	public void test40() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t45.raml"),
				new ClassPathResourceLoader(), "/t45.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.TestTypeWithDefaults");
		Class<?> class1 = compileAndTest.get("org.aml.test.TestTypeWithDefaults");
		try {
			Object newInstance = class1.newInstance();
			Method method = class1.getMethod("getZ");
			Object vl=method.invoke(newInstance);
			TestCase.assertTrue(vl.equals(2.0f));
			method = class1.getMethod("getQ");
			vl=method.invoke(newInstance);
			TestCase.assertTrue(vl.equals("Hello"));
			method = class1.getMethod("getV");
			vl=method.invoke(newInstance);
			TestCase.assertTrue(vl.equals(true));
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e1) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		} catch (InvocationTargetException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
	}
	
	@Test
	public void test41() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t40.raml"),
				new ClassPathResourceLoader(), "/t40.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.TestTypeWithDefaults");
		Class<?> class1 = compileAndTest.get("org.aml.test.TestTypeWithDefaults");
		try {
			Object newInstance = class1.newInstance();
			Method method = class1.getMethod("getZ");
			Object vl=method.invoke(newInstance);
			TestCase.assertTrue(vl.equals(2.0));
			method = class1.getMethod("getQ");
			vl=method.invoke(newInstance);
			TestCase.assertTrue(vl.equals("Hello"));
			method = class1.getMethod("getV");
			vl=method.invoke(newInstance);
			TestCase.assertTrue(vl.equals(true));
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e1) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		} catch (InvocationTargetException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
	}
	class X implements Cloneable{
		
		@Override
		public X clone()  {
			try {
				return (X) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	@Test
	public void test42() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t41.raml"),
				new ClassPathResourceLoader(), "/t41.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setGenerateHashCodeAndEquals(true);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Person");
		Class<?> class1 = compileAndTest.get("org.aml.test.Person");
		try {
			Object newInstance = class1.newInstance();
			Object newInstance2 = class1.newInstance();
			TestCase.assertTrue(newInstance.equals(newInstance2));
			TestCase.assertEquals(newInstance.hashCode(), newInstance2.hashCode());
		} catch (SecurityException | InstantiationException | IllegalAccessException e1) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
	}
	@Test
	public void test43() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t42.raml"),
				new ClassPathResourceLoader(), "t42.raml");
		JavaWriter wr = new JavaWriter();
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.Person");
		Class<?> class1 = compileAndTest.get("org.aml.test.Person");
		try {
			Object newInstance = class1.newInstance();
			try {
				Method m=class1.getMethod("withName", String.class);
				TestCase.assertTrue(m.getReturnType()==class1);
			} catch (NoSuchMethodException e) {
				TestCase.assertTrue(false);
			}
			Object newInstance2 = class1.newInstance();
			TestCase.assertTrue(newInstance instanceof Serializable);
			TestCase.assertTrue(newInstance instanceof Cloneable);
			TestCase.assertTrue(newInstance instanceof Comparable);
			TestCase.assertTrue(newInstance.equals(newInstance2));
			TestCase.assertEquals(newInstance.hashCode(), newInstance2.hashCode());
		} catch (SecurityException | InstantiationException | IllegalAccessException e1) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
	}
	
	@Test
	public void test44() {
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/t46.raml"),
				new ClassPathResourceLoader(), "t46.raml");
		JavaWriter wr = new JavaWriter();
		wr.getConfig().setJacksonSupport(true);
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), "org.aml.test.H");
		Class<?> class1 = compileAndTest.get("org.aml.test.H");
		try {
			Object newInstance = class1.newInstance();
			try {
				Method m=class1.getMethod("getSalesSummary");
				
			} catch (NoSuchMethodException e) {
				TestCase.assertTrue(false);
			}
			try {
				Method m=class1.getMethod("getSaleDetails");				
			} catch (NoSuchMethodException e) {
				TestCase.assertTrue(false);
			}
//			Object newInstance2 = class1.newInstance();
//			TestCase.assertTrue(newInstance instanceof Serializable);
//			TestCase.assertTrue(newInstance instanceof Cloneable);
//			TestCase.assertTrue(newInstance instanceof Comparable);
//			TestCase.assertTrue(newInstance.equals(newInstance2));
//			TestCase.assertEquals(newInstance.hashCode(), newInstance2.hashCode());
		} catch (SecurityException | InstantiationException | IllegalAccessException e1) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
		//TestCase.assertTrue(class1.getSuperclass().getSimpleName().equals("Person"));
		try {			
		} catch (SecurityException e) {
			TestCase.assertTrue(false);
		} catch (IllegalArgumentException e) {
			TestCase.assertTrue(false);
		}
	}
}

