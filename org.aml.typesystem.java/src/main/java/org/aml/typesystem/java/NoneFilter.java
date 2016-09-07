package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

public class NoneFilter implements IMemberFilter{

	@Override
	public boolean accept(IMember member) {
		return false;
	}

}
