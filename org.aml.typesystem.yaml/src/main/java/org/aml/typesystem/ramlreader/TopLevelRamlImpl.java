package org.aml.typesystem.ramlreader;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.aml.apimodel.Library;
import org.aml.apimodel.TopLevelModel;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.TypeRegistryImpl;
import org.raml.v2.api.model.common.ValidationResult;
import org.raml.v2.internal.impl.commons.nodes.TypeDeclarationNode;
import org.raml.yagi.framework.nodes.Node;

public class TopLevelRamlImpl extends AnnotableImpl implements TopLevelModel{

	
	protected transient Node original;
	
	protected transient HashMap<String,Node> typeDecls=new HashMap<>();
	protected transient HashMap<String,Node> atypeDecls=new HashMap<>();
	
	public TopLevelRamlImpl(Node original) {
		super();
		this.original = original;
	}
	public TopLevelRamlImpl(TopLevelRamlImpl n) {
		this.original=n.original;
		this.typeDecls=n.typeDecls;
		this.atypeDecls=n.atypeDecls;
		this.topLevelTypes=n.topLevelTypes;
		this.annotationTypes=n.annotationTypes;
		this.topLevelTypes.allTypes().forEach(x->x.setSource(this));
		this.annotationTypes.allTypes().forEach(x->x.setSource(this));
	}
	protected LinkedHashMap<String,LibraryImpl>usesMap=new LinkedHashMap<>();
	
	

	protected TypeRegistryImpl topLevelTypes=new TypeRegistryImpl(BuiltIns.getBuiltInTypes());
	protected TypeRegistryImpl annotationTypes=new TypeRegistryImpl(BuiltIns.getBuiltInTypes());

	
	@Override
	public ITypeRegistry types() {
		return topLevelTypes;
	}
	@Override
	public ITypeRegistry annotationTypes() {
		return annotationTypes;
	}
	@Override
	public Map<String, ? extends Library> uses() {
		return usesMap;
	}
	
	public boolean isOk() {
		return true;
	}
	public List<ValidationResult> validationResults() {
		return null;
	}
	@Override
	public String getVersion() {
		return "1.0";
	}

	
}
