package org.aml.typesystem.acbuilder;

import java.util.ArrayList;
import java.util.List;

import org.aml.typesystem.AbstractType;

public class CompositeAcElement {
	
	public static enum TypeFamily{	
		NUMBER,
		BOOLEAN,
		STRING,		
		OBJECT,
		ARRAY
	}
	
	protected ArrayList<AcElement>list=new ArrayList<>();
	
	protected TypeFamily family;
	
	protected AbstractType associtatedType;
	
	public AbstractType getAssocitatedType() {
		return associtatedType;
	}

	public void setAssocitatedType(AbstractType associtatedType) {
		this.associtatedType = associtatedType;
	}

	public CompositeAcElement(TypeFamily family) {
		super();
		this.family = family;
	}
	
	public List<AcElement> getChildren(){
		return list;
	}

	public TypeFamily getFamily() {
		return family;
	}
	public void setFamily(TypeFamily family) {
		this.family = family;
	}

}
