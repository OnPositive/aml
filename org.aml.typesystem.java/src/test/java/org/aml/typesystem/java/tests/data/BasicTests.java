package org.aml.typesystem.java.tests.data;

import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.beans.PropertyViewImpl;
import org.aml.typesystem.java.BeanPropertiesFilter;
import org.aml.typesystem.java.JavaTypeBuilder;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.reflection.ReflectionType;
import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class BasicTests {

	@Test
	public void test() {
		JavaTypeBuilder javaTypeBuilder = new JavaTypeBuilder();
		AbstractType type = javaTypeBuilder.getType(new ReflectionType(Person.class));
		Set<String> propertySet = type.propertySet();
		Assert.assertTrue(propertySet.contains("name"));
		Assert.assertTrue(propertySet.size()==3);
	}

	@Test
	public void test1() {
		JavaTypeBuilder javaTypeBuilder = new JavaTypeBuilder();
		AbstractType type = javaTypeBuilder.getType(new ReflectionType(Manager.class));
		Set<String> propertySet = type.propertySet();
		Assert.assertTrue(propertySet.contains("name"));
		Assert.assertTrue(propertySet.size()==5);
		IProperty property = new PropertyViewImpl(type).getProperty("managed");
		AbstractType range = property.range();
		ComponentShouldBeOfType meta = range.oneMeta(ComponentShouldBeOfType.class);
		junit.framework.Assert.assertTrue(meta.range().name().equals("Person"));
	}
	
	@Test
	public void test2() {
		JavaTypeBuilder javaTypeBuilder = new JavaTypeBuilder();
		AbstractType type = javaTypeBuilder.getType(new ReflectionType(NestedArray.class));
		IProperty property = new PropertyViewImpl(type).getProperty("items");
		AbstractType range = property.range();
		ComponentShouldBeOfType meta = range.oneMeta(ComponentShouldBeOfType.class);
		meta=meta.range().oneMeta(ComponentShouldBeOfType.class);
		junit.framework.Assert.assertTrue(meta.range().name().equals("double"));
	}
	
	@Test
	public void test3() {
		JavaTypeBuilder javaTypeBuilder = new JavaTypeBuilder();
		AbstractType type = javaTypeBuilder.getType(new ReflectionType(ClassWithCollection.class));
		IProperty property = new PropertyViewImpl(type).getProperty("teamMembers");
		AbstractType range = property.range();
		ComponentShouldBeOfType meta = range.oneMeta(ComponentShouldBeOfType.class);
		junit.framework.Assert.assertTrue(meta.range().name().equals("Person"));
	}
	
	@Test
	public void test4() {
		JavaTypeBuilder javaTypeBuilder = new JavaTypeBuilder();
		javaTypeBuilder.getConfig().setMemberFilter(new BeanPropertiesFilter());
		AbstractType type = javaTypeBuilder.getType(new ReflectionType(PersonWithProps.class));
		Set<String> propertySet = type.propertySet();
		Assert.assertTrue(propertySet.contains("name"));
		Assert.assertTrue(propertySet.contains("lastName"));
		Assert.assertTrue(propertySet.size()==2);
	}
}