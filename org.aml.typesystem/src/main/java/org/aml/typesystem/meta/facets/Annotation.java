package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.TypeInformation;

/**
 * <p>Annotation class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Annotation extends TypeInformation implements ISimpleFacet {

	protected String name;
	protected Object value;
	protected AbstractType type;

	/**
	 * <p>Constructor for Annotation.</p>
	 *
	 * @param annotationName a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @param type2 a {@link org.aml.typesystem.AbstractType} object.
	 */
	public Annotation(String annotationName, Object value, AbstractType type2) {
		super(false);
		this.name = annotationName;
		this.value = value;
		this.type=type2;
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "(" + name + ")";
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(Object value) {
		this.value = value;
	}

	

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		if (type == null) {
			return new Status(Status.ERROR, 1231, "unknown annotation type " + name);
		} else {
			return type.validate(this.value);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Object value() {
		return value;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}
}
