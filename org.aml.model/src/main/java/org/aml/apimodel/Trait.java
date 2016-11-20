package org.aml.apimodel;

import java.util.List;

public interface Trait extends MethodBase{

	String name();

	List<SecuredByConfig> securedBy();
}
