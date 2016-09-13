package org.aml.raml2java;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;

public class Tst {

	double age;
	public double getAge() {
		return age;
	}


	public void setAge(double age) {
		this.age = age;
	}

	
	boolean hasWife;
	
	public boolean getHasWife() {
		return hasWife;
	}


	public void setHasWife(boolean hasWife) {
		this.hasWife = hasWife;
	}


	public static void main(String[] args) {
		Tst unmarshal = JAXB.unmarshal(Tst.class.getResourceAsStream("/s1.xml"), Tst.class);
		System.out.println(unmarshal);
	}
}
