package org.aml.apimodel;

import java.math.BigDecimal;
import java.util.List;

public class AbstractParam {

	public List<String> getEnumeration() {
		return null;
	}

	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getExample() {
		return null;
	}

	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getKey() {
		return null;
	}

	public String getDefaultValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getMinLength() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getMaxLength() {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getMinimum() {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getMaximum() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRepeat() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static enum TypeKind{
		BOOLEAN,DATE,FILE,INTEGER,NUMBER,STRING
	}

	public TypeKind getTypeKind() {
		return null;
	}

}
