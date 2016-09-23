package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Action;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.Resource;
import org.aml.apimodel.Response;
import org.aml.apimodel.TopLevelModel;
import org.raml.v2.internal.impl.commons.nodes.MethodNode;
import org.raml.v2.internal.impl.commons.nodes.ResponseNode;
import org.raml.yagi.framework.nodes.Node;

public class MethodImpl extends AbstractWrappedNodeImpl<Resource, MethodNode> implements Action {

	public MethodImpl(TopLevelModel mdl, Resource parent, MethodNode node) {
		super(mdl, parent, node);
	}

	@Override
	public String method() {
		return node.getName();
	}

	@Override
	public Resource resource() {
		return this.parent;
	}

	@Override
	public List<String> protocols() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<String> getIs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends INamedParam> queryParameters() {
		Node childNodeWithKey = this.getChildNodeWithKey("queryParameters");
		return toParamList(childNodeWithKey);
	}

	

	@Override
	public List<Response> responses() {
		ArrayList<Response> result = new ArrayList<>();
		Node childNodeWithKey = this.getChildNodeWithKey("responses");
		if (childNodeWithKey != null) {
			for (Node n : childNodeWithKey.getChildren()) {
				if (n instanceof ResponseNode){
					ResponseNode rn=(ResponseNode) n;
					result.add(new ResponseImpl(mdl,this,rn));
				}
			}
		}
		return result;
	}
	
}