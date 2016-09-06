package org.aml.java2raml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="element")
public class XMLSerialized2 {
	
	@XmlElement(required=false,name="Name")
	String name;
	@XmlAttribute(required=false,name="LastName")
	String lastName;
//	
	@XmlElement(defaultValue="100",nillable=true)
	double salary;
}
