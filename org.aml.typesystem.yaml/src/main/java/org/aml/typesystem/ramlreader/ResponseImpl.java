package org.aml.typesystem.ramlreader;

import org.aml.apimodel.Action;
import org.aml.apimodel.Response;
import org.aml.apimodel.TopLevelModel;
import org.raml.yagi.framework.nodes.KeyValueNode;

public class ResponseImpl extends AbstractWrappedNodeImpl<Action, KeyValueNode> implements Response{

	
	public ResponseImpl(TopLevelModel mdl, MethodImpl methodImpl, KeyValueNode rn) {
		super(mdl,methodImpl,rn);
	}

	@Override
	public String code() {
		return getKey();
	}

}
