package org.aml.typesystem.java;

import org.aml.typesystem.IFieldModel;
import org.aml.typesystem.IMember;

public class FieldMemberFilter implements IMemberFilter{

	@Override
	public boolean accept(IMember member) {		
		return member instanceof IFieldModel;
	}

}
