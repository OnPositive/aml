package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aml.apimodel.Annotable;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.TopLevelModel;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.raml.v2.internal.impl.commons.nodes.TypeDeclarationNode;
import org.raml.yagi.framework.nodes.KeyValueNode;
import org.raml.yagi.framework.nodes.Node;
import org.raml.yagi.framework.nodes.ObjectNode;
import org.raml.yagi.framework.nodes.SimpleTypeNode;
import org.raml.yagi.framework.nodes.StringNode;

public class AbstractWrappedNodeImpl<P extends Annotable,T extends Node> extends AnnotableImpl {

	protected P parent;
	protected T node;
	protected TopLevelModel mdl;

	public AbstractWrappedNodeImpl(TopLevelModel mdl,P parent,T node) {
		super();
		this.parent=parent;
		this.node=node;
		this.mdl=mdl;
	}
	
	public String displayName() {		
		return this.getChildWithKeyAs("displayName", String.class, this.getKey());		
	}
	@SuppressWarnings("unchecked")
	protected List<MimeType> toMimeTypes(Node childNodeWithKey) {
		List<NamedParam> paramList = (List<NamedParam>) toParamList(childNodeWithKey);
		ArrayList<MimeType>mt=new ArrayList<>();
		for (NamedParam p:paramList){
			mt.add(new MimeTypeImpl(p));
		}
		return mt;
	}
	
	public List<MimeType> body() {
		Node childNodeWithKey = this.getChildNodeWithKey("body");
		return toMimeTypes(childNodeWithKey);
	}


	public final List<? extends INamedParam> headers() {
		Node childNodeWithKey = this.getChildNodeWithKey("headers");
		return toParamList(childNodeWithKey);
	}

	public  final  boolean hasBody() {
		return !body().isEmpty();
	}
	
	public String getKey() {
		if (this.node instanceof KeyValueNode){
			KeyValueNode kv=(KeyValueNode) this.node;
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
	protected<V> List<V> getChildWithType(Class<V>cl){
		ArrayList<V>result=new ArrayList<>();
		if (this.node instanceof KeyValueNode){
			KeyValueNode kv=(KeyValueNode) this.node;
			for (Node n:kv.getValue().getChildren()){
				if (cl.isInstance(n)){
					result.add(cl.cast(n));
				}
			}
		}
		return result;
	}
	protected<V> V getChildWithKeyAs(String key,Class<V>cl,V defaultValue){
		if (this.node instanceof KeyValueNode){
			KeyValueNode kv=(KeyValueNode) this.node;
			return getFromKV(key, cl, kv,defaultValue);
			
		}
		return defaultValue;
	}
	protected Node getChildNodeWithKey(String key) {
		KeyValueNode kv=(KeyValueNode) this.node;
		for (Node n:kv.getValue().getChildren()){
			if (n instanceof KeyValueNode){
				KeyValueNode node=(KeyValueNode) n;
				if (node.getKey() instanceof StringNode){
					StringNode nnm=(StringNode) node.getKey();
					if (nnm.getValue().equals(key)){
						Node value = node.getValue();
						return value;
					}
				}
			}
		}
		return null;
	}

	private <V> V getFromKV(String key, Class<V> cl, KeyValueNode kv,V defaultValue) {
		for (Node n:kv.getValue().getChildren()){
			if (n instanceof KeyValueNode){
				KeyValueNode node=(KeyValueNode) n;
				if (node.getKey() instanceof StringNode){
					StringNode nnm=(StringNode) node.getKey();
					if (nnm.getValue().equals(key)){
						Node value = node.getValue();
						if (value instanceof SimpleTypeNode<?>){
							SimpleTypeNode<?>vl=(SimpleTypeNode<?>) value;
							if (cl.isInstance(vl.getValue())){
								return cl.cast(vl.getValue());
							}
						}
						if (value instanceof ObjectNode){
							ObjectNode on=(ObjectNode) value;
							List<Node> children = on.getChildren();
							if (children.size()==1){
								Node nm = children.get(0);
								if (nm instanceof KeyValueNode){
									KeyValueNode val=(KeyValueNode) nm;
									Node actualValue=val.getValue();
									if (actualValue instanceof SimpleTypeNode<?>){
										SimpleTypeNode<?>st=(SimpleTypeNode<?>) actualValue;
										if (cl.isInstance(st.getValue())){
											return cl.cast(st.getValue());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

}