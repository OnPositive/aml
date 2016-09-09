package org.aml.typesystem.ramlreader;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.TypeRegistryImpl;
import org.aml.typesystem.raml.model.TopLevelRaml;
import org.raml.v2.internal.impl.commons.nodes.TypeDeclarationNode;
import org.raml.yagi.framework.nodes.Node;

public class TopLevelRamlImpl implements TopLevelRaml{

	
	protected transient Node original;
	
	protected transient HashMap<String,TypeDeclarationNode> typeDecls=new HashMap<>();
	
	public TopLevelRamlImpl(Node original) {
		super();
		this.original = original;
	}
	protected LinkedHashMap<String,TopLevelRamlImpl>usesMap=new LinkedHashMap<>();
	
	

	protected TypeRegistryImpl topLevelTypes=new TypeRegistryImpl(BuiltIns.getBuiltInTypes());
	protected TypeRegistryImpl annotationTypes=new TypeRegistryImpl(BuiltIns.getBuiltInTypes());
}
