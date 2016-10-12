package org.aml.apimodel;

import java.util.Collection;

public interface Trait extends MethodBase{

	String name();

	Collection<SecuredByConfig> securedBy();
}
