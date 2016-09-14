package org.aml.raml2java;

import javax.xml.bind.JAXB;

public class Tst {

	public static void main(String[] args) {
		XMLCustom unmarshal = JAXB.unmarshal(Tst.class.getResourceAsStream("x.xml"),XMLCustom.class);
		System.out.println(unmarshal);
	}
}
