package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.FacetRegistry;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.FacetDeclaration;
import org.aml.typesystem.meta.restrictions.FacetRestriction;
import org.aml.typesystem.meta.restrictions.RestrictionsList;
import org.raml.v2.internal.impl.commons.nodes.AnnotationNode;
import org.raml.v2.internal.impl.commons.nodes.AnnotationTypeNode;
import org.raml.v2.internal.impl.commons.nodes.CustomFacetDefinitionNode;
import org.raml.v2.internal.impl.commons.nodes.ExternalSchemaTypeExpressionNode;
import org.raml.v2.internal.impl.commons.nodes.FacetNode;
import org.raml.v2.internal.impl.commons.nodes.TypeDeclarationField;
import org.raml.v2.internal.impl.commons.nodes.TypeDeclarationNode;
import org.raml.v2.internal.impl.commons.nodes.TypeExpressionNode;
import org.raml.v2.internal.impl.v10.nodes.ArrayTypeExpressionNode;
import org.raml.v2.internal.impl.v10.nodes.LibraryLinkNode;
import org.raml.v2.internal.impl.v10.nodes.LibraryNode;
import org.raml.v2.internal.impl.v10.nodes.LibraryRefNode;
import org.raml.v2.internal.impl.v10.nodes.NamedTypeExpressionNode;
import org.raml.v2.internal.impl.v10.nodes.NativeTypeExpressionNode;
import org.raml.v2.internal.impl.v10.nodes.PropertyNode;
import org.raml.v2.internal.impl.v10.nodes.UnionTypeExpressionNode;
import org.raml.yagi.framework.nodes.KeyValueNode;
import org.raml.yagi.framework.nodes.Node;
import org.raml.yagi.framework.nodes.ObjectNode;
import org.raml.yagi.framework.nodes.SimpleTypeNode;

public class TopLevelRamlModelBuilder {

	private static final String FACETS = "facets";
	private static final String PROPERTIES = "properties";
	private static final String USES = "uses";
	private static final String TYPES = "types";
	private static final String ANNOTATIONTYPES = "annotationTypes";

	protected Map<Object, LibraryImpl> ramlGraph = new HashMap<>();

	public LibraryImpl buildUsesMaps(Node node) {
		if (ramlGraph.containsKey(id(node))) {
			return ramlGraph.get(id(node));
		}
		LibraryImpl result = new LibraryImpl(node);
		ramlGraph.put(id(node), result);
		Optional<Node> value = getValue(node, USES);
		value.ifPresent(x -> x.getChildren().forEach(l -> {
			LibraryNode used = (LibraryNode) l;
			String namespace = used.getName();
			Node c = used.getContextNode();
			if (c instanceof LibraryLinkNode) {
				LibraryLinkNode link = (LibraryLinkNode) c;
				Node libraryContent = link.getRefNode();
				LibraryImpl impl = buildUsesMaps(libraryContent);
				result.usesMap.put(namespace, impl);
			}
		}));
		value = getValue(node, TYPES);
		value.ifPresent(x -> x.getChildren().forEach(l -> {
			TypeDeclarationField t = (TypeDeclarationField) l;
			// result.topLevelTypes.put(t.getName(), BuiltIns.NOTHING);
			result.typeDecls.put(t.getName(), (TypeDeclarationNode) t.getValue());
		}));
		value = getValue(node, ANNOTATIONTYPES);
		value.ifPresent(x -> x.getChildren().forEach(l -> {
			AnnotationTypeNode t = (AnnotationTypeNode) l;			
			result.atypeDecls.put(t.getName(), (TypeDeclarationNode) t.getValue());

		}));
		return result;
	}

	protected void buildTypes(TopLevelRamlImpl node) {
		node.usesMap.values().forEach(x -> buildTypes(x));
		node.typeDecls.keySet().forEach(x -> resolveType(node, x));
		node.atypeDecls.keySet().forEach(x -> buildType(node, x,node.atypeDecls.get(x)));
		for (AbstractType t: node.types()){
			finishAnnotationBinding(node, t);
		}
		for (AbstractType t: node.annotationTypes()){
			finishAnnotationBinding(node, t);
		}
	}

	private void finishAnnotationBinding(TopLevelRamlImpl node, AbstractType t) {
		for (TypeInformation i:t.declaredMeta()){
			if (i instanceof Annotation){
				Annotation a=(Annotation) i;
				String name = a.getName();
				String namespace=null;
				int indexOf = name.indexOf('.');
				if (indexOf!=-1){
					namespace=name.substring(0, indexOf);
					name=name.substring(indexOf+1);
				}
				TopLevelRamlImpl ti=node;
				if (namespace!=null){
					ti=node.usesMap.get(namespace);
				}
				a.setAnnotationType(ti.annotationTypes().getType(name));
			}
		}
	}

	private AbstractType buildSuperType(TopLevelRamlImpl node, TypeExpressionNode tn) {
		if (tn instanceof ArrayTypeExpressionNode) {
			ArrayTypeExpressionNode at = (ArrayTypeExpressionNode) tn;
			return TypeOps.array(buildSuperType(node, at.of()));
		}
		if (tn instanceof UnionTypeExpressionNode) {
			UnionTypeExpressionNode un = (UnionTypeExpressionNode) tn;
			List<TypeExpressionNode> of = un.of();
			Stream<AbstractType> map = of.stream().map(t -> buildSuperType(node, t));
			return TypeOps.union("", Arrays.asList(map.toArray()).toArray(new AbstractType[(int) map.count()]));
		}
		if (tn instanceof NativeTypeExpressionNode) {
			return BuiltIns.getBuiltInTypes().getType(tn.getTypeExpressionText());
		}
		if (tn instanceof NamedTypeExpressionNode) {
			NamedTypeExpressionNode namedNode = (NamedTypeExpressionNode) tn;
			String nameSpace = null;
			if (!namedNode.getChildren().isEmpty()) {
				nameSpace = ((LibraryRefNode) namedNode.getChildren().get(0)).getRefName();
			}
			String typeName = namedNode.getLiteralValue();
			if (nameSpace != null) {
				return resolveType(node.usesMap.get(nameSpace), typeName);
			}
			return resolveType(node, typeName);
		}
		if (tn instanceof TypeDeclarationNode) {
			// should not pass here;
			throw new IllegalStateException();
		}
		if (tn instanceof ExternalSchemaTypeExpressionNode) {
			ExternalSchemaTypeExpressionNode en = (ExternalSchemaTypeExpressionNode) tn;
			// TODO FIX ME
			return TypeOps.derive(en.getSchemaValue(), BuiltIns.EXTERNAL);
		}
		throw new IllegalStateException();
	}

	private AbstractType resolveType(TopLevelRamlImpl topLevelRamlImpl, String typeName) {
		if (topLevelRamlImpl.topLevelTypes.hasDeclaration(typeName)) {
			return topLevelRamlImpl.topLevelTypes.getType(typeName);
		}
		AbstractType abstractType = buildType(topLevelRamlImpl, typeName);
		return abstractType;
	}

	private AbstractType buildType(TopLevelRamlImpl topLevelRamlImpl, String typeName) {
		TypeDeclarationNode ts = findDeclaration(topLevelRamlImpl, typeName);
		return buildType(topLevelRamlImpl, typeName, ts);
	}

	private AbstractType buildType(TopLevelRamlImpl topLevelRamlImpl, String typeName, TypeDeclarationNode ts) {
		List<TypeExpressionNode> baseTypes = ts.getBaseTypes();
		ArrayList<AbstractType> superTypes = new ArrayList<>();
		for (TypeExpressionNode n : baseTypes) {
			AbstractType superType = buildSuperType(topLevelRamlImpl, n);
			superTypes.add(superType);
		}
		AbstractType result = TypeOps.derive(typeName, superTypes.toArray(new AbstractType[superTypes.size()]));
		if (ts.getParent() instanceof AnnotationTypeNode){
			topLevelRamlImpl.annotationTypes.registerType(result);
		}
		else{
			topLevelRamlImpl.topLevelTypes.registerType(result);
		}
		List<Node> facets = ts.getChildren();
		for (Node node : facets) {
			if (node instanceof FacetNode) {
				FacetNode n=(FacetNode) node;
				if (n.getName().equals(PROPERTIES)) {
					Node value = n.getValue();
					List<Node> ps = value.getChildren();
					for (Node p : ps) {
						PropertyNode pn = (PropertyNode) p;
						TypeDeclarationNode td = (TypeDeclarationNode) pn.getValue();
						AbstractType buildType = buildType(topLevelRamlImpl, "", td);
						boolean required = pn.isRequired();
						result.declareProperty(pn.getName(), buildType, !required);
					}
					continue;
				}
				
				Node value = n.getValue();
				if (value instanceof SimpleTypeNode<?>) {
					SimpleTypeNode<?> stn = (SimpleTypeNode<?>) value;
					FacetRestriction<?> build = RestrictionsList.build(n.getName(), stn.getValue());
					result.addMeta(build);
				}
			}
			else{				
				KeyValueNode kv=(KeyValueNode) node;
				SimpleTypeNode b=(SimpleTypeNode) kv.getKey();
				String literalValue = b.getLiteralValue();
				if (kv instanceof AnnotationNode){
					Node value = kv.getValue();
					Object val=value;
					if (value instanceof SimpleTypeNode<?>){
						SimpleTypeNode<?>si=(SimpleTypeNode<?>) value;
						val=si.getValue();
					}
					Annotation as=new Annotation(literalValue.substring(1, literalValue.length()-1), val, null);
					result.addMeta(as);
				}
				if (literalValue.equals(FACETS)) {
					Node value = kv.getValue();
					List<Node> ps = value.getChildren();
					for (Node p : ps) {
						CustomFacetDefinitionNode pn = (CustomFacetDefinitionNode) p;
						TypeDeclarationNode td = (TypeDeclarationNode) pn.getValue();
						AbstractType buildType = buildType(topLevelRamlImpl, "", td);
						FacetDeclaration fd=new FacetDeclaration(pn.getFacetName(), buildType);
						result.addMeta(fd);
					}
					continue;
				}
				TypeInformation facet = FacetRegistry.facet(literalValue);
				if (facet instanceof ISimpleFacet){
					ISimpleFacet sf=(ISimpleFacet) facet;					
					Node value = kv.getValue();
					ObjectNode on=(ObjectNode) value;
					if (on.getChildren().size()==1){
						Node n2 = on.getChildren().get(0);
						if (n2 instanceof KeyValueNode){
							KeyValueNode kv2=(KeyValueNode) n2;
							if (kv2.getValue() instanceof SimpleTypeNode<?>){
								SimpleTypeNode<?>vl=(SimpleTypeNode<?>) kv2.getValue();
								sf.setValue(vl.getValue());
								result.addMeta(facet);
							}
						}
					}					
				}				
			}
		}
		return result;
	}

	private TypeDeclarationNode findDeclaration(TopLevelRamlImpl topLevelRamlImpl, String typeName) {
		return topLevelRamlImpl.typeDecls.get(typeName);
	}

	public TopLevelRamlImpl build(Node node) {
		TopLevelRamlImpl n = buildUsesMaps(node);
		buildTypes(n);
		return n;
	}

	private Object id(Node node) {
		return node;
	}

	Optional<Node> getValue(Node n, String path) {
		Node r = n.get(path);
		return Optional.ofNullable(r);
	}
}