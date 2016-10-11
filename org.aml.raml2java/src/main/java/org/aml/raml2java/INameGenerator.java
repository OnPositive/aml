package org.aml.raml2java;

import org.aml.typesystem.AbstractType;

public interface INameGenerator {

	String fullyQualifiedName(AbstractType t);

}
