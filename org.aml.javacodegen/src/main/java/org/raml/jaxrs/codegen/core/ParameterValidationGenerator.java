package org.raml.jaxrs.codegen.core;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.aml.apimodel.INamedParam;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JVar;

public class ParameterValidationGenerator {

	
	protected void addValidation(final INamedParam parameter,
			final JVar argumentVariable) {
		if (isNotBlank(parameter.getPattern())) {
			JAnnotationUse patternAnnotation = argumentVariable.annotate(Pattern.class);
			patternAnnotation.param("regexp", parameter.getPattern());
		}

		final Integer minLength = parameter.getMinLength();
		final Integer maxLength = parameter.getMaxLength();
		if ((minLength != null) || (maxLength != null)) {
			final JAnnotationUse sizeAnnotation = argumentVariable
					.annotate(Size.class);

			if (minLength != null) {
				sizeAnnotation.param("min", minLength);
			}

			if (maxLength != null) {
				sizeAnnotation.param("max", maxLength);
			}
		}

		final BigDecimal minimum = parameter.getMinimum();
		if (minimum != null) {
			addMinMaxConstraint(parameter, "minimum", Min.class, minimum,
					argumentVariable);
		}

		final BigDecimal maximum = parameter.getMaximum();
		if (maximum != null) {
			addMinMaxConstraint(parameter, "maximum", Max.class, maximum,
					argumentVariable);
		}

		if (parameter.isRequired()) {
			argumentVariable.annotate(NotNull.class);
		}
	}
	private void addMinMaxConstraint(final INamedParam parameter,
			final String name, final Class<? extends Annotation> clazz,
			final BigDecimal value, final JVar argumentVariable) {
		try {
			final long boundary = value.longValueExact();
			argumentVariable.annotate(clazz).param(
					BasicGenerator.DEFAULT_ANNOTATION_PARAMETER, boundary);
		} catch (final ArithmeticException ae) {
			BasicGenerator.LOGGER.info("Non integer "
					+ name
					+ " constraint ignored for parameter: "
					+ ToStringBuilder.reflectionToString(parameter,
							SHORT_PREFIX_STYLE));
		}
	}
}
