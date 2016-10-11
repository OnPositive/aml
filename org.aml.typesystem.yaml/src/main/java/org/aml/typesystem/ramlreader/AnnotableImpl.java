package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Annotable;
import org.aml.typesystem.meta.facets.Annotation;
import org.raml.v2.internal.impl.commons.nodes.AnnotationNode;
import org.raml.yagi.framework.nodes.KeyValueNode;
import org.raml.yagi.framework.nodes.Node;
import org.raml.yagi.framework.nodes.ObjectNode;
import org.raml.yagi.framework.nodes.SimpleTypeNode;
import org.raml.yagi.framework.nodes.StringNode;

public abstract class AnnotableImpl implements Annotable{
	
	protected Node original;
	
	
	public AnnotableImpl(Node original) {
		super();
		this.original = original;
	}

	protected abstract TopLevelRamlImpl getTopLevel();
	
	@Override
	public List<Annotation> annotations() {
		ArrayList<Annotation>results=new ArrayList<>();
		Node node=this.original;
		if (node instanceof KeyValueNode){
			KeyValueNode kv=(KeyValueNode) node;
			node=kv.getValue();
		}
		for (Node n:node.getChildren()){
			if (n instanceof AnnotationNode){
				AnnotationNode mn=(AnnotationNode) n;
				Object val=TopLevelRamlModelBuilder.toObject(mn.getValue());
				String literalValue = mn.getKey().getLiteralValue();
				Annotation as = new Annotation(literalValue.substring(1, literalValue.length() - 1), val, null);
				TopLevelRamlModelBuilder.bindAnnotation(this.getTopLevel(), as);
				results.add(as);
			}
		}
		return results;
	}

	protected <V> List<V> getChildWithType(Class<V>cl) {
		ArrayList<V>result=new ArrayList<>();
		if (this.original instanceof KeyValueNode){
			KeyValueNode kv=(KeyValueNode) this.original;
			for (Node n:kv.getValue().getChildren()){
				if (cl.isInstance(n)){
					result.add(cl.cast(n));
				}
			}
		}
		return result;
	}

	protected <V> V getChildWithKeyAs(String key, Class<V>cl, V defaultValue) {
		if (this.original instanceof KeyValueNode){
			KeyValueNode kv=(KeyValueNode) this.original;
			return getFromKV(key, cl, kv,defaultValue);
			
		}
		return defaultValue;
	}

	protected Node getChildNodeWithKey(String key) {
		Node nodeo=this.original;
		if (nodeo instanceof KeyValueNode){
			KeyValueNode kv=(KeyValueNode) nodeo;
			nodeo=kv.getValue();
		}
		for (Node n:nodeo.getChildren()){
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

	private <V> V getFromKV(String key, Class<V> cl, KeyValueNode kv, V defaultValue) {
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
