package org.aml.java2raml;

import javax.xml.bind.annotation.XmlTransient;

public class XMLTransient {

	String name;
	
	@XmlTransient
	String password;
}
