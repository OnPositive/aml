package org.aml.typesystem.ramlreader;

import java.util.Map;

import org.aml.apimodel.MethodBase;
import org.aml.apimodel.SecurityScheme;
import org.aml.apimodel.TopLevelModel;
import org.raml.v2.internal.impl.commons.nodes.SecuritySchemeNode;
import org.raml.yagi.framework.nodes.Node;

public class SecuritySchemeImpl extends AbstractWrappedNodeImpl<TopLevelRamlImpl, SecuritySchemeNode> implements SecurityScheme{

	public SecuritySchemeImpl(TopLevelModel mdl, TopLevelRamlImpl parent, SecuritySchemeNode node) {
		super(mdl, parent, node);
	
	}

	@Override
	public String name() {
		return getKey();
	}

	@Override
	public String type() {
		return getChildWithKeyAs("type", String.class, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> settings() {
		Node n=getChildNodeWithKey("settings");
		Object result=TopLevelRamlModelBuilder.toObject(n);
		return (Map<String, Object>) result;
	}

	@Override
	public MethodBase describedBy() {
		Node childNodeWithKey = getChildNodeWithKey("describedBy");
		return new MethodImpl(mdl, null, childNodeWithKey);
	}

}
