
package org.aml.raml2java;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface KindAnnotated {


    public Kind value() default Kind.BASIC;

}
