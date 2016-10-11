package org.aml.java2raml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class XMLSerialized {
	
	@XmlElement(name="Name",required=true)
	String name;
	@XmlAttribute(name="LastName",required=true)
	String lastName;
}
