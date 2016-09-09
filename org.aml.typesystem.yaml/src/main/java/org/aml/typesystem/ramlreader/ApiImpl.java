package org.aml.typesystem.ramlreader;

import org.raml.yagi.framework.nodes.Node;

public class ApiImpl extends TopLevelRamlImpl{

	public ApiImpl(Node original) {
		super(original);
	}

	protected ApiImpl(TopLevelRamlImpl n) {
		super(n);
	}

}
