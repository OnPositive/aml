package org.aml.typesystem.yamlwriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aml.apimodel.Action;
import org.aml.apimodel.Annotable;
import org.aml.apimodel.DocumentationItem;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MethodBase;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.Resource;
import org.aml.apimodel.Response;
import org.aml.apimodel.SecuredByConfig;
import org.aml.apimodel.SecurityScheme;
import org.aml.apimodel.Trait;
import org.aml.apimodel.impl.ApiImpl;
import org.aml.apimodel.impl.MimeTypeImpl;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.TypeRegistryImpl;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.beans.IPropertyView;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.XMLFacet;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.meta.restrictions.HasPropertyRestriction;
import org.aml.typesystem.meta.restrictions.PropertyIs;

/**
 * <p>
 * RamlWriter class.
 * </p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class RamlWriter extends GenericWriter {

	private static final String ANNOTATION_TYPES = "annotationTypes";
	private static final String RAML_1_0_LIBRARY = "#%RAML 1.0 Library";
	private static final String RAML_1_0_API = "#%RAML 1.0";
	private static final String ITEMS = "items";
	private static final String PROPERTIES = "properties";
	private static final String TYPES = "types";
	private static final String TYPE = "type";

	/**
	 * <p>
	 * store.
	 * </p>
	 *
	 * @param deriveObjectType
	 *            a {@link org.aml.typesystem.AbstractType} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String store(AbstractType deriveObjectType) {
		TypeRegistryImpl impl = new TypeRegistryImpl(BuiltIns.getBuiltInTypes());
		impl.registerType(deriveObjectType);
		return store(impl, new TypeRegistryImpl(BuiltIns.getBuiltInTypes()));
	}

	protected LinkedHashMap<String, Object> dumpType(AbstractType t) {
		LinkedHashMap<String, Object> result = new LinkedHashMap<>();
		Set<AbstractType> superTypes = t.superTypes();
		if (superTypes.size() > 0) {
			if (superTypes.size() == 1) {
				String name = superTypes.iterator().next().name();
				if (t.isNullable()) {
					name = name + " | nil";
				}
				result.put(TYPE, name);
			} else {
				ArrayList<String> types = new ArrayList<>();
				for (AbstractType ts : superTypes) {
					String name = ts.name();
					if (t.isNullable()) {
						name = name + " | nil";
					}
					types.add(name);
				}
				result.put(TYPE, types);
			}
		}
		if (t.isSubTypeOf(BuiltIns.OBJECT)) {
			IPropertyView propertiesView = t.toPropertiesView();
			LinkedHashMap<String, Object> dumpedProps = new LinkedHashMap<>();
			for (IProperty p : propertiesView.properties()) {
				Object vl = null;
				vl = typeRespresentation(p.range(), true);
				String id = p.id();
				if (!p.isRequired()) {
					id = id + "?";
				}
				dumpedProps.put(id, vl);
			}
			if (!dumpedProps.isEmpty()) {
				result.put(PROPERTIES, dumpedProps);
			}
		}
		Set<TypeInformation> meta = t.declaredMeta();
		for (TypeInformation ti : meta) {
			if (ti instanceof PropertyIs) {
				continue;
			}
			if (ti instanceof HasPropertyRestriction) {
				continue;
			}
			if (ti instanceof ComponentShouldBeOfType) {
				ComponentShouldBeOfType cs = (ComponentShouldBeOfType) ti;
				result.put(ITEMS, typeRespresentation(cs.range(), true));
			}

			if (ti instanceof ISimpleFacet) {
				ISimpleFacet fs = (ISimpleFacet) ti;
				Object value = fs.value();
				if (value instanceof Map) {
					Map<?, ?> mm = (Map<?, ?>) value;
					if (mm.isEmpty()) {
						value = NOVALUE;
					}
				}
				if (value instanceof String) {
					value = cleanupStringValue((String) value);
				}
				result.put(fs.facetName(), value);
			} else {
				if (ti instanceof XMLFacet) {
					result.put("xml", toMap(ti));
				}
			}
		}
		return result;
	}

	protected Object typeRespresentation(AbstractType p, boolean allowNamed) {
		Object vl;
		if (p.isAnonimous()) {
			if (p.isArray()) {
				if (p.declaredMeta().size() == 1) {
					ComponentShouldBeOfType oneMeta = p.oneMeta(ComponentShouldBeOfType.class);
					if (oneMeta != null) {
						Object typeRespresentation = typeRespresentation(oneMeta.range(), allowNamed);
						if (typeRespresentation instanceof String) {
							return typeRespresentation.toString() + "[]";
						}
					}
				}
			}
			HashMap<String, Object> dumpType = dumpType(p);
			vl = dumpType;
		} else {
			String name = p.name();
			vl = name;
			if (p.isNullable()) {
				return vl + " | nil";
			}
		}
		return vl;
	}

	/**
	 * <p>
	 * store.
	 * </p>
	 *
	 * @param registry
	 *            a {@link org.aml.typesystem.ITypeRegistry} object.
	 * @param ar
	 *            a {@link org.aml.typesystem.ITypeRegistry} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String store(ITypeRegistry registry, ITypeRegistry ar) {
		LinkedHashMap<Object, Object> toStore = new LinkedHashMap<>();
		dumpTypes(registry, ar, toStore);
		String header = RAML_1_0_LIBRARY;
		return dumpMap(toStore, header);
	}

	protected void dumpTypes(ITypeRegistry registry, ITypeRegistry ar, LinkedHashMap<Object, Object> toStore) {
		Collection<AbstractType> types2 = registry.types();
		LinkedHashMap<String, Object> atps = fillRromList(ar.types());
		if (!atps.isEmpty()) {
			toStore.put(ANNOTATION_TYPES, atps);
		}
		types2 = registry.types();
		LinkedHashMap<String, Object> tps = fillRromList(types2);
		if (!tps.isEmpty()) {
			toStore.put(TYPES, tps);
		}
	}

	protected void dumpResources(List<Resource> res, LinkedHashMap<String, Object> map) {
		for (Resource r : res) {
			map.put(r.relativeUri(), dumpResource(r));
		}
	}

	private LinkedHashMap<String, Object> dumpMethod(MethodBase a) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		dumpCollection("securedBy", mp, a.securedBy(), this::dumpSecuredBy, s -> s.name());
		addScalarField("description", mp, a, a::description);
		addScalarField("displayName", mp, a, a::displayName);
		addScalarField("is", mp, a, a::getIs);
		addScalarField("protocols", mp, a, a::protocols);
		dumpCollection("queryParameters", mp, a.queryParameters(), this::dumpNamedParam, this::typeKey);
		dumpCollection("headers", mp, a.headers(), this::dumpNamedParam, this::typeKey);
		dumpCollection("body", mp, a.body(), this::dumpMimeType, (k) -> k.getType());
		dumpCollection("responses", mp, a.responses(), this::dumpResponse, (k) -> Integer.parseInt(k.code()));
		addAnnotations(a, mp);
		return mp;
	}

	private LinkedHashMap<String, Object> dumpTrait(Trait a) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		dumpCollection("securedBy", mp, a.securedBy(), this::dumpSecuredBy, s -> s.name());
		addScalarField("description", mp, a, a::description);
		addScalarField("displayName", mp, a, a::displayName);
		addScalarField("is", mp, a, a::getIs);
		dumpCollection("queryParameters", mp, a.queryParameters(), this::dumpNamedParam, this::typeKey);
		dumpCollection("headers", mp, a.headers(), this::dumpNamedParam, this::typeKey);
		dumpCollection("body", mp, a.body(), this::dumpMimeType, (k) -> k.getType());
		dumpCollection("responses", mp, a.responses(), this::dumpResponse, (k) -> Integer.parseInt(k.code()));
		addAnnotations(a, mp);
		return mp;
	}

	private LinkedHashMap<String, Object> dumpSecuredBy(SecuredByConfig r) {
		return r.settings();
	}

	private LinkedHashMap<String, Object> dumpResponse(Response r) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		addScalarField("description", mp, r, r::description);
		dumpCollection("headers", mp, r.headers(), this::dumpNamedParam, this::typeKey);
		dumpCollection("body", mp, r.body(), this::dumpMimeType, (k) -> k.getType());
		addAnnotations(r, mp);
		return mp;
	}

	private String typeKey(INamedParam k) {
		return k.getKey() + (k.isRequired() ? "" : "?");
	}

	private Object dumpMimeType(MimeType r) {
		AbstractType typeModel = ((MimeTypeImpl) r).getPlainModel();
		if (typeModel != null) {
			return typeRespresentation(typeModel, true);
		} else {
			return null;
		}
	}

	private Object dumpDocumentationItem(DocumentationItem r) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		mp.put("title", r.getTitle());
		mp.put("content", cleanupStringValue(r.getContent()));
		return mp;
	}

	private Object dumpDocumentationItems(List<DocumentationItem> r) {
		ArrayList<Object> results = new ArrayList<>();
		for (DocumentationItem i : r) {
			results.add(dumpDocumentationItem(i));
		}
		return results;
	}

	private LinkedHashMap<String, Object> dumpResource(Resource r) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		dumpCollection("uriParameters", mp, r.uriParameters(), this::dumpNamedParam, this::typeKey);
		addScalarField("description", mp, r, r::description);
		addScalarField("displayName", mp, r, r::displayName);
		dumpResources(r.resources(), mp);
		for (Action a : r.methods()) {
			mp.put(a.method(), dumpMethod(a));
		}
		addAnnotations(r, mp);
		return mp;
	}

	private LinkedHashMap<String, Object> dumpSecurityScheme(SecurityScheme r) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		mp.put("type", r.type());
		addScalarField("description", mp, r, r::description);
		if (r.settings() != null && !r.settings().isEmpty()) {
			mp.put("settings", r.settings());
		}
		if (r.describedBy() != null) {
			LinkedHashMap<String, Object> dumpMethod = dumpMethod(r.describedBy());
			mp.put("describedBy", dumpMethod);
		}
		addAnnotations(r, mp);
		return mp;

	}

	@SuppressWarnings("unchecked")
	public String store(ApiImpl model) {
		LinkedHashMap<String, Object> toStore = new LinkedHashMap<>();
		addScalarField("title", toStore, model, model::getTitle);
		addScalarField("version", toStore, model, model::getVersion);
		addScalarField("baseUri", toStore, model, model::getBaseUrl);
		addScalarField("description", toStore, model, model::description);
		addScalarField("mediaType", toStore, model, model::getMediaType);
		addScalarField("protocol", toStore, model, model::getProtocols);
		if (!model.getUsesLocations().isEmpty()) {
			toStore.put("uses", model.getUsesLocations());
		}
		addAnnotations(model, toStore);
		dumpCollection("securitySchemes", toStore, model.securityDefinitions(), this::dumpSecurityScheme,
				s -> s.name());
		dumpCollection("traits", toStore, model.getTraits(), this::dumpTrait, s -> s.name());
		dumpCollection("securedBy", toStore, model.getSecuredBy(), this::dumpSecuredBy, s -> s.name());
		dumpTypes(model.types(), model.annotationTypes(), (LinkedHashMap<Object, Object>) (Map<?, ?>) toStore);
		String header = RAML_1_0_API;
		dumpResources(model.resources(), toStore);
		if (!model.getDocumentation().isEmpty()) {
			toStore.put("documentation", dumpDocumentationItems(model.getDocumentation()));
		}
		return dumpMap((LinkedHashMap<Object, Object>) (Map<?, ?>) toStore, header);
	}

	private void addAnnotations(Annotable model, LinkedHashMap<String, Object> toStore) {
		for (Annotation a : model.annotations()) {
			toStore.put(a.facetName(), a.value());
		}
	}
}