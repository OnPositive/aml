package org.aml.typesystem.acbuilder;

import java.util.LinkedHashMap;

import org.aml.typesystem.AbstractType;

public class AcScheme {
	
	protected LinkedHashMap<AbstractType, CompositeAcElement>schemes=new LinkedHashMap<>();
	protected AbstractType baseType;
	
	public AcScheme(AbstractType baseType) {
		super();
		this.baseType = baseType;
	}

	public LinkedHashMap<AbstractType, CompositeAcElement> getSchemes() {
		return schemes;
	}

	public void setSchemes(LinkedHashMap<AbstractType, CompositeAcElement> schemes) {
		this.schemes = schemes;
	}
	
}
