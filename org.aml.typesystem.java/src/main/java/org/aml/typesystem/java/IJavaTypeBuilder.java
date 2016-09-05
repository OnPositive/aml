package org.aml.typesystem.java;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.ITypeModel;

public interface IJavaTypeBuilder {

	AbstractType getType(ITypeModel mdl);
}
