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
import org.aml.typesystem.meta.restrictions.FacetRestriction;
import org.aml.typesystem.meta.restrictions.RestrictionsList;
import org.raml.v2.internal.impl.commons.nodes.AnnotationTypeNode;
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
import org.raml.yagi.framework.nodes.Node;
import org.raml.yagi.framework.nodes.SimpleTypeNode;

public class TopLevelRamlModelBuilder {

	private static final String FACETS = "facets";
	private static final String PROPERTIES = "properties";
	private static final String USES = "uses";
	private static final String TYPES = "types";
	private static final String ANNOTATIONTYPES = "annotationTypes";

	protected Map<Object, TopLevelRamlImpl> ramlGraph = new HashMap<>();

	public TopLevelRamlImpl buildUsesMaps(Node node) {
		if (ramlGraph.containsKey(id(node))) {
			return ramlGraph.get(id(node));
		}
		TopLevelRamlImpl result = new TopLevelRamlImpl(node);
		ramlGraph.put(id(node), result);
		Optional<Node> value = getValue(node, USES);
		value.ifPresent(x -> x.getChildren().forEach(l -> {
			LibraryNode used = (LibraryNode) l;
			String namespace = used.getName();
			Node c = used.getContextNode();
			if (c instanceof LibraryLinkNode) {
				LibraryLinkNode link = (LibraryLinkNode) c;
				Node libraryContent = link.getRefNode();
				TopLevelRamlImpl impl = buildUsesMaps(libraryContent);
				result.usesMap.put(namespace, impl);
			}
		}));
		value = getValue(node, TYPES);
		value.ifPresent(x -> x.getChildren().forEach(l -> {
			TypeDeclarationField t = (TypeDeclarationField) l;
			//result.topLevelTypes.put(t.getName(), BuiltIns.NOTHING);
			result.typeDecls.put(t.getName(), (TypeDeclarationNode) t.getValue());
		}));
		value = getValue(node, ANNOTATIONTYPES);
		value.ifPresent(x -> x.getChildren().forEach(l -> {
			AnnotationTypeNode t = (AnnotationTypeNode) l;
			//result.annotationTypes.put(t.getName(), BuiltIns.NOTHING);
		}));
		return result;
	}

	protected void buildTypes(TopLevelRamlImpl node) {
		node.usesMap.values().forEach(x -> buildTypes(x));
		node.typeDecls.keySet().forEach(x->resolveType(node, x));
	}

	private AbstractType buildSuperType(TopLevelRamlImpl node, TypeExpressionNode tn) {
		if (tn instanceof ArrayTypeExpressionNode){
			ArrayTypeExpressionNode at=(ArrayTypeExpressionNode) tn;
			return TypeOps.array(buildSuperType(node, at.of()));
		}
		if (tn instanceof UnionTypeExpressionNode){
			UnionTypeExpressionNode un=(UnionTypeExpressionNode) tn;
			List<TypeExpressionNode> of = un.of();
			Stream<AbstractType> map = of.stream().map(t->buildSuperType(node, t));
			return TypeOps.union("",Arrays.asList(map.toArray()).toArray(new AbstractType[(int) map.count()]));
		}
		if (tn instanceof NativeTypeExpressionNode){
			return BuiltIns.getBuiltInTypes().getType(tn.getTypeExpressionText());
		}
		if (tn instanceof NamedTypeExpressionNode){
			NamedTypeExpressionNode namedNode=(NamedTypeExpressionNode) tn;
			String nameSpace=null;
			if (!namedNode.getChildren().isEmpty()){
				nameSpace=((LibraryRefNode)namedNode.getChildren().get(0)).getRefName();			
			}
			String typeName=namedNode.getLiteralValue();
			if (nameSpace!=null){
				return resolveType(node.usesMap.get(nameSpace),typeName);
			}
			return resolveType(node,typeName);	
		}
		if (tn instanceof TypeDeclarationNode){
			//should not pass here;
			throw new IllegalStateException();
		}
		if (tn instanceof ExternalSchemaTypeExpressionNode){
			ExternalSchemaTypeExpressionNode en=(ExternalSchemaTypeExpressionNode) tn;
			//TODO FIX ME
			return TypeOps.derive(en.getSchemaValue(),BuiltIns.EXTERNAL);
		}
		throw new IllegalStateException();
	}

	private AbstractType resolveType(TopLevelRamlImpl topLevelRamlImpl, String typeName) {
		if (topLevelRamlImpl.topLevelTypes.hasDeclaration(typeName)){
			return topLevelRamlImpl.topLevelTypes.getType(typeName);
		}
		AbstractType abstractType=buildType(topLevelRamlImpl,typeName);
		return abstractType;
	}

	private AbstractType buildType(TopLevelRamlImpl topLevelRamlImpl, String typeName) {
		TypeDeclarationNode ts= findDeclaration(topLevelRamlImpl,typeName);
		List<TypeExpressionNode> baseTypes = ts.getBaseTypes();
		ArrayList<AbstractType>superTypes=new ArrayList<>();
		for (TypeExpressionNode n:baseTypes){
			AbstractType superType=buildSuperType(topLevelRamlImpl,n);
			superTypes.add(superType);
		}
		AbstractType result=TypeOps.derive(typeName, superTypes.toArray(new AbstractType[superTypes.size()]));
		topLevelRamlImpl.topLevelTypes.registerType(result);
		List<FacetNode> facets = ts.getFacets();
		for (FacetNode n : facets) {
			if (n.getName().equals(PROPERTIES)) {
				Node value = n.getValue();
				List<Node> ps = value.getChildren();
				for (Node p : ps) {
					PropertyNode pn = (PropertyNode) p;
					result.declareProperty(pn.getName(), BuiltIns.STRING, !pn.isRequired());
					
				}
				continue;
			}
			if (n.getName().equals(FACETS)){
				continue;
			}
			Node value=n.getValue();
			if (value instanceof SimpleTypeNode<?>){
				SimpleTypeNode<?>stn=(SimpleTypeNode<?>) value;
				FacetRestriction<?> build = RestrictionsList.build(n.getName(), stn.getValue());
				result.addMeta(build);
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
