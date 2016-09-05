package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.meta.IHasType;

public interface IMatchesProperty extends IHasType {

	String id();

	boolean matches(String name);

}
