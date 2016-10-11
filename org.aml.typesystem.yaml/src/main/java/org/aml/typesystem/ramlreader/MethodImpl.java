package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Action;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.Resource;
import org.aml.apimodel.Response;
import org.aml.apimodel.TopLevelModel;
import org.raml.v2.internal.impl.commons.nodes.MethodNode;
import org.raml.v2.internal.impl.commons.nodes.TraitRefNode;
import org.raml.yagi.framework.nodes.ArrayNode;
import org.raml.yagi.framework.nodes.KeyValueNode;
import org.raml.yagi.framework.nodes.Node;

public class MethodImpl extends AbstractWrappedNodeImpl<Resource, MethodNode> implements Action {

	public MethodImpl(TopLevelModel mdl, Resource parent, MethodNode node) {
		super(mdl, parent, node);
	}

	@Override
	public String method() {
		return ((MethodNode)original).getName();
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
		ArrayList<String>traits=new ArrayList<>();
		Node childNodeWithKey = this.getChildNodeWithKey("is");
		if (childNodeWithKey instanceof ArrayNode){
			ArrayNode rr=(ArrayNode) childNodeWithKey;
			
			for (Node n:rr.getChildren()){
				if (n instanceof TraitRefNode){
					TraitRefNode tr=(TraitRefNode) n;
					String value = tr.getValue();
					traits.add(value);
				}
			}
		}
		return traits;
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
				if (n instanceof KeyValueNode){
					KeyValueNode rn=(KeyValueNode) n;
					result.add(new ResponseImpl(mdl,this,rn));
				}
			}
		}
		return result;
	}

	@Override
	public List<MimeType> body() {
		return body(this);
	}

	@Override
	public boolean hasBody() {
		return hasBody(this);
	}
	
}