package org.aml.typesystem.internal.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>Name class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

	String value();
}
