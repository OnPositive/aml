package org.aml.raml2java;

import java.util.HashSet;

import org.aml.typesystem.AbstractType;

public class DefaultNameGenerator implements INameGenerator{

	protected String defaultPackageName;
	protected HashSet<String>used=new HashSet<>();
	
	
	public DefaultNameGenerator(String string) {
		this.defaultPackageName=string;
	}


	@Override
	public String fullyQualifiedName(AbstractType t) {
		String string = defaultPackageName+"."+t.name();
		if (!used.add(string)){
			for (int i=2;i<Integer.MAX_VALUE;i++){
				string = defaultPackageName+"."+t.name()+i;
				if (used.add(string)){
					return string;
				}
			}
		}
		return string;
	}

}
