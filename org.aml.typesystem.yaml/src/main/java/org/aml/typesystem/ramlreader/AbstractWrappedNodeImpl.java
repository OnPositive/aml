package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Annotable;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.TopLevelModel;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.raml.yagi.framework.nodes.KeyValueNode;
import org.raml.yagi.framework.nodes.Node;
import org.raml.yagi.framework.nodes.SimpleTypeNode;
import org.raml.yagi.framework.nodes.StringNode;

public class AbstractWrappedNodeImpl<P extends Annotable,T extends Node> extends AnnotableImpl {

	protected P parent;
	protected TopLevelModel mdl;

	public AbstractWrappedNodeImpl(TopLevelModel mdl,P parent,T node) {
		super(node);
		this.parent=parent;
		this.mdl=mdl;
	}
	@Override
	protected TopLevelRamlImpl getTopLevel() {
		return (TopLevelRamlImpl) mdl;
	}
	
	public String displayName() {		
		return this.getChildWithKeyAs("displayName", String.class, this.getKey());		
	}
	@SuppressWarnings("unchecked")
	protected List<MimeType> toMimeTypes(Node childNodeWithKey,MethodImpl method) {
		List<NamedParam> paramList = (List<NamedParam>) toParamList(childNodeWithKey);
		ArrayList<MimeType>mt=new ArrayList<>();
		for (NamedParam p:paramList){
			mt.add(new MimeTypeImpl(p.getTypeModel(),method));
		}
		return mt;
	}
	
	protected List<MimeType> body(MethodImpl method) {
		Node childNodeWithKey = this.getChildNodeWithKey("body");
		return toMimeTypes(childNodeWithKey,method);
	}


	public final List<? extends INamedParam> headers() {
		Node childNodeWithKey = this.getChildNodeWithKey("headers");
		return toParamList(childNodeWithKey);
	}

	protected  final  boolean hasBody(MethodImpl m) {
		return !body(m).isEmpty();
	}
	
	public String getKey() {
		if (this.original instanceof KeyValueNode){
			KeyValueNode kv=(KeyValueNode) this.original;
			if (kv.getKey() instanceof SimpleTypeNode<?>){
				SimpleTypeNode<?> nnm=(SimpleTypeNode<?>) kv.getKey();
				return nnm.getLiteralValue();
			}
			
		}
		return null;
	}
	
	protected List<? extends INamedParam> toParamList(Node childNodeWithKey) {
		ArrayList<NamedParam>prms=new ArrayList<>();
		if (childNodeWithKey==null){
			return prms;
		}
		List<Node> children = childNodeWithKey.getChildren();
		
		TopLevelRamlModelBuilder topLevelRamlModelBuilder = new TopLevelRamlModelBuilder();
		for (Node n:children){
			KeyValueNode kv=(KeyValueNode) n;
			StringNode key=(StringNode) kv.getKey();
			String typeName=key.getLiteralValue();
			boolean required=!typeName.endsWith("?");
			if (!required){
				typeName=typeName.substring(0, typeName.length()-1);
			}			
			Node value = kv.getValue();			
			AbstractType buildType = topLevelRamlModelBuilder.buildType((TopLevelRamlImpl) mdl, typeName,value, false);
			if (buildType.isOptional()){
				required=false;
			}
			boolean repeat=false;
			if (buildType.isArray()){
				repeat=true;
				ComponentShouldBeOfType oneMeta = buildType.oneMeta(ComponentShouldBeOfType.class);
				AbstractType at=oneMeta.range();
				for (TypeInformation i:buildType.meta()){
					if (!(i instanceof ComponentShouldBeOfType)){
						at.addMeta(i);
					}
				}
				buildType=at;
			}
			NamedParam result=new NamedParam(buildType, required, repeat);
			prms.add(result);
		}
		return prms;
	}
	
	public String description() {		
		return this.getChildWithKeyAs("description", String.class, null);		
	}

}