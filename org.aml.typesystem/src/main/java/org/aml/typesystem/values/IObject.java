package org.aml.typesystem.values;

import java.util.Set;

public interface IObject {

	Object getProperty(String name);

	Set<String> keys();
}
