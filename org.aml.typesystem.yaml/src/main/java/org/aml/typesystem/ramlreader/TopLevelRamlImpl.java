package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.aml.apimodel.Library;
import org.aml.apimodel.SecurityScheme;
import org.aml.apimodel.TopLevelModel;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.TypeRegistryImpl;
import org.raml.v2.api.model.common.ValidationResult;
import org.raml.v2.internal.impl.commons.nodes.SecuritySchemeNode;
import org.raml.yagi.framework.nodes.Node;

public class TopLevelRamlImpl extends AnnotableImpl implements TopLevelModel{

	
	protected transient HashMap<String,Node> typeDecls=new HashMap<>();
	protected transient HashMap<String,Node> atypeDecls=new HashMap<>();
	
	public TopLevelRamlImpl(Node original) {
		super(original);
		this.original = original;
	}
	public TopLevelRamlImpl(TopLevelRamlImpl n) {
		super(n.original);
		this.typeDecls=n.typeDecls;
		this.atypeDecls=n.atypeDecls;
		this.topLevelTypes=n.topLevelTypes;
		this.annotationTypes=n.annotationTypes;
		this.topLevelTypes.allTypes().forEach(x->x.setSource(this));
		this.annotationTypes.allTypes().forEach(x->x.setSource(this));
		this.usesMap=n.usesMap;
		this.validation=n.validation;
	}
	protected LinkedHashMap<String,LibraryImpl>usesMap=new LinkedHashMap<>();
	
	

	protected TypeRegistryImpl topLevelTypes=new TypeRegistryImpl(BuiltIns.getBuiltInTypes());
	protected TypeRegistryImpl annotationTypes=new TypeRegistryImpl(BuiltIns.getBuiltInTypes());
	private List<ValidationResult> validation=new ArrayList<>();

	
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
		if (this.validation!=null&&!this.validationResults().isEmpty()){
			return false;
		}
		return true;
	}
	public List<ValidationResult> validationResults() {
		return validation;
	}
	@Override
	public String getVersion() {
		return "1.0";
	}
	@Override
	protected TopLevelRamlImpl getTopLevel() {
		return this;
	}
	@Override
	public List<SecurityScheme> securityDefinitions() {
		ArrayList<SecurityScheme>result=new ArrayList<>();
		Node childNodeWithKey = this.getChildNodeWithKey("securitySchemes");
		if (childNodeWithKey!=null){
			for (Node n:childNodeWithKey.getChildren()){
				if (n instanceof SecuritySchemeNode){
					SecuritySchemeNode sn=(SecuritySchemeNode) n;
					result.add(new SecuritySchemeImpl(this, this, sn));
				}
			}
		}
		return result;
	}
	public void setValidationResults(List<ValidationResult> validationResults) {
		this.validation=validationResults;
	}
}
