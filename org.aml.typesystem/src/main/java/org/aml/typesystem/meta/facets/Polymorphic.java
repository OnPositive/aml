package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

public class Polymorphic extends Facet<Boolean>{

	public Polymorphic(boolean value) {
		super(value,true);
		
	}
	
	public Polymorphic() {
		super(true,true);
		
	}

	@Override
	public String facetName() {
		return "polymorphic";
	}

	

	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
