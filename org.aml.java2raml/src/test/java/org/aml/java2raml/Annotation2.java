package org.aml.java2raml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation2 {
	String name();
	boolean type() default true;
}
