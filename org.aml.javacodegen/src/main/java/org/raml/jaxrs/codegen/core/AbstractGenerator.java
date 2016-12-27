/*
 * Copyright 2013-2015 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.jaxrs.codegen.core;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.strip;
import static org.raml.jaxrs.codegen.core.Names.EXAMPLE_PREFIX;
import static org.raml.jaxrs.codegen.core.Names.GENERIC_PAYLOAD_ARGUMENT_NAME;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.MimeMultipart;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.aml.apimodel.Action;
import org.aml.apimodel.Api;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.Resource;
import org.aml.apimodel.Response;
import org.apache.commons.lang.StringUtils;
import org.raml.jaxrs.codegen.core.ext.GeneratorExtension;
import org.raml.jaxrs.codegen.core.ext.InterfaceNameBuilderExtension;

import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

/**
 * <p>Abstract AbstractGenerator class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class AbstractGenerator extends BasicGenerator {
	protected Context context;
	protected Types types;
	protected List<GeneratorExtension> extensions;

	

	/**
	 * <p>run.</p>
	 *
	 * @param raml a {@link org.aml.apimodel.Api} object.
	 * @param configuration a {@link org.raml.jaxrs.codegen.core.Configuration} object.
	 * @return a {@link java.util.Set} object.
	 * @throws java.lang.Exception if any.
	 */
	protected Set<String> run(final Api raml, final Configuration configuration)
			throws Exception {
		validate(configuration);
		extensions = configuration.getExtensions();
		context = new Context(configuration, raml);
		types = new Types(context);

		for (GeneratorExtension e : extensions) {
			e.setRaml(raml);
			e.setCodeModel(context.getCodeModel());
		}
		List<Resource> resources = raml.resources();

		for (final Resource resource : resources) {
			createResourceInterface(resource, raml,configuration);
		}
		return context.generate();
	}

	/**
	 * <p>createResourceInterface.</p>
	 *
	 * @param resource a {@link org.aml.apimodel.Resource} object.
	 * @param raml a {@link org.aml.apimodel.Api} object.
	 * @throws java.lang.Exception if any.
	 * @param config a {@link org.raml.jaxrs.codegen.core.Configuration} object.
	 */
	protected void createResourceInterface(final Resource resource, final Api raml,Configuration config) throws Exception {
		
		String resourceInterfaceName = null;
    	for (GeneratorExtension e : extensions) {
    		if(e instanceof InterfaceNameBuilderExtension){
    			InterfaceNameBuilderExtension inbe = (InterfaceNameBuilderExtension) e;
    			resourceInterfaceName = inbe.buildResourceInterfaceName(resource);
    			if(resourceInterfaceName!=null){
    				break;
    			}
    		}
        }
    	if(resourceInterfaceName==null){
    		resourceInterfaceName = Names.buildResourceInterfaceName(resource,config);
    	}		

		final JDefinedClass resourceInterface = context
				.createResourceInterface(resourceInterfaceName);
		context.setCurrentResourceInterface(resourceInterface);

		final String path = strip(resource.relativeUri(), "/");
		resourceInterface.annotate(Path.class).param(
				DEFAULT_ANNOTATION_PARAMETER,
				StringUtils.defaultIfBlank(path, "/"));

		if (isNotBlank(resource.description())) {
			resourceInterface.javadoc().add(resource.description());
		}

		addResourceMethods(resource, resourceInterface, path);

		/* call registered extensions */
		for (GeneratorExtension e : extensions) {
			e.onCreateResourceInterface(resourceInterface, resource);
		}
	}

	/**
	 * <p>addResourceMethods.</p>
	 *
	 * @param resource a {@link org.aml.apimodel.Resource} object.
	 * @param resourceInterface a {@link com.sun.codemodel.JDefinedClass} object.
	 * @param resourceInterfacePath a {@link java.lang.String} object.
	 * @param resourceInterface a {@link com.sun.codemodel.JDefinedClass} object.
	 * @param resourceInterfacePath a {@link java.lang.String} object.
	 * @param resourceInterfacePath a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 */
	protected void addResourceMethods(final Resource resource,
			final JDefinedClass resourceInterface,
			final String resourceInterfacePath) throws Exception {
		for (final Action action : resource.methods()) {
			if (!action.hasBody()) {
				addResourceMethods(resourceInterface, resource, resourceInterfacePath,
						action, null, false);
			} else if (action.body().size() == 1) {
				final MimeType bodyMimeType = action.body()
						.iterator().next();
				addResourceMethods(resourceInterface, resource, resourceInterfacePath,
						action, bodyMimeType, false);
			} else {
				for (final MimeType bodyMimeType : action.body()) {
					addResourceMethods(resourceInterface, resource,
							resourceInterfacePath, action, bodyMimeType, true);
				}
			}
		}

		for (final Resource childResource : resource.resources()) {
			addResourceMethods(childResource, resourceInterface,
					resourceInterfacePath);
		}
	}

	/**
	 * <p>getUniqueResponseMimeTypes.</p>
	 *
	 * @param action a {@link org.aml.apimodel.Action} object.
	 * @return a {@link java.util.Collection} object.
	 */
	protected Collection<MimeType> getUniqueResponseMimeTypes(
			final Action action) {
		final Map<String, MimeType> responseMimeTypes = new HashMap<String, MimeType>();
		for (final Response response : action.responses()) {
			if (response.hasBody()) {
				for (final MimeType responseMimeType : response.body()) {
					if (responseMimeType != null) {
						responseMimeTypes.put(responseMimeType.getType(),
								responseMimeType);
					}
				}
			}
		}
		return responseMimeTypes.values();
	}

	/**
	 * <p>addResourceMethod.</p>
	 *
	 * @param resourceInterface a {@link com.sun.codemodel.JDefinedClass} object.
	 * @param resourceInterfacePath a {@link java.lang.String} object.
	 * @param resourceInterfacePath a {@link java.lang.String} object.
	 * @param action a {@link org.aml.apimodel.Action} object.
	 * @param bodyMimeType a {@link org.aml.apimodel.MimeType} object.
	 * @param addBodyMimeTypeInMethodName a boolean.
	 * @param uniqueResponseMimeTypes a {@link java.util.Collection} object.
	 * @throws java.lang.Exception if any.
	 * @param resource a {@link org.aml.apimodel.Resource} object.
	 */
	protected abstract void addResourceMethod(
			final JDefinedClass resourceInterface,
			final Resource resource,
			final String resourceInterfacePath, final Action action,
			final MimeType bodyMimeType,
			final boolean addBodyMimeTypeInMethodName,
			final Collection<MimeType> uniqueResponseMimeTypes)
			throws Exception;

	/**
	 * <p>addParamAnnotation.</p>
	 *
	 * @param resourceInterfacePath a {@link java.lang.String} object.
	 * @param action a {@link org.aml.apimodel.Action} object.
	 * @param method a {@link com.sun.codemodel.JMethod} object.
	 */
	protected void addParamAnnotation(final String resourceInterfacePath,
			final Action action, final JMethod method) {
		final String path = StringUtils.substringAfter(action.resource()
				.getUri(), resourceInterfacePath + "/");
		if (isNotBlank(path)) {
			method.annotate(Path.class).param(DEFAULT_ANNOTATION_PARAMETER,
					path);
		}
	}

	private void addCatchAllFormParametersArgument(final MimeType bodyMimeType,
			final JMethod method, final JDocComment javadoc,
			final JType argumentType) {
		method.param(argumentType, GENERIC_PAYLOAD_ARGUMENT_NAME);

		// build a javadoc text out of all the params
		List<INamedParam> formParameters = bodyMimeType.getFormParameters();
		if(formParameters!=null){
			for (INamedParam formParameter : formParameters) {
				final StringBuilder sb = new StringBuilder();
				sb.append(formParameter.getKey()).append(": ");
				appendParameterJavadocDescription(formParameter, sb);
				javadoc.addParam(GENERIC_PAYLOAD_ARGUMENT_NAME).add(sb.toString());
			}
		}
	}

	/**
	 * <p>addParameterJavaDoc.</p>
	 *
	 * @param parameter a {@link INamedParam} object.
	 * @param parameterName a {@link java.lang.String} object.
	 * @param parameterName a {@link java.lang.String} object.
	 * @param javadoc a {@link com.sun.codemodel.JDocComment} object.
	 */
	protected void addParameterJavaDoc(final INamedParam parameter,
			final String parameterName, final JDocComment javadoc) {
		javadoc.addParam(parameterName).add(
				defaultString(parameter.description())
						+ getPrefixedExampleOrBlank(parameter.getExample()));
	}

	/**
	 * <p>getPrefixedExampleOrBlank.</p>
	 *
	 * @param example a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getPrefixedExampleOrBlank(final String example) {
		return isNotBlank(example) ? EXAMPLE_PREFIX + example : "";
	}

	/**
	 * <p>appendParameterJavadocDescription.</p>
	 *
	 * @param param a {@link INamedParam} object.
	 * @param sb a {@link java.lang.StringBuilder} object.
	 */
	protected void appendParameterJavadocDescription(final INamedParam param,
			final StringBuilder sb) {
		if (isNotBlank(param.getDisplayName())) {
			sb.append(param.getDisplayName());
		}

		if (isNotBlank(param.description())) {
			if (sb.length() > 0) {
				sb.append(" - ");
			}
			sb.append(param.description());
		}

		if (isNotBlank(param.getExample())) {
			sb.append(EXAMPLE_PREFIX).append(param.getExample());
		}

		sb.append("<br/>\n");
	}

	private void addPlainBodyArgument(final MimeType bodyMimeType,
			final JMethod method, final JDocComment javadoc) throws IOException {

	    if(context.getConfiguration().isUseJsr303Annotations()) {
            method.param(types.getRequestEntityClass(bodyMimeType),
                    GENERIC_PAYLOAD_ARGUMENT_NAME).annotate(Valid.class);
        } else {
            method.param(types.getRequestEntityClass(bodyMimeType),
                    GENERIC_PAYLOAD_ARGUMENT_NAME);
        }

		javadoc.addParam(GENERIC_PAYLOAD_ARGUMENT_NAME).add(
				getPrefixedExampleOrBlank(bodyMimeType.getExample()));
	}

	private boolean hasAMultiTypeFormParameter(final MimeType bodyMimeType) {
		for (final INamedParam formParameters : bodyMimeType.getFormParameters()) {
			if (formParameters.getTypeModel().isUnion()){
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>addFormParameters.</p>
	 *
	 * @param bodyMimeType a {@link org.aml.apimodel.MimeType} object.
	 * @param method a {@link com.sun.codemodel.JMethod} object.
	 * @param javadoc a {@link com.sun.codemodel.JDocComment} object.
	 * @throws java.lang.Exception if any.
	 */
	protected void addFormParameters(final MimeType bodyMimeType,
			final JMethod method, final JDocComment javadoc) throws Exception {
		if (hasAMultiTypeFormParameter(bodyMimeType)) {
			// use a "catch all" MultivaluedMap<String, String> parameter
			final JClass type = types.getGeneratorClass(MultivaluedMap.class)
					.narrow(String.class, String.class);

			addCatchAllFormParametersArgument(bodyMimeType, method, javadoc,
					type);
		} else {
			for (final INamedParam namedFormParameters : bodyMimeType.getFormParameters()) {
				addParameter(namedFormParameters.getKey(), namedFormParameters, FormParam.class, method, javadoc);
			}
		}
	}

	/**
	 * <p>addConsumesAnnotation.</p>
	 *
	 * @param bodyMimeType a {@link org.aml.apimodel.MimeType} object.
	 * @param method a {@link com.sun.codemodel.JMethod} object.
	 */
	protected void addConsumesAnnotation(final MimeType bodyMimeType,
			final JMethod method) {
		if (bodyMimeType != null) {
			method.annotate(Consumes.class).param(DEFAULT_ANNOTATION_PARAMETER,
					bodyMimeType.getType());
		}
	}

	/**
	 * <p>addProducesAnnotation.</p>
	 *
	 * @param uniqueResponseMimeTypes a {@link java.util.Collection} object.
	 * @param method a {@link com.sun.codemodel.JMethod} object.
	 */
	protected void addProducesAnnotation(
			final Collection<MimeType> uniqueResponseMimeTypes,
			final JMethod method) {
		if (uniqueResponseMimeTypes.isEmpty()) {
			return;
		}

		final JAnnotationArrayMember paramArray = method.annotate(
				Produces.class).paramArray(DEFAULT_ANNOTATION_PARAMETER);

		for (final MimeType responseMimeType : uniqueResponseMimeTypes) {
			paramArray.param(responseMimeType.getType());
		}
	}

	/**
	 * <p>addBodyParameters.</p>
	 *
	 * @param bodyMimeType a {@link org.aml.apimodel.MimeType} object.
	 * @param method a {@link com.sun.codemodel.JMethod} object.
	 * @param javadoc a {@link com.sun.codemodel.JDocComment} object.
	 * @throws java.lang.Exception if any.
	 */
	protected void addBodyParameters(final MimeType bodyMimeType,
			final JMethod method, final JDocComment javadoc) throws Exception {
		if (bodyMimeType == null) {
			return;
		} else if (MediaType.APPLICATION_FORM_URLENCODED.equals(bodyMimeType
				.getType())) {
			addFormParameters(bodyMimeType, method, javadoc);
		} else if (MediaType.MULTIPART_FORM_DATA.equals(bodyMimeType.getType())) {
			// use a "catch all" javax.mail.internet.MimeMultipart parameter
			addCatchAllFormParametersArgument(bodyMimeType, method, javadoc,
					types.getGeneratorType(MimeMultipart.class));
		} else {
			addPlainBodyArgument(bodyMimeType, method, javadoc);
		}
	}

	/**
	 * <p>addPathParameters.</p>
	 *
	 * @param action a {@link org.aml.apimodel.Action} object.
	 * @param method a {@link com.sun.codemodel.JMethod} object.
	 * @param javadoc a {@link com.sun.codemodel.JDocComment} object.
	 * @throws java.lang.Exception if any.
	 */
	protected void addPathParameters(final Action action, final JMethod method,
			final JDocComment javadoc) throws Exception {
		addAllResourcePathParameters(action.resource(), method, javadoc);
	}

	private void addAllResourcePathParameters(Resource resource,
			final JMethod method, final JDocComment javadoc) throws Exception {
		for (final INamedParam namedUriParameter : resource.uriParameters()) {
			addParameter(namedUriParameter.getKey(),
					namedUriParameter, PathParam.class, method,
					javadoc);
		}
		Resource parentResource = resource.parentResource();

		if (parentResource != null) {
			addAllResourcePathParameters(parentResource, method, javadoc);
		}

	}

	/**
	 * <p>addHeaderParameters.</p>
	 *
	 * @param action a {@link org.aml.apimodel.Action} object.
	 * @param method a {@link com.sun.codemodel.JMethod} object.
	 * @param javadoc a {@link com.sun.codemodel.JDocComment} object.
	 * @throws java.lang.Exception if any.
	 */
	protected void addHeaderParameters(final Action action, final JMethod method,
			final JDocComment javadoc) throws Exception {
		for (final INamedParam namedHeaderParameter : action.headers()) {
			addParameter(namedHeaderParameter.getKey(),
					namedHeaderParameter, HeaderParam.class, method,
					javadoc);
		}
	}
	/**
	 * <p>addBaseJavaDoc.</p>
	 *
	 * @param action a {@link org.aml.apimodel.Action} object.
	 * @param method a {@link com.sun.codemodel.JMethod} object.
	 * @return a {@link com.sun.codemodel.JDocComment} object.
	 */
	protected JDocComment addBaseJavaDoc(final Action action, final JMethod method)
    {
        final JDocComment javadoc = method.javadoc();
        if (isNotBlank(action.description()))
        {
            javadoc.add(action.description());
        }
        return javadoc;
    }

	/**
	 * <p>addQueryParameters.</p>
	 *
	 * @param action a {@link org.aml.apimodel.Action} object.
	 * @param method a {@link com.sun.codemodel.JMethod} object.
	 * @param javadoc a {@link com.sun.codemodel.JDocComment} object.
	 * @throws java.lang.Exception if any.
	 */
	protected void addQueryParameters(final Action action, final JMethod method,
			final JDocComment javadoc) throws Exception {
		for (final INamedParam namedQueryParameter : action.queryParameters()) {
			addParameter(namedQueryParameter.getKey(),
					namedQueryParameter, QueryParam.class, method,
					javadoc);
		}
	}

	private void addParameter(final String name, final INamedParam parameter,
			final Class<? extends Annotation> annotationClass,
			final JMethod method, final JDocComment javadoc) throws Exception {
		if (this.context.getConfiguration().getIgnoredParameterNames().contains(name)){
			return;
		}
		for (GeneratorExtension e : extensions) {
			if (!e.AddParameterFilter(name, parameter, annotationClass, method)) {
				return;
			}
		}

		final String argumentName = Names.buildVariableName(name);

		final JVar argumentVariable = method
				.param(types.buildParameterType(parameter, argumentName),
						argumentName);

		argumentVariable.annotate(annotationClass).param(
				DEFAULT_ANNOTATION_PARAMETER, name);

		if (parameter.getDefaultValue() != null) {
			argumentVariable.annotate(DefaultValue.class).param(
					DEFAULT_ANNOTATION_PARAMETER, parameter.getDefaultValue());
		}

		if (context.getConfiguration().isUseJsr303Annotations()) {
			new ParameterValidationGenerator().addValidation(parameter, argumentVariable);
		}

		addParameterJavaDoc(parameter, argumentVariable.name(), javadoc);
	}

	private void addResourceMethods(final JDefinedClass resourceInterface,
			final Resource resource,
			final String resourceInterfacePath,
			final Action action,
			final MimeType bodyMimeType,
			final boolean addBodyMimeTypeInMethodName) throws Exception {
		final Collection<MimeType> uniqueResponseMimeTypes = getUniqueResponseMimeTypes(action);

		addResourceMethod(resourceInterface, resource, resourceInterfacePath, action,
				bodyMimeType, addBodyMimeTypeInMethodName,
				uniqueResponseMimeTypes);
	}
}