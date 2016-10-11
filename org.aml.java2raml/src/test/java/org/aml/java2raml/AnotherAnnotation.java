package org.aml.java2raml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AnotherAnnotation {

	Annotation2[] xx();
}
