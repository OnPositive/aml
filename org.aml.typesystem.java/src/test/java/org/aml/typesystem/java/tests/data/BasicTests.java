package org.aml.typesystem.java.tests.data;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.java.JavaTypeBuilder;
import org.aml.typesystem.reflection.ReflectionType;
import org.junit.Test;

public class BasicTests {

	@Test
	public void test() {
		JavaTypeBuilder javaTypeBuilder = new JavaTypeBuilder();
		AbstractType type = javaTypeBuilder.getType(new ReflectionType(Person.class));
		System.out.println(type);
	}

}
