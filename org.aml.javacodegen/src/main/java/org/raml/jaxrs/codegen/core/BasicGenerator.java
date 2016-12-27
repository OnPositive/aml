package org.raml.jaxrs.codegen.core;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.File;
import java.io.Reader;
import java.util.LinkedHashSet;
import java.util.Set;

import org.aml.apimodel.Api;
import org.aml.apimodel.TopLevelModel;
import org.apache.commons.lang.Validate;
import org.raml.jaxrs.codegen.core.Configuration.JaxrsVersion;
import org.raml.v2.api.model.common.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasicGenerator {

	/** Constant <code>DEFAULT_ANNOTATION_PARAMETER="value"</code> */
	protected static final String DEFAULT_ANNOTATION_PARAMETER = "value";
	/** Constant <code>LOGGER</code> */
	protected static final Logger LOGGER = LoggerFactory
				.getLogger(Generator.class);

	public BasicGenerator() {
		super();
	}

	protected void validate(final Configuration configuration) {
		Validate.notNull(configuration, "configuration can't be null");
	
		final File outputDirectory = configuration.getOutputDirectory();
		Validate.notNull(outputDirectory, "outputDirectory can't be null");
	
		Validate.isTrue(outputDirectory.isDirectory(), outputDirectory
				+ " is not a pre-existing directory");
		Validate.isTrue(outputDirectory.canWrite(), outputDirectory
				+ " can't be written to");
	
		if (outputDirectory.listFiles().length > 0) {
			LOGGER.warn("Directory "
					+ outputDirectory
					+ " is not empty, generation will work but pre-existing files may remain and produce unexpected results");
		}
	
		Validate.notEmpty(configuration.getBasePackageName(),
				"base package name can't be empty");
	}

	/**
	 * <p>run.</p>
	 *
	 * @param raml a {@link org.aml.apimodel.Api} object.
	 * @param configuration a {@link org.raml.jaxrs.codegen.core.Configuration} object.
	 * @return a {@link java.util.Set} object.
	 * @throws java.lang.Exception if any.
	 */
	public Set<String> run(final Reader raml, final Configuration configuration) throws Exception {
		System.out.println("relative includes are not supported in this mode!");
		return run(raml,configuration,"",true);
	}

	/**
	 * <p>toDetailedString.</p>
	 *
	 * @param item a {@link ValidationResult} object.
	 * @return a {@link java.lang.String} object.
	 */
	public Set<String> run(final Reader ramlReader, final Configuration configuration, String readerLocation) throws Exception {
		return run(ramlReader, configuration,readerLocation,false);
	}

	/**
	 * <p>run.</p>
	 *
	 * @param ramlReader a {@link java.io.Reader} object.
	 * @param configuration a {@link org.raml.jaxrs.codegen.core.Configuration} object.
	 * @return a {@link java.util.Set} object.
	 * @throws java.lang.Exception if any.
	 * @param readerLocation a {@link java.lang.String} object.
	 */
	public Set<String> run(final Reader ramlReader, final Configuration configuration, String readerLocation, boolean ignoreErrors) throws Exception {
		if (isNotBlank(configuration.getAsyncResourceTrait())
				&& configuration.getJaxrsVersion() == JaxrsVersion.JAXRS_1_1) {
			throw new IllegalArgumentException(
					"Asynchronous resources are not supported in JAX-RS 1.1");
		}
		TopLevelModel t=new APILoader().load(ramlReader, configuration, readerLocation, ignoreErrors);
		if (t instanceof Api){
			return run((Api) t,configuration);
		}
		return new LinkedHashSet<>();
	}

	protected abstract Set<String> run(Api t, Configuration configuration) throws Exception ;

}