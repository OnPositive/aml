package org.aml.typesystem.meta;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

public class TypeModifier extends TypeInformation {

	public TypeModifier() {
		super(false);
	}
	
	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
