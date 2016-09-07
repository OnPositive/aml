package org.aml.typesystem.reflection;

import java.lang.reflect.TypeVariable;

import org.aml.typesystem.ITypeParameter;

/**
 * <p>ReflectionTypeParameter class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class ReflectionTypeParameter implements ITypeParameter {
	
	/**
	 * <p>Constructor for ReflectionTypeParameter.</p>
	 *
	 * @param element a {@link java.lang.reflect.TypeVariable} object.
	 */
	public ReflectionTypeParameter(TypeVariable<?> element) {
		super();
		this.element = element;
	}

	protected TypeVariable<?> element;
	
	/** {@inheritDoc} */
	@Override
	public String getName() {
		return element.getName();
	}

}
