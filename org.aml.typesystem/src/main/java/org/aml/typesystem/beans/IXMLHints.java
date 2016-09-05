package org.aml.typesystem.beans;

import java.util.List;

public interface IXMLHints {

	boolean isAttribute();
	String  localName();
	
	List<String>order();
	String qualifiedName();
	boolean wrapped();
}
