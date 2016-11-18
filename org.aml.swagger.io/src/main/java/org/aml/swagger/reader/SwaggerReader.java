package org.aml.swagger.reader;

import java.io.IOException;
import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aml.apimodel.impl.ActionImpl;
import org.aml.apimodel.impl.ApiImpl;
import org.aml.apimodel.impl.MimeTypeImpl;
import org.aml.apimodel.impl.NamedParamImpl;
import org.aml.apimodel.impl.ResourceImpl;
import org.aml.apimodel.impl.ResponseImpl;
import org.aml.apimodel.impl.SecuritySchemeImpl;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.TypeRegistryImpl;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.FacetRegistry;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.Default;
import org.aml.typesystem.meta.facets.Description;
import org.aml.typesystem.meta.facets.Discriminator;
import org.aml.typesystem.meta.facets.DisplayName;
import org.aml.typesystem.meta.facets.Example;
import org.aml.typesystem.meta.facets.Format;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.meta.restrictions.Enum;
import org.aml.typesystem.meta.restrictions.Pattern;
import org.aml.typesystem.meta.restrictions.minmax.MaxItems;
import org.aml.typesystem.meta.restrictions.minmax.MaxLength;
import org.aml.typesystem.meta.restrictions.minmax.Maximum;
import org.aml.typesystem.meta.restrictions.minmax.MinItems;
import org.aml.typesystem.meta.restrictions.minmax.MinLength;
import org.aml.typesystem.meta.restrictions.minmax.Minimum;
import org.aml.typesystem.ramlreader.TopLevelRamlImpl;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.aml.typesystem.yamlwriter.RamlWriter;
import org.raml.v2.api.loader.CompositeResourceLoader;
import org.raml.v2.api.loader.UrlResourceLoader;
import org.raml.yagi.framework.util.DateType;
import org.raml.yagi.framework.util.DateUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.models.AbstractModel;
import io.swagger.models.ArrayModel;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.Xml;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.RefParameter;
import io.swagger.models.properties.AbstractNumericProperty;
import io.swagger.models.properties.AbstractProperty;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.ByteArrayProperty;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.models.properties.UUIDProperty;
import io.swagger.models.refs.RefFormat;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.SwaggerResolver;

public class SwaggerReader {

	private ApiImpl result;
	private Swagger swagger;

	
	
	public ApiImpl read(String content) {
		result = new ApiImpl();
		result.getUsesLocations().put("commons",
				"https://raw.githubusercontent.com/apiregistry/commons/master/commons.raml");
		result.getUsesLocations().put("extras",
				"https://raw.githubusercontent.com/apiregistry/typesExtras/master/typeExtras.raml");
		swagger = new SwaggerParser().readWithInfo(content).getSwagger();
		swagger = new SwaggerResolver(swagger, new ArrayList<>()).resolve();
		if (swagger == null) {
			return null;
		}
		String title = swagger.getInfo().getTitle();
		title = title.replace("Swagger", "").trim();
		result.setTitle(title);
		result.setVersion(swagger.getInfo().getVersion());
		result.setDescription(swagger.getInfo().getDescription());
		Map<String, SecuritySchemeDefinition> securityDefinitions = swagger.getSecurityDefinitions();
		if (securityDefinitions != null) {
			for (String s : securityDefinitions.keySet()) {
				SecuritySchemeImpl sd = new SecuritySchemeImpl();
				sd.setName(s);
				SecuritySchemeDefinition securitySchemeDefinition = securityDefinitions.get(s);
				sd.setDescription(securitySchemeDefinition.getDescription());
				if (securitySchemeDefinition instanceof OAuth2Definition) {
					OAuth2Definition od = (OAuth2Definition) securitySchemeDefinition;
					sd.setType("OAuth 2.0");
					sd.settings().put("accessTokenUri",
							od.getTokenUrl() != null ? od.getTokenUrl() : od.getAuthorizationUrl());
					sd.settings().put("authorizationUri", od.getAuthorizationUrl());
					String flow = od.getFlow();
					if (flow.equals("accessCode")){
						flow="authorization_code";
					}
					sd.settings().put("authorizationGrants", new String[] { flow });
					ArrayList<String>scopeDescriptions=new ArrayList<>();
					if (od.getScopes()!=null){
						Set<String> keySet = od.getScopes().keySet();
						sd.settings().put("scopes", new ArrayList<>(keySet));
						boolean hasDescr=true;
						for (String k:keySet){
							String e = od.getScopes().get(k);
							if (e!=null&&e.trim().length()>0){
							scopeDescriptions.add(e);
							}
							else{
								hasDescr=false;
							}
						}
						if (hasDescr&&scopeDescriptions.size()>0){
							sd.annotations().add(new Annotation("commons.OathScopeDescriptions", scopeDescriptions));
						}
					}
				} else if (securitySchemeDefinition instanceof ApiKeyAuthDefinition) {
					sd.setType("Pass Through");
				} else if (securitySchemeDefinition instanceof BasicAuthDefinition) {
					sd.setType("Basic Authentication");
				} else {

					throw new IllegalStateException();
				}
				result.securityDefinitions().add(sd);
			}
		}
		if (swagger.getDefinitions() != null) {
			for (String s : swagger.getDefinitions().keySet()) {
				Model m = swagger.getDefinitions().get(s);
				result.addType(convertType(s, m));
			}
		}

		String host = swagger.getHost();
		String basePath = swagger.getBasePath();
		if (basePath == null) {
			basePath = "";
		}
		if (host != null) {
			result.setBaseUrl(host + basePath);
		}
		List<String> prod = swagger.getProduces();
		List<String> cons = swagger.getConsumes();
		if (prod != null) {
			// if (!prod.equals(cons)) {
			// throw new IllegalStateException();
			// }
			result.setMediaTypes(prod);
		}
		for (String p : swagger.getPaths().keySet()) {
			Path path = swagger.getPaths().get(p);
			if (p.startsWith("/")) {
				p = p.substring(1);
			}
			if (p.endsWith("/")) {
				p = p.substring(0, p.length()-1);
			}
			ResourceImpl orCreateResource = result.getOrCreateResource(p);
			addOperation(orCreateResource, "get", path.getGet(), path.getParameters());
			addOperation(orCreateResource, "head", path.getHead(), path.getParameters());
			addOperation(orCreateResource, "delete", path.getDelete(), path.getParameters());
			addOperation(orCreateResource, "options", path.getOptions(), path.getParameters());
			addOperation(orCreateResource, "patch", path.getPatch(), path.getParameters());
			addOperation(orCreateResource, "post", path.getPost(), path.getParameters());
			addOperation(orCreateResource, "put", path.getPut(), path.getParameters());

		}
		RamlWriter rs = new RamlWriter();
		String store = rs.store(result);
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(store,
				new CompositeResourceLoader(new UrlResourceLoader()), "t.raml");
		if (!build.isOk()) {
			System.out.println(store);
			System.out.println(build.validationResults());

			throw new IllegalSelectorException();
		}
		return result;
	}

	void addOperation(ResourceImpl orCreateResource, String code, Operation op, List<Parameter> list) {
		if (op == null) {
			return;
		}
		ActionImpl orCreateMethod = orCreateResource.getOrCreateMethod(code);
		orCreateMethod.setDescription(op.getDescription());
		orCreateMethod.setDisplayName(op.getSummary());
		ArrayList<Parameter> ps = new ArrayList<>();
		if (op.isDeprecated() != null && op.isDeprecated()) {
			orCreateMethod.annotations().add(new Annotation("commons.Depricated", null));
		}
		if (list != null) {
			ps.addAll(list);
		}
		if (op.getParameters() != null) {
			ps.addAll(op.getParameters());
		}
		List<String> produces = swagger.getProduces();
		List<String> mediaType = this.result.getMediaType();
		if (op.getConsumes() != null) {
			mediaType = op.getConsumes();
		}
		if (op.getProduces() != null) {
			produces = op.getProduces();
		}
		produces=cleanupMedia(produces);
		mediaType=cleanupMedia(mediaType);
		AbstractType fdt = TypeOps.derive("", BuiltIns.OBJECT);
		boolean hasFdt = false;
		for (Parameter p : ps) {
			String in = p.getIn();
			AbstractType pType = convertParameterToType(p);
			NamedParamImpl namedParamImpl = new NamedParamImpl(p.getName(), pType, p.getRequired(), false);
			if (in.equals("query")) {
				orCreateMethod.addQueryParameter(namedParamImpl);
			} else if (in.equals("path")) {
				ResourceImpl r = (ResourceImpl) orCreateMethod.resource();
				try {
					r.addUriParameterToHierarchy(namedParamImpl);
				} catch (Exception e) {
					System.err.println("Unmapped parameter:" + namedParamImpl.getKey() + orCreateMethod.toString());
				}
			} else if (in.equals("header")) {
				orCreateMethod.headers().add(namedParamImpl);
			} else if (in.equals("formData")) {
				fdt.declareProperty(p.getName(), pType, !p.getRequired());
				hasFdt = true;
			} else if (in.equals("body")) {
				for (String s : mediaType) {
					orCreateMethod.addBody(s, pType);
				}
			} else {
				throw new IllegalStateException();
			}
		}
		if (hasFdt) {
			for (String s : mediaType) {
				if (s.contains("form")) {
					orCreateMethod.addBody(s, fdt);
				}
			}
		}

		Map<String, Response> responses = op.getResponses();
		for (String c : responses.keySet()) {
			Response response = responses.get(c);
			if (c.equals("default")) {
				boolean hasOk = false;
				for (String s : responses.keySet()) {
					if (s.startsWith("2")) {
						hasOk = true;
					}
				}
				if (hasOk) {
					c = "500";
				} else {
					if (response.getDescription() != null
							&& response.getDescription().toLowerCase().contains("error")) {
						c = "500";
					} else
						c = "200";
				}
			}

			ResponseImpl impl = new ResponseImpl(c);
			if (produces == null) {
				produces = new ArrayList<>();
				produces.add("application/json");
			}

			for (String m : produces) {
				Property schema = response.getSchema();
				if (schema != null) {
					AbstractType tp = propertyToType(schema);
					impl.body().add(new MimeTypeImpl(tp, orCreateMethod, m));
				}
			}
			impl.setDescription(response.getDescription());
			if (response.getHeaders() != null) {
				for (String h : response.getHeaders().keySet()) {
					Property property = response.getHeaders().get(h);
					if (property != null) {
						impl.headers()
								.add(new NamedParamImpl(h, propertyToType(property), property.getRequired(), false));
					}
				}
			}
			orCreateMethod.responses().add(impl);
		}
		Object object = op.getVendorExtensions().get("x-ms-pageable");
		if (object!=null){
			if (object instanceof ObjectNode){
				ObjectNode nm=(ObjectNode) object;
				LinkedHashMap<String, Object>vl=new LinkedHashMap<>();
				JsonNode value = nm.get("nextLinkName");
				String asText = value.asText();
				if (asText!=null&&!asText.equals("null")){
					vl.put("nextLinkName", asText);
					object=vl;
				}
				else{
					object=null;
				}
			}
			if (object!=null){
				orCreateMethod.annotations().add(new Annotation("extras.Pagination", object));
			}
		}
	}

	private List<String> cleanupMedia(List<String> produces) {
		if (produces!=null){
			ArrayList<String>result=new ArrayList<>();
			for (String s:produces){
				if (s.indexOf(';')!=-1){
					s=s.substring(0, s.indexOf(';'));
				}
				if (s.startsWith("/")){
					s=s.substring(1);
				}
				if (s.equals("plain")){
					s="text/plain";
				}
				result.add(s);
			}
			return result;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private AbstractType convertParameterToType(Parameter p) {
		if (p instanceof AbstractSerializableParameter<?>) {
			AbstractSerializableParameter<?> basicModel = (AbstractSerializableParameter<?>) p;
			String type = basicModel.getType();
			if (type==null&&basicModel.getMinimum()!=null){
				type="number";
			}
			if (type==null&&basicModel.getMaximum()!=null){
				type="number";
			}
			
			if (type==null&&basicModel.getEnum()!=null){
				type="string";
			}
			
			if (type!=null&&type.equals("array")){
				if (basicModel.getEnum()!=null){
					type="string";	
				}
				//FIXME
			}
			
			if (type!=null&&type.equals("boolean")){
				basicModel.setEnum(null);//FIXME
			}
			String format = basicModel.getFormat();
			FormatMapper mapFormat = FormatMapper.mapFormat(new FormatMapper(type,format));
			type=mapFormat.type;
			if (((AbstractSerializableParameter) p).getEnum()!=null){
				LinkedHashSet<Object> linkedHashSet = new LinkedHashSet<>(((AbstractSerializableParameter) p).getEnum());
				linkedHashSet.remove(null);
				((AbstractSerializableParameter) p).setEnum(new ArrayList<>(linkedHashSet));
			}
			format=mapFormat.format;
			AbstractType st = base(type);
			AbstractType derive = TypeOps.derive("", st);
			transferTo(derive, Description.class, basicModel.getDescription());
			transferTo(derive, Example.class, basicModel.getExample());
			transferTo(derive, DisplayName.class, basicModel.getName());
			transferTo(derive, Default.class, basicModel.getDefaultValue());
			transferTo(derive, Enum.class, basicModel.getEnum());

			transferTo(derive, Format.class, format);
			transferTo(derive, MaxLength.class, basicModel.getMaxLength());
			transferTo(derive, MinLength.class, basicModel.getMinLength());
			transferTo(derive, Pattern.class, basicModel.getPattern());
			transferTo(derive, Enum.class, basicModel.getEnum());
			transferTo(derive, Minimum.class, basicModel.getMinimum());
			transferTo(derive, Maximum.class, basicModel.getMaximum());
			Property items = basicModel.getItems();
			if (items != null) {
				derive.addMeta(new ComponentShouldBeOfType(propertyToType(items)));
			}
			return derive;
		} else if (p instanceof RefParameter) {
			throw new IllegalStateException();
		} else if (p instanceof BodyParameter) {
			BodyParameter bp = (BodyParameter) p;
			Model schema = bp.getSchema();
			AbstractType convertType = convertType("", schema);
			transferTo(convertType, Description.class, bp.getDescription());
			transferTo(convertType, Example.class, bp.getExamples());
			return convertType;
		} else {
			throw new IllegalStateException();
		}
	}

	TypeRegistryImpl reg = new TypeRegistryImpl(null);

	int id = 0;

	public AbstractType convertType(String name, Model m) {
		name = name.replace('[', '_');
		name = name.replace(']', '_');
		name = name.replace(',', '_');
		name = name.replace('(', '_');
		name = name.replace(')', '_');
		name = name.replace('.', '_');
		if (BuiltIns.getBuiltInTypes().getType(name)!=null){
			name=Character.toUpperCase(name.charAt(0))+name.substring(1);
		}
		if (reg.getType(name) != null) {
			return reg.getType(name);
		}
		if (m instanceof AbstractModel) {
			if (m instanceof ArrayModel) {
				ArrayModel basicModel = (ArrayModel) m;
				String type = basicModel.getType();
				
				AbstractType st = base(type);
				AbstractType derive = TypeOps.derive(name, st);
				transferTo(derive, Description.class, basicModel.getDescription());
				transferTo(derive, Example.class, basicModel.getExample());
				transferTo(derive, DisplayName.class, basicModel.getTitle());
				Property additionalProperties = basicModel.getItems();
				if (additionalProperties != null) {
					AbstractType propertyToType = propertyToType(additionalProperties);
					derive.addMeta(new ComponentShouldBeOfType(propertyToType));
				}

				return derive;
			} else if (m instanceof ComposedModel) {
				ComposedModel cm = (ComposedModel) m;
				List<Model> allOf = cm.getAllOf();
				ArrayList<AbstractType> superTypes = new ArrayList<>();
				for (Model ma : allOf) {
					AbstractType convertType = convertType("", ma);
					while (convertType.isAnonimous()) {
						if (ma instanceof ModelImpl) {
							convertType = convertType.clone("Anonimous" + (id++));
							result.addType(convertType);
						} else {
							convertType = convertType.superType();
						}
					}
					superTypes.add(convertType);
				}
				AbstractType derive = TypeOps.derive(name, superTypes.toArray(new AbstractType[superTypes.size()]));
				transferTo(derive, Description.class, cm.getDescription());
				transferTo(derive, Example.class, cm.getExample());
				transferTo(derive, DisplayName.class, cm.getTitle());

				return derive;
			} else {
				ModelImpl basicModel = (ModelImpl) m;
				String type = basicModel.getType();
				if (type==null){
					if (basicModel.getEnum()!=null){
						type="string";
					}
				}
				String format = basicModel.getFormat();
				FormatMapper mapFormat = FormatMapper.mapFormat(new FormatMapper(type, format));
				type=mapFormat.type;
				format=mapFormat.format;
				AbstractType st = base(type);
				AbstractType derive = TypeOps.derive(name, st);
				transferTo(derive, Description.class, basicModel.getDescription());
				transferTo(derive, Example.class, basicModel.getExample());
				transferTo(derive, DisplayName.class, basicModel.getTitle());
				transferTo(derive, Default.class, basicModel.getDefaultValue());
				transferTo(derive, Discriminator.class, basicModel.getDiscriminator());
				if (!derive.isArray()&&!derive.isObject()){
					transferTo(derive, Enum.class, basicModel.getEnum());
				}
				transferTo(derive, Format.class, format);
				reg.registerType(derive);
				Map<String, Property> properties = m.getProperties();
				if (properties != null) {
					for (String s : properties.keySet()) {
						Property p = properties.get(s);
						AbstractType propertyToType = propertyToType(p);
						derive.declareProperty(s, propertyToType, !p.getRequired());
					}
				}
				Property add = basicModel.getAdditionalProperties();
				if (add != null) {
					derive.declareAdditionalProperty(propertyToType(add));
				}
				addXMLInfo(derive, basicModel.getXml());
				return derive;
			}
		}
		if (m instanceof RefModel) {
			RefModel rm = (RefModel) m;
			RefFormat refFormat = rm.getRefFormat();
			if (refFormat != RefFormat.INTERNAL) {
				throw new IllegalArgumentException("Only internal references are supported at this moment");
			}
			String simpleRef = rm.getSimpleRef();
			String get$ref = rm.get$ref();
			if (!get$ref.startsWith("#/definitions/")) {
				throw new IllegalArgumentException("Only internal references are supported at this moment");
			}

			Model actual = swagger.getDefinitions().get(simpleRef);
			AbstractType convertType = convertType(simpleRef, actual);

			return convertType;
		}
		
		return null;
	}

	public AbstractType propertyToType(Property p) {
		String type;
		AbstractType range = null;
		
		if (p instanceof RefProperty) {
			RefProperty rm = (RefProperty) p;
			RefFormat refFormat = rm.getRefFormat();
			if (refFormat != RefFormat.INTERNAL) {
				throw new IllegalArgumentException("Only internal references are supported at this moment");
			}
			String simpleRef = rm.getSimpleRef();
			String get$ref = rm.get$ref();
			if (!get$ref.startsWith("#/definitions/")) {
				throw new IllegalArgumentException("Only internal references are supported at this moment");
			}
			Model actual = swagger.getDefinitions().get(simpleRef);
			AbstractType convertType=null;
			if (actual==null){
				System.err.println("Warning can not find definition for :"+simpleRef);
				convertType=BuiltIns.OBJECT;
			}
			else{
				convertType = convertType(simpleRef, actual);
			}
			range = TypeOps.derive("", convertType);
			transferTo(range, Description.class, rm.getDescription());
			transferTo(range, Example.class, rm.getExample());
			transferTo(range, DisplayName.class, rm.getTitle());
		} else {
			AbstractProperty ps = (AbstractProperty) p;
			type = p.getType();
			String format = p.getFormat();
			if (format != null) {
				FormatMapper mapFormat = FormatMapper.mapFormat(new FormatMapper(type, format));
				format=mapFormat.format;
				type=mapFormat.type;
			}
			AbstractType bs = base(type);
			range = TypeOps.derive("", bs);
			transferTo(range, Description.class, p.getDescription());
			transferTo(range, Example.class, p.getExample());
			transferTo(range, DisplayName.class, p.getTitle());

			transferTo(range, Format.class, format);
			if (ps instanceof AbstractNumericProperty) {
				AbstractNumericProperty np = (AbstractNumericProperty) ps;
				transferTo(range, Maximum.class, np.getMaximum());
				transferTo(range, Minimum.class, np.getMinimum());

			} else if (ps instanceof UUIDProperty) {

			} else if (ps instanceof DateTimeProperty) {
				ps.setFormat(null);
			} else if (ps instanceof BooleanProperty) {

			} else if (ps instanceof FileProperty) {

			}
			else if (ps instanceof DateProperty) {

			}else if (ps instanceof ByteArrayProperty) {

			} else if (ps instanceof StringProperty) {
				StringProperty sp = (StringProperty) ps;
				transferTo(range, MaxLength.class, sp.getMaxLength());
				transferTo(range, MinLength.class, sp.getMinLength());
				transferTo(range, Pattern.class, sp.getPattern());
				transferTo(range, Enum.class, sp.getEnum());
			} else if (ps instanceof io.swagger.models.properties.MapProperty) {
				io.swagger.models.properties.MapProperty mp = (io.swagger.models.properties.MapProperty) ps;
				Property additionalProperties = mp.getAdditionalProperties();
				if (additionalProperties != null) {
					AbstractType propertyToType = propertyToType(additionalProperties);
					range.declareAdditionalProperty(propertyToType);
				}
			} else if (ps instanceof ArrayProperty) {
				io.swagger.models.properties.ArrayProperty mp = (io.swagger.models.properties.ArrayProperty) ps;
				Property additionalProperties = mp.getItems();
				if (additionalProperties != null) {
					AbstractType propertyToType = propertyToType(additionalProperties);
					range.addMeta(new ComponentShouldBeOfType(propertyToType));
				}
				transferTo(range, MinItems.class, mp.getMinItems());
				transferTo(range, MaxItems.class, mp.getMaxItems());
			} else if (ps instanceof ObjectProperty) {
				ObjectProperty op = (ObjectProperty) ps;
				Map<String, Property> properties = op.getProperties();
				for (String s : properties.keySet()) {
					Property pp = properties.get(s);
					AbstractType propertyToType = propertyToType(pp);
					range.declareProperty(s, propertyToType, !p.getRequired());
				}

				// Property add = op.getAdditionalProperties();
				// if (add != null) {
				// derive.declareAdditionalProperty(propertyToType(add));
				// }
				// throw new IllegalStateException();
			} else {
				throw new IllegalStateException();
			}
		}
		if (p.getXml() != null) {
			addXMLInfo(range, p.getXml());
		}
		if(range.isSubTypeOf(BuiltIns.DATETIME)){
			Example oneMeta = range.oneMeta(Example.class);
			if (oneMeta!=null){
				String val=""+oneMeta.value();
				if (!DateUtils.isValidDate(val, DateType.datetime, null)){
					range.removeMeta(oneMeta);
				}
			}
		}
		if (p.getReadOnly()!=null&&p.getReadOnly()){
			range.addMeta(new Annotation("extras.Readonly",true ));			
		}
		return range;
	}

	private void addXMLInfo(AbstractType range, Xml xml) {
		// TODO Auto-generated method stub

	}

	void transferTo(AbstractType tp, Class<? extends TypeInformation> cl, Object value) {
		if (value == null || value.toString().length() == 0) {
			return;
		}
		if (value instanceof Double) {
			if (((double) ((Double) value).intValue()) == ((Double) value).doubleValue()) {
				value = ((Double) value).intValue();
			}
		}
		if (value instanceof ObjectNode){
			ObjectNode n=(ObjectNode) value;
			try {
				value=new ObjectMapper().reader(Object.class).readValue(n);
			} catch (IOException e) {
				throw new IllegalStateException();
			}
		}
		if (value instanceof Number){
			long v=((Number) value).longValue();
			if (v>10000000){
				return ;
			}
			if (v<-10000000){
				return ;
			}
		}
		TypeInformation facet = FacetRegistry.facet(FacetRegistry.getFacetName((Class<? extends TypeInformation>) cl));
		ISimpleFacet fs = (ISimpleFacet) facet;
		fs.setValue(value);
		tp.addMeta((TypeInformation) fs);
	}

	static HashMap<String, AbstractType> maps = new HashMap<>();

	static {
		maps.put("string", BuiltIns.STRING);
		maps.put(null, BuiltIns.OBJECT);
		maps.put("null", BuiltIns.OBJECT);
		maps.put("date-time", BuiltIns.DATETIME);
		maps.put("datetime", BuiltIns.DATETIME);
		maps.put("number", BuiltIns.NUMBER);
		maps.put("integer", BuiltIns.INTEGER);
		maps.put("boolean", BuiltIns.BOOLEAN);
		maps.put("file", BuiltIns.FILE);
		maps.put("object", BuiltIns.OBJECT);
		maps.put("array", BuiltIns.ARRAY);
	}

	AbstractType base(String type) {
		
		if (maps.containsKey(type)) {
			return maps.get(type);
		}
		if (type.startsWith("commons.")){
			AbstractType derive = TypeOps.derive(type,BuiltIns.STRING);
			maps.put(type, derive);
			return derive;
		}
		throw new IllegalStateException();
	}
}