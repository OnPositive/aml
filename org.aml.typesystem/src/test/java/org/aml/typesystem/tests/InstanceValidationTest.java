package org.aml.typesystem.tests;

import java.util.Arrays;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.Status;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.meta.restrictions.Enum;
import org.aml.typesystem.meta.restrictions.Pattern;
import org.aml.typesystem.meta.restrictions.UniqueItems;
import org.aml.typesystem.meta.restrictions.minmax.MaxItems;
import org.aml.typesystem.meta.restrictions.minmax.MaxLength;
import org.aml.typesystem.meta.restrictions.minmax.MaxProperties;
import org.aml.typesystem.meta.restrictions.minmax.Maximum;
import org.aml.typesystem.meta.restrictions.minmax.MinItems;
import org.aml.typesystem.meta.restrictions.minmax.MinLength;
import org.aml.typesystem.meta.restrictions.minmax.MinProperties;
import org.aml.typesystem.meta.restrictions.minmax.Minimum;
import org.aml.typesystem.values.ArrayImpl;
import org.aml.typesystem.values.ObjectImpl;

import junit.framework.Assert;
import junit.framework.TestCase;

public class InstanceValidationTest extends TestCase {

	public void test0() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.declareProperty("age", BuiltIns.NUMBER, true);
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test1() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.declareProperty("age", BuiltIns.NUMBER, true);
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", 2);
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test10() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.closeUnknownProperties();
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		obj.putProperty("d", "Pavel");
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test11() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.closeUnknownProperties();
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test12() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.addMeta(new MinProperties(2));
		person.closeUnknownProperties();
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test13() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.addMeta(new MinProperties(1));
		person.closeUnknownProperties();
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test14() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.addMeta(new MaxProperties(2));
		person.closeUnknownProperties();
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test15() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.addMeta(new MaxProperties(0));
		person.closeUnknownProperties();
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test16() {
		final AbstractType person = TypeOps.derive("Persons", BuiltIns.ARRAY);
		person.addMeta(new MinItems(0));
		final ArrayImpl obj = new ArrayImpl();
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test17() {
		final AbstractType person = TypeOps.derive("Persons", BuiltIns.ARRAY);
		person.addMeta(new MinItems(1));
		final ArrayImpl obj = new ArrayImpl();
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test18() {
		final AbstractType person = TypeOps.derive("Persons", BuiltIns.ARRAY);
		person.addMeta(new MaxItems(0));
		final ArrayImpl obj = new ArrayImpl();
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test19() {
		final AbstractType person = TypeOps.derive("Persons", BuiltIns.ARRAY);
		person.addMeta(new MaxItems(0));
		final ArrayImpl obj = new ArrayImpl();
		obj.add(3);
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test2() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.declareProperty("age", BuiltIns.NUMBER, false);
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test20() {
		final AbstractType person = TypeOps.derive("INts", BuiltIns.ARRAY);
		person.addMeta(new UniqueItems());
		final ArrayImpl obj = new ArrayImpl();
		obj.add(3);
		obj.add(3);
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test21() {
		final AbstractType person = TypeOps.derive("Ints", BuiltIns.ARRAY);
		person.addMeta(new UniqueItems());
		final ArrayImpl obj = new ArrayImpl();
		obj.add(3);
		obj.add(4);
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test22() {
		final AbstractType string = TypeOps.derive("MyString", BuiltIns.STRING);
		string.addMeta(new MinLength(3));
		Status validateDirect = string.validateDirect("1234");
		Assert.assertTrue(validateDirect.isOk());
		validateDirect = string.validateDirect("12");
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test24() {
		final AbstractType string = TypeOps.derive("MyString", BuiltIns.STRING);
		string.addMeta(new MaxLength(3));
		Status validateDirect = string.validateDirect("1234");
		Assert.assertTrue(!validateDirect.isOk());
		validateDirect = string.validateDirect("12");
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test25() {
		final AbstractType string = TypeOps.derive("MyString", BuiltIns.STRING);
		string.addMeta(new Pattern("."));
		Status validateDirect = string.validateDirect("12");
		Assert.assertTrue(!validateDirect.isOk());
		validateDirect = string.validateDirect("1");
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test27() {
		final AbstractType string = TypeOps.derive("MyNumber", BuiltIns.NUMBER);
		string.addMeta(new Minimum(1));
		Status validateDirect = string.validateDirect(0);
		Assert.assertTrue(!validateDirect.isOk());
		validateDirect = string.validateDirect(2);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test28() {
		final AbstractType string = TypeOps.derive("MyNumber", BuiltIns.NUMBER);
		string.addMeta(new Maximum(1));
		Status validateDirect = string.validateDirect(0);
		Assert.assertTrue(validateDirect.isOk());
		validateDirect = string.validateDirect(2);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test29() {
		final AbstractType string = TypeOps.derive("MyNumber", BuiltIns.ARRAY);
		final Status validateDirect = string.validateDirect(0);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test3() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.declareMapProperty(".", BuiltIns.NUMBER);
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		obj.putProperty("d", "Pavel");
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test30() {
		final AbstractType strings = TypeOps.derive("MyNumber", BuiltIns.ARRAY);
		strings.addMeta(new ComponentShouldBeOfType(BuiltIns.STRING));
		Status validateDirect = strings.validateDirect(0);
		Assert.assertTrue(!validateDirect.isOk());
		ArrayImpl at = new ArrayImpl();
		at.add(3);
		validateDirect = strings.validateDirect(at);
		Assert.assertTrue(!validateDirect.isOk());
		at = new ArrayImpl();
		at.add("3");
		validateDirect = strings.validateDirect(at);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test31() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.STRING);
		person.addMeta(new Enum(Arrays.asList(new String[] { "a", "b" })));
		final Status validateDirect = person.validateDirect("c");
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test32() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.STRING);
		person.addMeta(new Enum(Arrays.asList(new String[] { "a", "b" })));
		final Status validateDirect = person.validateDirect("a");
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test4() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.declareMapProperty(".*", BuiltIns.NUMBER);
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		obj.putProperty("d", "Pavel");
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test5() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.declareMapProperty(".*", BuiltIns.NUMBER);
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		obj.putProperty("d", 5);
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test6() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.declareAdditionalProperty(BuiltIns.NUMBER);
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		obj.putProperty("d", 5);
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test7() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.declareAdditionalProperty(BuiltIns.NUMBER);
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		obj.putProperty("d", false);
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}

	public void test8() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.declareMapProperty(".", BuiltIns.BOOLEAN);
		person.declareAdditionalProperty(BuiltIns.NUMBER);
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		obj.putProperty("d", false);
		obj.putProperty("dd", 5);
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(validateDirect.isOk());
	}

	public void test9() {
		final AbstractType person = TypeOps.derive("Person", BuiltIns.OBJECT);
		person.declareProperty("name", BuiltIns.STRING, false);
		person.declareMapProperty(".", BuiltIns.BOOLEAN);
		person.declareAdditionalProperty(BuiltIns.NUMBER);
		final ObjectImpl obj = new ObjectImpl();
		obj.putProperty("name", "Pavel");
		obj.putProperty("d", false);
		obj.putProperty("dd", false);
		final Status validateDirect = person.validateDirect(obj);
		Assert.assertTrue(!validateDirect.isOk());
	}
}