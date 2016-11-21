package org.aml.swagger.writer;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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
import org.aml.apimodel.Library;
import org.aml.apimodel.MethodBase;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.ParameterLocation;
import org.aml.apimodel.Resource;
import org.aml.apimodel.Response;
import org.aml.apimodel.SecuredByConfig;
import org.aml.apimodel.SecurityScheme;
import org.aml.apimodel.TopLevelModel;
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

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

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

	private Api api;

	protected LinkedHashMap<String, Object> dumpType(AbstractType t) {
		LinkedHashMap<String, Object> result = new LinkedHashMap<>();
		
		Set<AbstractType> superTypes = t.superTypes();
		if (t.isExternal()){
			System.err.println("Swagger can not represent external types correctly - ignoring type");
			return result;
		}
		if (t.isUnion()) {
			System.err.println("Swagger can not represent union types correctly - ignoring type");
			return result;
		} else {
			if (inParam){
			   result.put(TYPE, "string");
			   if(t.isString()){
				   result.put(TYPE, "string");
			   }
			   if(t.isNumber()){
				   if (t.isInteger()){
					   result.put(TYPE, "integer");   
				   }
				   else{
					   result.put(TYPE, "number");
				   }
			   }
			   if(t.isBoolean()){
				   result.put(TYPE, "boolean");
			   }
			   if (t.isArray()){
				   result.put(TYPE, "array");
			   }
				//this means that we can not use allOf, and $ref to global definition
			}
			else if (superTypes.size() > 0) {
				if (superTypes.size() == 1) {
					if (t.isEffectivelyEmptyType() && !t.superType().isBuiltIn()) {
						while (t.isAnonimous()&&t.hasOnlyDisplayName()&&!t.isBuiltIn()&&!t.isExternal()){
							if (t.superTypes().size()==1){
								t=t.superType();
							}
						}
						if (t.isArray()&&t.isAnonimous()){
							return dumpType(t);
						}
						return toRef(t);
					} else {
						if (!t.superType().isBuiltIn()) {
							ArrayList<Object> ts = new ArrayList<>();
							ts.add(typeRespresentation(t.superType(), true));
							result.put("allOf", ts);
						} else {
							AbstractType next = superTypes.iterator().next();
							String name = next.name();
							if (next==BuiltIns.ANY){
								
							}
							else{
								
								result.put(TYPE, transformName(name));
							}
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

		Set<TypeInformation> meta = inParam?t.meta():t.declaredMeta();

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
				if (fs.facetName().equals("example")){
					if (inParam){
						continue;
					}
				}
				if (value instanceof String) {
					value = cleanupStringValue((String) value);
				}
				if (fs instanceof Annotation){
					Annotation an=(Annotation) fs;
					AbstractType annotationType = an.annotationType();
					if (annotationType!=null){
					result.put("x-"+annotationType.name(), value);
					}
					else{
						result.put("x-"+an.getName(), value);
					}
				}
				else{
				result.put(fs.facetName(), value);
				}
			} else {
				if (ti instanceof XMLFacet) {
					result.put("xml", toMap(ti));
				}
			}
		}
		return result;
	}
	protected static HashSet<String>allowedTypes=new HashSet<>();

	
	static{
		allowedTypes.add("array");
		allowedTypes.add("object");
		allowedTypes.add("string");
		allowedTypes.add("number");
		allowedTypes.add("boolean");
		allowedTypes.add("integer");
	}
	private String transformName(String name) {
		if (!allowedTypes.contains(name)){
			return "string";
		}
		return name;
	}

	protected LinkedHashMap<String, Object>  typeRespresentation(AbstractType p, boolean allowNamed) {
		LinkedHashMap<String, Object>  vl;
		if (p.isAnonimous() || !allowNamed) {
			LinkedHashMap<String, Object> dumpType = dumpType(p);
			vl = dumpType;
		} else {
			vl = toRef(p);
		}
		return vl;
	}

	protected ArrayList<AbstractType> extras = new ArrayList<>();

	protected String findPath(TopLevelModel m, AbstractType t) {
		if (m.types().getType(t.name()) != null) {
			return "";
		}
		for (String s : m.uses().keySet()) {
			Library library = m.uses().get(s);
			if (library != null) {
				String findPath = findPath(library, t);
				if (findPath != null) {
					if (findPath.isEmpty()) {
						return s;
					}
					return s + "." + findPath;
				}
			}
		}
		return null;
	}

	private LinkedHashMap<String, Object> toRef(AbstractType t) {
		if (t.isBuiltIn()){
			if (t==BuiltIns.ANY){
				return new LinkedHashMap<>();
			}
		}
		if (t.getSource() != api) {
			extras.add(t);
			String findPath = findPath(api, t);
			if (findPath == null) {
				System.err.println("Can not find path to type:" + t.name());
			}
			LinkedHashMap<String, Object> ref = new LinkedHashMap<>();
			ref.put("$ref", "#/definitions/" + findPath +"."+ t.name());
			return ref;
		}
		LinkedHashMap<String, Object> ref = new LinkedHashMap<>();
		ref.put("$ref", "#/definitions/" + t.name());
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
			map.put(r.getUri(), dumpResource(r));
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
		HashSet<AbstractType>tps=new HashSet<>();
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
			if (typeModel.isEffectivelyEmptyType()){
				typeModel=typeModel.superType();
			}
			tps.add(typeModel);
		}
		if (tps.size() > 1) {
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
		ArrayList<Object>secured=new ArrayList<>();
		if (r != null) {
			for (SecuredByConfig c : r) {
				LinkedHashMap<String, Object> rs = new LinkedHashMap<>();
				ArrayList<String> settings = new ArrayList<>();
				if (c.settings().containsKey("scopes")) {
					settings.addAll((Collection<? extends String>) c.settings().get("scopes"));
				}
				rs.put(c.name(), settings);
				secured.add(rs);
			}

		}
		if (!secured.isEmpty()) {
			target.put("security", secured);
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
		HashSet<AbstractType>tps=new HashSet<>();
		if (body != null && !body.isEmpty()) {
			for (MimeType m : body) {
				
				AbstractType typeModel = m.getTypeModel();
				LinkedHashMap<String, Object> dumpType = (LinkedHashMap<String, Object>) this.typeRespresentation(typeModel,true);
				mp.put("schema", dumpType);
				if (typeModel.isEffectivelyEmptyType()){
					typeModel=typeModel.superType();
				}
				tps.add(typeModel);
				break;
			}
			
		}
		if (tps.size() > 1) {
			System.err.println("Warning - response has more then one body:" + r.code());
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
			mp.put("authorizationUrl", ((String) x.settings().get("authorizationUri")));
			mp.put("tokenUrl", ((String) x.settings().get("accessTokenUri")));
			mp.put("type", "oauth2");
			Object object2 = x.settings().get("authorizationGrants");
			if (object2 instanceof List){
				@SuppressWarnings("unchecked")
				List<String>sm=(List<String>) object2;
				System.err.println("flow is a single item in swagger, conversion is not perfect");
				object2=sm.get(0);
			}
			String object = (String) object2;
			// "implicit", "password", "application" or "accessCode".
			// authorization_code, password, client_credentials, or implicit;
			if (object != null) {
				if (object.equals("authorization_code")) {
					mp.put("flow", "accessCode");
				} else if (object.equals("implicit")) {
					mp.put("flow", "implicit");
					mp.remove("tokenUrl");
				}
				else if (object.equals("password")) {
					mp.put("flow", "password");
					mp.remove("authorizationUrl");
				}
				else {
					System.err.println("Can not map authorizationGrants:" + object);
				}
			}
			else{
				mp.remove("tokenUrl");
				mp.put("flow", "implicit");
			}			
			List<?> scopes = (List<?>) x.settings().get("scopes");
			if (scopes != null && !scopes.isEmpty()) {
				LinkedHashMap<String, String> scopesMap = new LinkedHashMap<>();
				for (Object c : scopes) {
					scopesMap.put(c.toString(), "");
				}
				mp.put("scopes", scopesMap);
			}
		} else if (x.type().equals("Basic Authentication")) {
			mp.put("type", "basic");
		} else if (x.type().equals("Pass Through")) {
			mp.put("type", "apiKey");
			MethodBase base = x.describedBy();
			if (base == null) {
				System.err.println("Path through securiry scheme misses described by");
			} else {
				ArrayList<INamedParam> ps = new ArrayList<>();
				ps.addAll(base.queryParameters());
				ps.addAll(base.headers());
				if (ps.size() > 1) {
					System.err.println(
							"Path through securiry scheme has more then one parameter and can not be converted to ApiKey scheme correctly");
				}
				if (ps.size() < 1) {
					System.err.println(
							"Path through securiry scheme has less then one parameter and can not be converted to ApiKey scheme correctly");
				}
				else{
				INamedParam iNamedParam = ps.get(0);
				mp.put("name", iNamedParam.getKey());
				mp.put("in", iNamedParam.location().name().toLowerCase());
				}
			}
		}
		else if (x.type().equals("OAuth 1.0")) {
			mp.put("type", "apiKey");
			MethodBase base = x.describedBy();
			if (base == null) {
				System.err.println("Path through securiry scheme misses described by");
			} else {
				ArrayList<INamedParam> ps = new ArrayList<>();
				ps.addAll(base.queryParameters());
				ps.addAll(base.headers());
				if (ps.size() > 1) {
					System.err.println(
							"Custom securiry scheme has more then one parameter and can not be converted to ApiKey scheme correctly");
				}
				if (ps.size() < 1) {
					System.err.println(
							"Custom securiry scheme has less then one parameter and can not be converted to ApiKey scheme correctly");
				}
				else{
				INamedParam iNamedParam = ps.get(0);
				mp.put("name", iNamedParam.getKey());
				mp.put("in", iNamedParam.location().name().toLowerCase());
				}
			}
			System.err.println("Swagger does not support Oath 1.0");
		}
		else {
			mp.put("type", "apiKey");
			MethodBase base = x.describedBy();
			if (base == null) {
				System.err.println("Path through securiry scheme misses described by");
			} else {
				ArrayList<INamedParam> ps = new ArrayList<>();
				ps.addAll(base.queryParameters());
				ps.addAll(base.headers());
				if (ps.size() > 1) {
					System.err.println(
							"Custom securiry scheme has more then one parameter and can not be converted to ApiKey scheme correctly");
				}
				if (ps.size() < 1) {
					System.err.println(
							"Custom securiry scheme has less then one parameter and can not be converted to ApiKey scheme correctly");
				}
				else{
				INamedParam iNamedParam = ps.get(0);
				mp.put("name", iNamedParam.getKey());
				mp.put("in", iNamedParam.location().name().toLowerCase());
				}
			}					
		}
		addScalarField("description", mp, x, x::description);
		addAnnotations(x, mp);
		return mp;
	}

	@SuppressWarnings("unchecked")
	public String store(Api model) {
		this.api = model;
		LinkedHashMap<String, Object> toStore = new LinkedHashMap<>();
		toStore.put("swagger", "2.0");
		addInfoObject(model, toStore);
		addScalarField("schemes", toStore, model,
				() -> model.getProtocols().stream().map(x -> x.toLowerCase()).toArray());
		addScalarField("consumes", toStore, model, model::getMediaType);
		addScalarField("produces", toStore, model, model::getMediaType);
		String baseUrl = model.getBaseUrl();
		if (baseUrl != null) {
			try {
				URL url = new URL(baseUrl);
				toStore.put("host", url.getHost());
				String path = url.getPath();
				if (path.length() > 0) {
					toStore.put("basePath", path);
				}
			} catch (MalformedURLException e) {
				System.err.println("Mailformed base url:" + baseUrl);
			}
		}
		addAnnotations(model, toStore);
		dumpCollection("securityDefinitions", toStore, model.securityDefinitions(), this::dumpSecurityScheme,
				x -> x.name());
		dumpSecuredBy(toStore, model.getSecuredBy());
		dumpTypes(model.types(), model.annotationTypes(), (LinkedHashMap<Object, Object>) (Map<?, ?>) toStore);
		LinkedHashMap<String, Object> paths = new LinkedHashMap<>();
		dumpResources(model.allResources(), paths);
		toStore.put("paths", paths);
		while (!extras.isEmpty()) {
			ArrayList<AbstractType> ts = new ArrayList<>(extras);
			extras.clear();

			LinkedHashMap<String, Object> object = (LinkedHashMap<String, Object>) toStore.get(TYPES);
			if (object == null) {
				object = new LinkedHashMap<>();
				toStore.put(TYPES, object);
			}
			for (AbstractType c : ts) {
				LinkedHashMap<String, Object> dumpType = dumpType(c);
				String findPath = findPath(api, c);
				object.put(findPath+"."+c.name(), dumpType);
				
			}
		}
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
			toStore.put("x-" + a.annotationType().name(), a.value());
		}
	}

	public Swagger toSwaggerObject(Api raml) {
		return new SwaggerParser().parse(store(raml));
	}
}