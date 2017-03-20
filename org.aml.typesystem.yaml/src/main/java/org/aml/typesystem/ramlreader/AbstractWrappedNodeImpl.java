package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Annotable;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.ParameterLocation;
import org.aml.apimodel.SecuredByConfig;
import org.aml.apimodel.TopLevelModel;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.raml.v2.internal.impl.commons.nodes.TypeDeclarationNode;
import org.raml.yagi.framework.nodes.ErrorNode;
import org.raml.yagi.framework.nodes.KeyValueNode;
import org.raml.yagi.framework.nodes.Node;
import org.raml.yagi.framework.nodes.SimpleTypeNode;
import org.raml.yagi.framework.nodes.StringNode;

public class AbstractWrappedNodeImpl<P extends Annotable, T extends Node> extends AnnotableImpl {

	protected P parent;
	protected TopLevelModel mdl;

	public AbstractWrappedNodeImpl(TopLevelModel mdl, P parent, T node) {
		super(node);
		this.parent = parent;
		this.mdl = mdl;
	}

	public ArrayList<SecuredByConfig> securedBy() {
		ArrayList<SecuredByConfig> results = new ArrayList<>();
		Node childNodeWithKey = this.getChildNodeWithKey("securedBy");
		if (childNodeWithKey != null) {
			for (Node n : childNodeWithKey.getChildren()) {
				if (n instanceof ErrorNode) {
					ErrorNode zz = (ErrorNode) n;
					Node source = zz.getSource();
					if (source instanceof org.raml.v2.internal.impl.commons.nodes.ParametrizedSecuritySchemeRefNode) {
						n = source;
					}
				}
				results.add(new SecuredByImpl(mdl, this, n));
			}
		}
		return results;
	}

	@Override
	protected TopLevelRamlImpl getTopLevel() {
		return (TopLevelRamlImpl) mdl;
	}

	public String displayName() {
		return this.getChildWithKeyAs("displayName", String.class, this.getKey());
	}

	@SuppressWarnings("unchecked")
	protected List<MimeType> toMimeTypes(Node childNodeWithKey, MethodImpl method) {
		if (childNodeWithKey instanceof TypeDeclarationNode) {
			ArrayList<MimeType> mt = new ArrayList<>();
			
			List<String> mediaType = method.parent.getApi().getMediaType();
			for (String s : mediaType) {
				TopLevelRamlModelBuilder topLevelRamlModelBuilder = new TopLevelRamlModelBuilder();
				AbstractType buildType = topLevelRamlModelBuilder.buildType((TopLevelRamlImpl) mdl, s, childNodeWithKey, false);
				MimeTypeImpl impl=new MimeTypeImpl(buildType,method);
				impl.setDefault(true);
				mt.add(impl);
			}
			return mt;
		}
		List<NamedParam> paramList = (List<NamedParam>) toParamList(childNodeWithKey, false);
		ArrayList<MimeType> mt = new ArrayList<>();
		for (NamedParam p : paramList) {
			MimeTypeImpl e = new MimeTypeImpl(p.getTypeModel(), method);
			e.setName(p.getKey());
			mt.add(e);
		}
		return mt;
	}

	protected List<MimeType> body(MethodImpl method) {
		Node childNodeWithKey = this.getChildNodeWithKey("body");
		return toMimeTypes(childNodeWithKey, method);
	}

	public final List<? extends INamedParam> headers() {
		Node childNodeWithKey = this.getChildNodeWithKey("headers");
		return toParamList(childNodeWithKey, true);
	}

	protected final boolean hasBody(MethodImpl m) {
		return !body(m).isEmpty();
	}

	public String getKey() {
		if (this.original instanceof KeyValueNode) {
			KeyValueNode kv = (KeyValueNode) this.original;
			if (kv.getKey() instanceof SimpleTypeNode<?>) {
				SimpleTypeNode<?> nnm = (SimpleTypeNode<?>) kv.getKey();
				return nnm.getLiteralValue();
			}

		}
		return null;
	}

	protected List<? extends INamedParam> toParamList(Node childNodeWithKey, boolean convertRepeat) {
		ArrayList<NamedParam> prms = new ArrayList<>();
		if (childNodeWithKey == null) {
			return prms;
		}
		List<Node> children = childNodeWithKey.getChildren();

		TopLevelRamlModelBuilder topLevelRamlModelBuilder = new TopLevelRamlModelBuilder();
		for (Node n : children) {
			KeyValueNode kv = (KeyValueNode) n;
			StringNode key = (StringNode) kv.getKey();
			String typeName = key.getLiteralValue();
			boolean required = !typeName.endsWith("?");
			if (!required) {
				typeName = typeName.substring(0, typeName.length() - 1);
			}
			Node value = kv.getValue();
			AbstractType buildType = topLevelRamlModelBuilder.buildType((TopLevelRamlImpl) mdl, typeName, value, false);
			if (buildType==null){
				continue;
			}
			if (buildType.isOptional()) {
				required = false;
			}
			boolean repeat = false;
			if (buildType.isArray() && convertRepeat) {
				repeat = true;
				ComponentShouldBeOfType oneMeta = buildType.oneMeta(ComponentShouldBeOfType.class);
				if (oneMeta != null) {
					AbstractType at = oneMeta.range();
					for (TypeInformation i : buildType.meta()) {
						if (!(i instanceof ComponentShouldBeOfType)) {
							at.addMeta(i);
						}
					}
					buildType = at;
				}
			}
			for (TypeInformation i : buildType.metaInfo) {
				if (i instanceof Annotation) {
					Annotation an = (Annotation) i;
					TopLevelRamlModelBuilder.bindAnnotation(getTopLevel(), an);
				}
			}
			NamedParam result = new NamedParam(buildType.clone(""), required, repeat);
			result.setName(buildType.name());
			Node parent2 = childNodeWithKey.getParent();
			if (parent2 instanceof KeyValueNode) {
				KeyValueNode kk = (KeyValueNode) parent2;
				Node key2 = kk.getKey();
				if (key2 instanceof SimpleTypeNode<?>) {
					SimpleTypeNode<?> st = (SimpleTypeNode<?>) key2;
					String literalValue = st.getLiteralValue();
					if (literalValue.equals("headers")) {
						result.setLocation(ParameterLocation.HEADER);
					} else if (literalValue.equals("uriParameters")) {
						result.setLocation(ParameterLocation.PATH);
					} else if (literalValue.equals("queryParameters")) {
						result.setLocation(ParameterLocation.QUERY);
					} else if (literalValue.equals("formParameters")) {
						result.setLocation(ParameterLocation.FORM);
					}
				}
			}
			prms.add(result);
		}
		return prms;
	}

	public String description() {
		return this.getChildWithKeyAs("description", String.class, null);
	}

}