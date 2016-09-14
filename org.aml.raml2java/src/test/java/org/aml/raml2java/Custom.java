package org.aml.raml2java;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlValue;

import org.w3c.dom.Element;


public class Custom {

	@XmlAnyAttribute
	Map<Object,Object>attrs=new LinkedHashMap<>();
	
	@XmlValue
	private String text;
	
	@XmlAnyElement
	List<Element>toStore=new ArrayList<>();
}
