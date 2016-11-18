package org.aml.apimodel;

import java.math.BigDecimal;
import java.util.List;

import org.aml.typesystem.AbstractType;

public interface INamedParam {

	public List<String> getEnumeration() ;

	public String description() ;

	public String getExample() ;

	public String getDisplayName() ;

	public String getKey() ;

	public String getDefaultValue() ;

	public String getPattern() ;

	public Integer getMinLength() ;

	public Integer getMaxLength() ;

	public BigDecimal getMinimum() ;

	public BigDecimal getMaximum();

	public boolean isRequired() ;

	public boolean isRepeat() ;
	
	public static enum TypeKind{
		BOOLEAN,DATE,FILE,INTEGER,NUMBER,STRING
	}

	public TypeKind getTypeKind();

	public AbstractType getTypeModel();
	
	public ParameterLocation location();
}