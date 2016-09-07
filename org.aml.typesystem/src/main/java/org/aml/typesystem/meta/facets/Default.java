package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

public class Default extends Facet<Object>{

	public Default(Object value) {
		super(value);
	}

	@Override
	public String facetName() {
		return "default";
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return ownerType.validate(value);
	}
	
	@Override
	public Object value() {
		Object value=super.value;
		if (this.value instanceof String){
			if (this.ownerType.isNumber()){
				if (this.ownerType.isSubTypeOf(BuiltIns.INTEGER)){
					value=Long.parseLong(value.toString());
				}
				else{
					value=Double.parseDouble(value.toString());
					
				}
			}
			if (this.ownerType.isBoolean()){
				value=Boolean.parseBoolean(value.toString());
			}
		}
		return value;
	}
	

	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}

}
