package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.aml.apimodel.Action;
import org.aml.apimodel.Api;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.Resource;
import org.aml.apimodel.TopLevelModel;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.TypeOps;
import org.raml.v2.internal.impl.commons.nodes.MethodNode;
import org.raml.v2.internal.impl.commons.nodes.ResourceNode;
import org.raml.yagi.framework.nodes.Node;

public class ResourceImpl extends AbstractWrappedNodeImpl<Resource,ResourceNode> implements Resource{

	public ResourceImpl(TopLevelModel mdl,Resource parent, ResourceNode n) {
		super(mdl,parent,n);
	}

	@Override
	public String relativeUri() {
		return ((ResourceNode)this.original).getRelativeUri();
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

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends INamedParam> uriParameters() {
		Node childNodeWithKey = this.getChildNodeWithKey("uriParameters");
		List<? extends INamedParam> paramList = toParamList(childNodeWithKey);
		String relativeUri = this.relativeUri();
		HashSet<String>pNames=new HashSet<>();
		while (true){
			int indexOf = relativeUri.indexOf('{');
			if (indexOf==-1){
				break;
			}
			int end=relativeUri.indexOf('}',indexOf);
			if (end==-1){
				break;
			}
			String pName=relativeUri.substring(indexOf+1, end);
			relativeUri=relativeUri.substring(end);
			pNames.add(pName);
		}
		for (String s:pNames){
			boolean found=false;
			for (INamedParam p:paramList){
				if (p.getKey().equals(s)){
					found=true;
					break;
				}
			}
			if (!found){
				NamedParam namedParam = new NamedParam(TypeOps.derive(s,BuiltIns.STRING), false, false);
				((List<NamedParam>)paramList).add(namedParam);
			}
		}
		return paramList;		
	}

	@Override
	public String getUri() {
		return ((ResourceNode)this.original).getResourcePath();
	}

	@Override
	public Api getApi() {
		return (Api) mdl;
	}
}