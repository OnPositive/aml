package org.aml.typesystem.ramlreader;

import org.aml.apimodel.Action;
import org.aml.apimodel.Response;
import org.aml.apimodel.TopLevelModel;
import org.raml.v2.internal.impl.commons.nodes.ResponseNode;

public class ResponseImpl extends AbstractWrappedNodeImpl<Action, ResponseNode> implements Response{

	
	public ResponseImpl(TopLevelModel mdl, MethodImpl methodImpl, ResponseNode rn) {
		super(mdl,methodImpl,rn);
	}

	@Override
	public String code() {
		return getKey();
	}


}
