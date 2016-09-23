package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Action;
import org.aml.apimodel.Resource;
import org.aml.apimodel.TopLevelModel;
import org.raml.v2.internal.impl.commons.nodes.MethodNode;
import org.raml.v2.internal.impl.commons.nodes.ResourceNode;

public class ResourceImpl extends AbstractWrappedNodeImpl<Resource,ResourceNode> implements Resource{

	public ResourceImpl(TopLevelModel mdl,Resource parent, ResourceNode n) {
		super(mdl,parent,n);
	}

	@Override
	public String relativeUri() {
		return this.node.getRelativeUri();
	}

	@Override
	public List<Resource> resources() {
		ArrayList<Resource>res=new ArrayList<>();
		this.getChildWithType(ResourceNode.class).forEach(x->res.add(new ResourceImpl(this.mdl,this, x)));
		return res;
	}

	@Override
	public Resource parentResource() {
		return parent;
	}

	@Override
	public List<Action> methods() {
		ArrayList<Action>res=new ArrayList<>();
		this.getChildWithType(MethodNode.class).forEach(x->res.add(new MethodImpl(this.mdl,this, x)));
		return res;
	}

	@Override
	public List<? extends NamedParam> uriParameters() {
		return new ArrayList<>();//FIXME
	}

	@Override
	public String getUri() {
		return node.getResourcePath();
	}
}