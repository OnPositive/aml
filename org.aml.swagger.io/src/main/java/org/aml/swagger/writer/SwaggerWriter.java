package org.aml.swagger.writer;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aml.apimodel.Action;
import org.aml.apimodel.Annotable;
import org.aml.apimodel.Api;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.ParameterLocation;
import org.aml.apimodel.Resource;
import org.aml.apimodel.Response;
import org.aml.apimodel.SecuredByConfig;
import org.aml.apimodel.SecurityScheme;
import org.aml.apimodel.impl.MimeTypeImpl;
import org.aml.apimodel.impl.NamedParamImpl;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.beans.IPropertyView;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.XMLFacet;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.meta.restrictions.HasPropertyRestriction;
import org.aml.typesystem.meta.restrictions.PropertyIs;
import org.aml.typesystem.ramlreader.NamedParam;
import org.aml.typesystem.yamlwriter.GenericWriter;
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
public class SwaggerWriter extends GenericWriter {

	private static final String ANNOTATION_TYPES = "x-annotationTypes";
	private static final String ITEMS = "items";
	private static final String PROPERTIES = "properties";
	private static final String TYPES = "definitions";
	private static final String TYPE = "type";

	static HashSet<String> skipFacets = new HashSet<>();

	static {
		skipFacets.add("displayName");
	}

	protected LinkedHashMap<String, Object> dumpType(AbstractType t) {
		LinkedHashMap<String, Object> result = new LinkedHashMap<>();
		Set<AbstractType> superTypes = t.superTypes();
		if (t.isUnion()) {
			System.err.println("Swagger can not represent union types correctly - ignoring type");
			return result;
		} else {
			if (superTypes.size() > 0) {
				if (superTypes.size() == 1) {
					if (t.isEffectivelyEmptyType() && !t.superType().isBuiltIn()) {
						String name = t.superType().name();
						if (t.superType().isArray()) {
							AbstractType componentType = t.superType().componentType();
							result.put("type", "array");
							result.put("items", typeRespresentation(componentType, true));
							return result;
						}
						result.put("$ref", "#/definitions/" + name);
						return result;
					} else {
						if (!t.superType().isBuiltIn()) {
							ArrayList<Object> ts = new ArrayList<>();
							ts.add(typeRespresentation(t.superType(), true));
							result.put("allOf", ts);
						} else {
							String name = superTypes.iterator().next().name();
							result.put(TYPE, name);
						}
					}
				} else {
					ArrayList<Object> types = new ArrayList<>();
					for (AbstractType ts : superTypes) {
						types.add(typeRespresentation(ts, true));
					}
					if (types.size() > 0) {
						result.put("allOf", types);
					}
				}
			}
		}
		if (t.isSubTypeOf(BuiltIns.OBJECT)) {
			IPropertyView propertiesView = t.toPropertiesView();
			LinkedHashMap<String, Object> dumpedProps = new LinkedHashMap<>();
			ArrayList<String> required = new ArrayList<>();
			for (IProperty p : propertiesView.properties()) {
				Object vl = null;
				vl = typeRespresentation(p.range(), true);
				String id = p.id();
				if (p.isRequired()) {
					required.add(p.id());
				}

				if (p.isAdditional()) {
					result.put("additionalProperties", vl);
				} else if (p.isMap()) {
					System.err.println("pattern properties are not supported in swagger");
				} else {
					dumpedProps.put(id, vl);
				}
			}
			if (!dumpedProps.isEmpty()) {
				result.put(PROPERTIES, dumpedProps);
			}
			if (!required.isEmpty()) {
				result.put("required", required);
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
				if (skipFacets.contains(fs.facetName())) {
					continue;
				}
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
		if (p.isAnonimous() || !allowNamed) {
			HashMap<String, Object> dumpType = dumpType(p);
			vl = dumpType;
		} else {
			String name = p.name();
			vl = toRef(name);
		}
		return vl;
	}

	private LinkedHashMap<String, Object> toRef(String name) {
		LinkedHashMap<String, Object> ref = new LinkedHashMap<>();
		ref.put("$ref", "#/definitions/" + name);
		return ref;
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

	protected String dumpMap(LinkedHashMap<Object, Object> toStore) {
		DumperOptions dumperOptions = new DumperOptions();
		toStore = cleanMap(toStore);
		dumperOptions.setDefaultFlowStyle(FlowStyle.BLOCK);
		dumperOptions.setAllowUnicode(true);
		Yaml rl = new Yaml(dumperOptions);
		StringWriter stringWriter = new StringWriter();
		BufferedWriter ws = new BufferedWriter(stringWriter);
		try {
			ws.newLine();
			rl.dump(toStore, ws);
			return stringWriter.toString().replaceAll(NOVALUE, "");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private LinkedHashMap<Object, Object> cleanMap(Map<Object, Object> toStore) {
		LinkedHashMap<Object, Object> result = new LinkedHashMap<>();
		for (Object k : toStore.keySet()) {
			if (k instanceof String) {
				k = cleanupStringValue((String) k);
			}
			Object object = toStore.get(k);
			object = cleanObject(object);
			result.put(k, object);
		}
		return result;
	}

	protected void dumpResources(List<Resource> res, LinkedHashMap<String, Object> map) {
		for (Resource r : res) {
			map.put(r.relativeUri(), dumpResource(r));
		}
	}

	/**
	 * tags [string] A list of tags for API documentation control. Tags can be
	 * used for logical grouping of operations by resources or any other
	 * qualifier. externalDocs External Documentation Object Additional external
	 * documentation for this operation. operationId string Unique string used
	 * to identify the operation. The id MUST be unique among all operations
	 * described in the API. Tools and libraries MAY use the operationId to
	 * uniquely identify an operation, therefore, it is recommended to follow
	 * common programming naming conventions. deprecated boolean Declares this
	 * operation to be deprecated. Usage of the declared operation should be
	 * refrained. Default value is false.
	 * 
	 * @param a
	 * @return
	 */
	private LinkedHashMap<String, Object> dumpMethod(Action a) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		addScalarField("summary", mp, a, a::displayName);
		addScalarField("description", mp, a, a::description);
		addScalarField("schemes", mp, a, a::protocols);

		ArrayList<INamedParam> value = new ArrayList<>(a.parameters());
		List<MimeType> body = a.body();
		LinkedHashSet<String> consumes = new LinkedHashSet<>();
		LinkedHashSet<String> produces = new LinkedHashSet<>();
		boolean added = false;
		for (MimeType m : body) {
			consumes.add(m.getType());
			List<INamedParam> formParameters = m.getFormParameters();
			if (formParameters != null && !formParameters.isEmpty()) {
				if (!added) {
					value.addAll(formParameters);
					added = true;
				}
			}
			AbstractType typeModel = ((MimeTypeImpl) m).getPlainModel();
			typeModel = typeModel.clone("");
			NamedParam e = new NamedParam(typeModel, true, false);
			e.setLocation(ParameterLocation.BODY);
			if (!added) {
				added = true;
				value.add(e);
			}
		}
		if (body.size() > 1) {
			// TODO FIX ME
			System.err.println("Warning, multiple bodies are not supported in swagger:" + a.resource().getUri() + "."
					+ a.method());
		}
		dumpParameters(value, mp);
		a.responses().forEach(r -> r.body().forEach(m -> produces.add(m.getType())));
		addScalarField("consumes", mp, consumes, () -> consumes);
		addScalarField("produces", mp, consumes, () -> produces);
		dumpCollection("responses", mp, a.responses(), this::dumpResponse, (k) -> Integer.parseInt(k.code()));
		if (!mp.containsKey("responses")) {
			LinkedHashMap<Object, Object> res = new LinkedHashMap<>();
			res.put("description", "default response");
			LinkedHashMap<Object, Object> value2 = new LinkedHashMap<>();
			value2.put("default", res);
			mp.put("responses", value2);
		}
		this.dumpSecuredBy(mp, a.securedBy());
		addAnnotations(a, mp);
		return mp;
	}

	@SuppressWarnings("unchecked")
	protected void dumpParameters(List<? extends INamedParam> ps, Map<String, Object> target) {
		ArrayList<Object> result = new ArrayList<>();
		for (INamedParam q : ps) {
			LinkedHashMap<String, Object> vl = new LinkedHashMap<>();
			vl.put("name", q.getKey());
			String locationString = "query";
			ParameterLocation location = q.location();
			if (q.isRequired()) {
				vl.put("required", true);
			}
			if (location != null) {
				switch (location) {
				case QUERY:
					locationString = "query";
					break;
				case PATH:
					locationString = "path";
					vl.put("required", true);
					break;
				case HEADER:
					locationString = "header";
					break;
				case FORM:
					locationString = "formData";
					break;
				case BODY:
					locationString = "body";
					vl.put("name", "body");
					vl.put("in", locationString);
					vl.put("schema", (Map<? extends String, ? extends Object>) typeRespresentation(
							((NamedParamImpl) q).getTypeModel(), true));
					result.add(vl);
					continue;
				default:
					break;
				}
			}
			vl.put("in", locationString);
			vl.putAll((Map<? extends String, ? extends Object>) dumpNamedParam(q));
			result.add(vl);
		}
		if (!ps.isEmpty()) {
			target.put("parameters", result);
		}
	}

	// private LinkedHashMap<String, Object> dumpTrait(Trait a) {
	// LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
	// dumpCollection("securedBy", mp, a.securedBy(), this::dumpSecuredBy, s ->
	// s.name());
	// addScalarField("description", mp, a, a::description);
	// addScalarField("displayName", mp, a, a::displayName);
	// addScalarField("is", mp, a, a::getIs);
	// dumpCollection("queryParameters", mp, a.queryParameters(),
	// this::dumpNamedParam, this::typeKey);
	// dumpCollection("headers", mp, a.headers(), this::dumpNamedParam,
	// this::typeKey);
	// dumpCollection("body", mp, a.body(), this::dumpMimeType, (k) ->
	// k.getType());
	// dumpCollection("responses", mp, a.responses(), this::dumpResponse, (k) ->
	// Integer.parseInt(k.code()));
	// addAnnotations(a, mp);
	// return mp;
	// }

	@SuppressWarnings("unchecked")
	private void dumpSecuredBy(LinkedHashMap<String, Object> target, List<SecuredByConfig> r) {
		LinkedHashMap<String, Object> rs = new LinkedHashMap<>();
		if (r != null) {
			for (SecuredByConfig c : r) {
				ArrayList<String> settings = new ArrayList<>();
				if (c.settings().containsKey("scopes")) {
					settings.addAll((Collection<? extends String>) c.settings().get("scopes"));
				}
				rs.put(c.name(), settings);
			}

		}
		if (!rs.isEmpty()) {
			target.put("security", rs);
		}
	}

	/**
	 * 
	 * examples Example Object An example
	 * 
	 * @param r
	 * @return
	 */
	private LinkedHashMap<String, Object> dumpResponse(Response r) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		String d = r.description() != null && !r.description().isEmpty() ? r.description() : r.code();
		addScalarField("description", mp, r, () -> d);
		dumpCollection("headers", mp, r.headers(), this::dumpNamedParam, this::typeKey);
		List<MimeType> body = r.body();
		if (body != null && !body.isEmpty()) {
			for (MimeType m : body) {
				LinkedHashMap<String, Object> dumpType = this.dumpType(m.getTypeModel());
				mp.put("schema", dumpType);
				break;
			}
			if (body.size() > 1) {
				System.err.println("Warning - response has more then one body:" + r.code());
			}
		}
		addAnnotations(r, mp);

		return mp;
	}

	private String typeKey(INamedParam k) {
		return k.getKey() + (k.isRequired() ? "" : "?");
	}

	// private Object dumpDocumentationItem(DocumentationItem r) {
	// LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
	// mp.put("title", r.getTitle());
	// mp.put("content", cleanupStringValue(r.getContent()));
	// return mp;
	// }
	//
	// private Object dumpDocumentationItems(List<DocumentationItem> r) {
	// ArrayList<Object> results = new ArrayList<>();
	// for (DocumentationItem i : r) {
	// results.add(dumpDocumentationItem(i));
	// }
	// return results;
	// }

	private LinkedHashMap<String, Object> dumpResource(Resource r) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		dumpParameters(r.allUriParameters(), mp);
		for (Action a : r.methods()) {
			mp.put(a.method(), dumpMethod(a));
		}
		addAnnotations(r, mp);
		return mp;
	}

	private LinkedHashMap<String, Object> dumpSecurityScheme(SecurityScheme x) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<>();
		if (x.type().equals("OAuth 2.0")) {			
			mp.put("authorizationUrl",((String) x.settings().get("authorizationUri")));
			mp.put("tokenUrl",((String) x.settings().get("accessTokenUri")));
			String object = (String) x.settings().get("authorizationGrants");
			// "implicit", "password", "application" or "accessCode".
			// authorization_code, password, client_credentials, or implicit;
			if (object != null) {
				if (object.equals("authorization_code")) {
					mp.put("flow","accessCode");
				} else if (object.equals("implicit")) {
					mp.put("flow","implicit");					
				} else {
					System.err.println("Can not map authorizationGrants:"+object);
				}
			}
			List<?>scopes=(List<?>) x.settings().get("scopes");
			if (scopes!=null&&!scopes.isEmpty()){
				LinkedHashMap<String, String>scopesMap=new LinkedHashMap<>();
				for (Object c:scopes){
					scopesMap.put(c.toString(), "");
				}
				mp.put("scopes",scopesMap);
			}
		}
		else if (x.type().equals("Basic Authentication")){
			mp.put("type","basic");
		}
		else if (x.type().equals("Pass Through")){
			mp.put("type", "apiKey");
		}	
		else{
			System.err.println("Can not accurately convert security scheme");
		}
		addScalarField("description", mp, x, x::description);
		addAnnotations(x, mp);			
		return mp;
	}

	@SuppressWarnings("unchecked")
	public String store(Api model) {
		LinkedHashMap<String, Object> toStore = new LinkedHashMap<>();
		toStore.put("swagger", "2.0");
		addInfoObject(model, toStore);
		addScalarField("schemes", toStore, model,
				() -> model.getProtocols().stream().map(x -> x.toLowerCase()).toArray());
		addScalarField("consumes", toStore, model, model::getMediaType);
		addScalarField("produces", toStore, model, model::getMediaType);		
		String baseUrl = model.getBaseUrl();
		if (baseUrl!=null){
			try {
				URL url = new URL(baseUrl);
				toStore.put("host", url.getHost());
				toStore.put("basePath", url.getPath());
			} catch (MalformedURLException e) {
				System.err.println("Mailformed base url:"+baseUrl);
			}
		}		
		addAnnotations(model, toStore);
		dumpCollection("securityDefinitions", toStore,model.securityDefinitions(), this::dumpSecurityScheme, x->x.name());
		dumpSecuredBy(toStore, model.getSecuredBy());
		dumpTypes(model.types(), model.annotationTypes(), (LinkedHashMap<Object, Object>) (Map<?, ?>) toStore);
		LinkedHashMap<String, Object> paths = new LinkedHashMap<>();
		dumpResources(model.allResources(), paths);
		toStore.put("paths", paths);
		return dumpMap((LinkedHashMap<Object, Object>) (Map<?, ?>) toStore);
	}

	/**
	 * dumps info object
	 * 
	 * @param model
	 * @param toStore
	 */
	protected void addInfoObject(Api model, LinkedHashMap<String, Object> toStore) {
		LinkedHashMap<String, Object> info = new LinkedHashMap<>();
		addScalarField("title", info, model, model::title);
		addScalarField("description", info, model, model::description);
		addScalarField("version", info, model, model::getVersion);
		toStore.put("info", info);
	}

	/**
	 * dumps annotations of element
	 * 
	 * @param model
	 * @param toStore
	 */
	private void addAnnotations(Annotable model, LinkedHashMap<String, Object> toStore) {
		for (Annotation a : model.annotations()) {
			toStore.put("x-" + a.facetName(), a.value());
		}
	}
}