package org.aml.typesystem.yamlwriter;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.aml.apimodel.Action;
import org.aml.apimodel.Annotable;
import org.aml.apimodel.DocumentationItem;
import org.aml.apimodel.INamedParam;
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
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;

/**
 * <p>
 * RamlWriter class.
 * </p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class RamlWriter {

	private static final String NOVALUE = "<<NOVALUE!!!";
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

	LinkedHashMap<String, Object> dumpType(AbstractType t) {
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
				vl = typeRespresentation(p.range());
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
				result.put(ITEMS, typeRespresentation(cs.range()));
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
				result.put(fs.facetName(), value);
			} else {
				if (ti instanceof XMLFacet) {
					result.put("xml", toMap(ti));
				}
			}
		}
		return result;
	}

	LinkedHashMap<String, Object> toMap(Object obj) {
		LinkedHashMap<String, Object> result = new LinkedHashMap<>();
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field f : declaredFields) {
			f.setAccessible(true);
			try {
				Object object = f.get(obj);
				if (object != null && !object.equals(Boolean.FALSE)) {
					result.put(f.getName(), object);
				}
			} catch (Exception e) {
				throw new IllegalStateException();
			}
		}
		return result;
	}

	private Object typeRespresentation(AbstractType p) {
		Object vl;
		if (p.isAnonimous()) {
			if (p.isArray()) {
				if (p.declaredMeta().size() == 1) {
					ComponentShouldBeOfType oneMeta = p.oneMeta(ComponentShouldBeOfType.class);
					if (oneMeta != null) {
						Object typeRespresentation = typeRespresentation(oneMeta.range());
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
		LinkedHashMap<String, Object> toStore = new LinkedHashMap<>();
		dumpTypes(registry, ar, toStore);
		String header = RAML_1_0_LIBRARY;
		return dumpMap(toStore, header);

	}

	protected void dumpTypes(ITypeRegistry registry, ITypeRegistry ar, LinkedHashMap<String, Object> toStore) {
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

	protected String dumpMap(LinkedHashMap<String, Object> toStore, String header) {
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(FlowStyle.BLOCK);
		Yaml rl = new Yaml(dumperOptions);
		StringWriter stringWriter = new StringWriter();
		BufferedWriter ws = new BufferedWriter(stringWriter);
		try {
			ws.write(header);
			ws.newLine();
			rl.dump(toStore, ws);
			return stringWriter.toString().replaceAll(NOVALUE, "");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private LinkedHashMap<String, Object> fillRromList(Collection<AbstractType> types2) {
		LinkedHashMap<String, Object> tps = new LinkedHashMap<>();
		ArrayList<AbstractType> ts = new ArrayList<>(types2);
		Collections.sort(ts, new Comparator<AbstractType>() {

			@Override
			public int compare(AbstractType o1, AbstractType o2) {
				int s1 = 0;
				int s2 = 0;
				if (o1.isObject()) {
					s1 = 1000;
				}
				if (o2.isObject()) {
					s2 = 1000;
				}
				if (s1 == s2) {
					return o1.name().compareTo(o2.name());
				}
				return s1 - s2;
			}
		});
		for (AbstractType t : ts) {
			tps.put(t.name(), dumpType(t));
		}
		return tps;
	}

	protected void dumpResources(List<Resource> res, LinkedHashMap<String, Object> map) {
		for (Resource r : res) {
			map.put(r.relativeUri(), dumpResource(r));
		}
	}

	protected <T> void dumpCollection(String prefix, LinkedHashMap<String, Object> target, Collection<T> value,
			Function<T, Object> func, Function<T, Object> keyFunc) {
		if (!value.isEmpty()) {
			LinkedHashMap<Object, Object> result = new LinkedHashMap<>();
			for (T v : value) {
				Object apply = func.apply(v);
				if (apply instanceof Map) {
					@SuppressWarnings("rawtypes")
					Map m = (Map) apply;
					if (m.isEmpty()) {
						apply = NOVALUE;
					}
				}
				result.put(keyFunc.apply(v), apply);
			}
			target.put(prefix, result);
		}
	}

	private LinkedHashMap<String, Object> dumpMethod(Action a) {
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
		dumpCollection("headers", mp, r.headers(), this::dumpNamedParam, this::typeKey);
		dumpCollection("body", mp, r.body(), this::dumpMimeType, (k) -> k.getType());
		addAnnotations(r, mp);
		return mp;
	}

	private String typeKey(INamedParam k) {
		return k.getKey() + (k.isRequired() ? "" : "?");
	}

	private Object dumpNamedParam(INamedParam r) {
		return typeRespresentation(r.getTypeModel());
	}

	private Object dumpMimeType(MimeType r) {
		AbstractType typeModel = ((MimeTypeImpl) r).getPlainModel();
		return typeRespresentation(typeModel);
	}
	
	private Object dumpDocumentationItem(DocumentationItem r) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		mp.put("title", r.getTitle());
		mp.put("content", r.getContent());
		return mp;
	}
	private Object dumpDocumentationItems(List<DocumentationItem> r) {
		ArrayList<Object>results=new ArrayList<>();
		for (DocumentationItem i:r){
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
		mp.put("settings", r.settings());
		addAnnotations(r, mp);
		return mp;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static void addScalarField(String name, LinkedHashMap tr, Object source, Supplier<Object> acc) {
		Object vl = acc.get();
		if (vl != null) {
			if (vl instanceof Collection){
				Collection<?>c=(Collection<?>) vl;
				if (c.isEmpty()){
					return;
				}
			}
			tr.put(name, vl);
		}
	}

	public String store(ApiImpl model) {
		LinkedHashMap<String, Object> toStore = new LinkedHashMap<>();
		addScalarField("title", toStore, model, model::getTitle);
		addScalarField("version", toStore, model, model::getVersion);
		addScalarField("baseUri", toStore, model, model::getBaseUrl);
		if (!model.getUsesLocations().isEmpty()) {
			toStore.put("uses", model.getUsesLocations());
		}
		addAnnotations(model, toStore);
		dumpCollection("securitySchemes", toStore, model.securityDefinitions(), this::dumpSecurityScheme,
				s -> s.name());
		dumpCollection("traits", toStore, model.getTraits(), this::dumpTrait,
				s -> s.name());
		dumpCollection("securedBy", toStore, model.getSecuredBy(), this::dumpSecuredBy,
				s -> s.name());
		
		dumpTypes(model.types(), model.annotationTypes(), toStore);
		String header = RAML_1_0_API;
		dumpResources(model.resources(), toStore);
		if (!model.getDocumentation().isEmpty()){
			toStore.put("documentation", dumpDocumentationItems(model.getDocumentation()));
		}
		return dumpMap(toStore, header);
	}

	private void addAnnotations(Annotable model, LinkedHashMap<String, Object> toStore) {
		for (Annotation a : model.annotations()) {
			toStore.put(a.facetName(), a.value());
		}
	}
}