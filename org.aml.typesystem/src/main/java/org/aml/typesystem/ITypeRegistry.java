package org.aml.typesystem;

import java.util.Collection;

public interface ITypeRegistry {

	AbstractType getType(String type);

	Collection<AbstractType> types();
}
