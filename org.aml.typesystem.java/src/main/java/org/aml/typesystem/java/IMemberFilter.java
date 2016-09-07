package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

public interface IMemberFilter extends IConfiguarionExtension{

	boolean accept(IMember member);
}
