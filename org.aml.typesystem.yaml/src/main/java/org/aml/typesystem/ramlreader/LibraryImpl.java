package org.aml.typesystem.ramlreader;

import org.aml.typesystem.raml.model.Library;
import org.raml.yagi.framework.nodes.Node;

public class LibraryImpl extends TopLevelRamlImpl implements Library{

	public LibraryImpl(Node original) {
		super(original);	
	}

}
