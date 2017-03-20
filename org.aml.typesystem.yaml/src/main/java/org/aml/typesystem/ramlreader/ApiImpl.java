package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Api;
import org.aml.apimodel.DocumentationItem;
import org.aml.apimodel.Resource;
import org.aml.apimodel.SecuredByConfig;
import org.raml.v2.internal.impl.commons.nodes.ResourceNode;
import org.raml.yagi.framework.nodes.Node;

public class ApiImpl extends TopLevelRamlImpl implements Api{

	public ApiImpl(Node original) {
		super(original);
	}

	protected ApiImpl(TopLevelRamlImpl n) {
		super(n);
		
	}

	@Override
	public List<Resource> resources() {
		ArrayList<Resource>resources=new ArrayList<>();
		for (Node n: this.original.getChildren()){
			if (n instanceof ResourceNode){
				resources.add(new ResourceImpl(this,null,(ResourceNode) n));
			}
		}
		return resources;
	}

	@Override
	public String title() {
		return this.getChildWithKeyAs("title", String.class, null);		
	}

	@Override
	public String version() {
		return this.getChildWithKeyAs("version", String.class, null);		
	}


	@Override
	public List<DocumentationItem> documentation() {
		Node childNodeWithKey = this.getChildNodeWithKey("documentation");
		ArrayList<DocumentationItem>docs=new ArrayList<>();
		if (childNodeWithKey!=null){
			for (Node n:childNodeWithKey.getChildren()){
				docs.add(new DocumentationItemImpl(this, this, n));
			}
		}
		return docs;
	}

	@Override
	public String description() {
		return this.getChildWithKeyAs("description", String.class, null);
	}

	@Override
	public List<String> getMediaType() {
		return getStringList("mediaType");
	}

	@Override
	public List<String> getProtocols() {
		String key = "protocols";
		return getStringList(key);
	}

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<SecuredByConfig> getSecuredBy() {
		return new AbstractWrappedNodeImpl(this, null, this.original).securedBy();
	}

	@Override
	public String getBaseUrl() {
		return this.getChildWithKeyAs("baseUri", String.class, null);
	}
}