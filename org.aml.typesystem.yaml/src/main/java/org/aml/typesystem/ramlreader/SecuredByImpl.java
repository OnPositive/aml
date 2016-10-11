package org.aml.typesystem.ramlreader;

import java.util.LinkedHashMap;
import java.util.Map;

import org.aml.apimodel.Annotable;
import org.aml.apimodel.SecuredByConfig;
import org.aml.apimodel.TopLevelModel;
import org.raml.v2.internal.impl.commons.nodes.ParametrizedSecuritySchemeRefNode;
import org.raml.yagi.framework.nodes.Node;

public class SecuredByImpl extends AbstractWrappedNodeImpl<Annotable, Node> implements SecuredByConfig{

	public SecuredByImpl(TopLevelModel mdl, Annotable parent, Node node) {
		super(mdl, parent, node);
	}

	@Override
	public String name() {	
		ParametrizedSecuritySchemeRefNode n=(ParametrizedSecuritySchemeRefNode) this.original;
		return n.getRefName();
	}

	@Override
	public LinkedHashMap<String, Object> settings() {
		ParametrizedSecuritySchemeRefNode n=(ParametrizedSecuritySchemeRefNode) this.original;
		Map<String, Node> parameters = n.getParameters();
		LinkedHashMap<String, Object>res=new LinkedHashMap<>();
		for (String s:parameters.keySet()){
			res.put(s, TopLevelRamlModelBuilder.toObject(parameters.get(s)));
		}
		return res;
	}

}
