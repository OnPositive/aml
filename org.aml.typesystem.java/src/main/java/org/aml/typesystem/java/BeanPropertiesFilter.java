package org.aml.typesystem.java;

import org.aml.typesystem.IFieldModel;
import org.aml.typesystem.IMember;
import org.aml.typesystem.IMethodModel;

public class BeanPropertiesFilter implements IMemberFilter{

	@Override
	public boolean accept(IMember member) {
		if (!member.isPublic()){
			return false;
		}
		if (member instanceof IMethodModel){
			String name = member.getName();
			if (name.startsWith("get")||name.startsWith("is")){
				if (((IMethodModel) member).getParameters().length==0){
					return true;
				}
			}
		}
		if (member instanceof IFieldModel){
			if (member.isPublic()){
				return true;
			}
		}
		return false;
	}
}
