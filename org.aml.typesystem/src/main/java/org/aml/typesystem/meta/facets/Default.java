package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

/**
 * <p>Default class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Default extends Facet<Object>{

	/**
	 * <p>Constructor for Default.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 */
	public Default(Object value) {
		super(value);
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "default";
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return ownerType.validate(value);
	}
	
	/** {@inheritDoc} */
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
	

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}

}
