package org.aml.swagger.writer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import org.aml.apimodel.Action;
import org.aml.apimodel.Api;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.ParameterLocation;
import org.aml.apimodel.Resource;
import org.aml.apimodel.SecuredByConfig;
import org.aml.apimodel.SecurityScheme;
import org.aml.apimodel.impl.MimeTypeImpl;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ramlreader.NamedParam;
import org.aml.typesystem.yamlwriter.GenericWriter;

import io.swagger.models.Info;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

public class Raml2SwaggerModel extends GenericWriter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Swagger convert(Api api) {
		Swagger result = new Swagger();
		Info info = new Info();
		info.setTitle(api.title());
		info.setDescription(api.description());
		info.setVersion(api.version());
		result.setInfo(info);
		result.setSchemes(
				(List) Arrays.asList(api.getProtocols().stream().map(x -> Scheme.forValue(x.toLowerCase())).toArray()));
		result.setConsumes(api.getMediaType());
		result.setProduces(api.getMediaType());
		api.annotations().forEach(x -> {
			result.setVendorExtension("x-" + x.getName(), cleanObject(x.getValue()));
		});
		String baseUrl = api.getBaseUrl();
		if (baseUrl != null) {
			try {
				URL url = new URL(baseUrl);
				result.setHost(url.getHost());
				result.setBasePath(url.getPath());
			} catch (MalformedURLException e) {
				System.err.println("Mailformed base url:" + baseUrl);
			}
		}
		api.securityDefinitions().forEach(x -> {
			SecuritySchemeDefinition convertSecurityScheme = convertSecurityScheme(x);
			if (convertSecurityScheme == null) {
				System.err.println("Can not convert security definition:" + x.name());
			}
			result.addSecurityDefinition(x.name(), convertSecurityScheme);
		});
		api.getSecuredBy().forEach(x->{
			SecurityRequirement securityRequirement = convertSecuredBy(x);
			result.addSecurity(securityRequirement);
		});
		LinkedHashMap<String, Path> paths = new LinkedHashMap<String,Path>();
		api.allResources().forEach(r->{			
			paths.put(r.getUri(), convertToPath(r));			
		});
		result.setPaths(paths);
		return result;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public SecurityRequirement convertSecuredBy(SecuredByConfig x) {
		SecurityRequirement securityRequirement = new SecurityRequirement();
		securityRequirement=securityRequirement.requirement(x.name());
		Object object = x.settings().get("scopes");
		if (object!=null){
			securityRequirement.setScopes((List<String>) object);
		}
		return securityRequirement;
	}

	private Path convertToPath(Resource r) {
		Path p=new Path();
		r.annotations().forEach(x -> {
			p.setVendorExtension("x-" + x.getName(), cleanObject(x.getValue()));
		});		
		r.methods().forEach(m->{
			String method=m.method();
			Operation op=convertMethod(m);
			p.set(method, op);			
		});
		return p;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private Operation convertMethod(Action m) {
		Operation result=new Operation();
		result.setSummary(m.displayName());
		result.setDescription(m.description());
		m.annotations().forEach(x -> {
			result.setVendorExtension("x-" + x.getName(), cleanObject(x.getValue()));
		});	
		if (!m.protocols().isEmpty()){
			result.setSchemes((List) Arrays.asList(m.protocols().stream().map(x -> Scheme.forValue(x.toLowerCase())).toArray()));
		}
		m.securedBy().forEach(x->{
			SecurityRequirement securityRequirement = convertSecuredBy(x);
			result.addSecurity(securityRequirement.getName(), securityRequirement.getScopes());
		});
		ArrayList<INamedParam> value = new ArrayList<>(m.parameters());
		List<MimeType> body = m.body();
		LinkedHashSet<String> consumes = new LinkedHashSet<>();
		LinkedHashSet<String> produces = new LinkedHashSet<>();
		boolean added = false;
		for (MimeType b : body) {
			consumes.add(b.getType());
			List<INamedParam> formParameters =b.getFormParameters();
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
			System.err.println("Warning, multiple bodies are not supported in swagger:" + m.resource().getUri() + "."
					+ m.method());
		}
		dumpParameters(value, result);
		m.responses().forEach(r -> r.body().forEach(t -> produces.add(t.getType())));
		result.setConsumes(new ArrayList<>(consumes));
		result.setProduces(new ArrayList<>(produces));
		m.responses().forEach(r->{
			result.addResponse(r.code(), convertResponse(r));
		});
		if (result.getResponses().isEmpty()){
			Response response = new Response();
			response.setDescription("default response");
			result.addResponse("default", response);
		}		
		return result;
	}

	private Response convertResponse(org.aml.apimodel.Response r) {
		Response rs=new Response();
		rs.setDescription(r.description());
		r.annotations().forEach(x -> {
			rs.setVendorExtension("x-" + x.getName(), cleanObject(x.getValue()));
		});	
		r.headers().forEach(h->rs.addHeader(h.getKey(), convertParam(h)));
		for (MimeType m:r.body()){
			rs.schema(convertType(m.getTypeModel()));
			break;
		}
		if (r.body().size()>1){
			System.err.println("Warning, multiple response bodies are not supported in swagger");
		}		
		return rs;
	}

	
	@SuppressWarnings("unused")
	private Property convertType(AbstractType typeModel) {
		Property ps=createBaseProperty(typeModel);
		return null;
	}
	
	private Property createBaseProperty(AbstractType typeModel) {
		if(typeModel.isString()){
			StringProperty result=new StringProperty();
			return result;
		}
		else if (typeModel.isNumber()){
			if (typeModel.isSubTypeOf(BuiltIns.INTEGER)){
				IntegerProperty i=new IntegerProperty();
				return i;
			}
			DoubleProperty ds=new DoubleProperty();
			return ds;
		}
		else if (typeModel.isBoolean()){
			BooleanProperty bs=new BooleanProperty();
			return bs;
		}
		else if (typeModel.isArray()){
			ArrayProperty ar=new ArrayProperty();
			return ar;
		}
		else if (typeModel.isSubTypeOf(BuiltIns.DATETIME)){
			StringProperty result=new StringProperty();
			return result;
		}
		else if (typeModel.isSubTypeOf(BuiltIns.DATE)){
			StringProperty result=new StringProperty();
			return result;
		}
		else if (typeModel.isSubTypeOf(BuiltIns.TIMEONLY)){
			StringProperty result=new StringProperty();
			return result;
		}
		else if (typeModel.isSubTypeOf(BuiltIns.DATETIMEONLY)){
			StringProperty result=new StringProperty();
			return result;
		}
		else if (typeModel.isSubTypeOf(BuiltIns.FILE)){
			FileProperty result=new FileProperty();
			return result;
		}
		else if (typeModel.isArray()){
			ArrayProperty ar=new ArrayProperty();
			return ar;
		}
		else if (typeModel.isObject()){
			ObjectProperty ar=new ObjectProperty();
			
			return ar;
		}
		
		return null;
	}

	private Property convertParam(INamedParam h) {		
		Property convertType = convertType(h.getTypeModel());
		convertType.setRequired(h.isRequired());
		convertType.setName(h.getKey());		
		return convertType;
	}
	private Parameter convertToParam(INamedParam p) {
		
		return null;
	}
	
	

	private void dumpParameters(ArrayList<INamedParam> value, Operation result) {
		for(INamedParam p:value){
			Parameter convertToParam = convertToParam(p);
			if (convertToParam==null){
				System.err.println("Can not convert parameter");
				continue;
			}
			result.addParameter(convertToParam);
		}
	}

	

	private SecuritySchemeDefinition convertSecurityScheme(SecurityScheme x) {
		if (x.type().equals("OAuth 2.0")) {
			OAuth2Definition r = new OAuth2Definition();
			r.setAuthorizationUrl((String) x.settings().get("authorizationUri"));
			r.setTokenUrl((String) x.settings().get("accessTokenUri"));
			String object = (String) x.settings().get("authorizationGrants");
			// "implicit", "password", "application" or "accessCode".
			// authorization_code, password, client_credentials, or implicit;
			if (object != null) {
				if (object.equals("authorization_code")) {
					r.setFlow("accessCode");
				} else if (object.equals("implicit")) {
					r.setFlow("implicit");
				} else {
					System.err.println("Can not map authorizationGrants:"+object);
				}
			}
			List<?>scopes=(List<?>) x.settings().get("scopes");
			if (scopes!=null){
				LinkedHashMap<String, String>scopesMap=new LinkedHashMap<>();
				for (Object c:scopes){
					scopesMap.put(c.toString(), "");
				}
				r.setScopes(scopesMap);
			}
			return r;
		}
		if (x.type().equals("Basic Authentication")){
			return new BasicAuthDefinition();
		}
		if (x.type().equals("Pass Through")){
			ApiKeyAuthDefinition result=new ApiKeyAuthDefinition();
			result.setName("");
			result.setType("");
			//TODO FILL IT
			return result;
		}
		return null;
	}

	@Override
	protected Object typeRespresentation(AbstractType typeModel, boolean allowNamed) {
		return null;
	}

	@Override
	protected Object dumpType(AbstractType typeModel) {
		return null;
	}
}
