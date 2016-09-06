package org.aml.typesystem.java;

import org.aml.typesystem.IFieldModel;
import org.aml.typesystem.IMember;

public class OneTwoOneNameBuilder implements IPropertyNameBuilder{

	@Override
	public String buildName(IMember memb) {
		if (memb instanceof IFieldModel){
			return memb.getName();
		}
		String transformMethodName = transformMethodName(memb);
		if (transformMethodName.length()==0){
			return "_";
		}
		
		return Character.toLowerCase(transformMethodName.charAt(0))+transformMethodName.substring(1);
	}

	private String transformMethodName(IMember memb) {
		if (memb.getName().startsWith("get")){
			return memb.getName().substring(3);
		}
		else if (memb.getName().startsWith("is")){
			return memb.getName().substring(2);
		}
		else if (memb.getName().startsWith("set")){
			return memb.getName().substring(3);
		}
		return memb.getName();
	}

}
