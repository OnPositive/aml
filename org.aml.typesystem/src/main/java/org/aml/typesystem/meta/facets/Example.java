package org.aml.typesystem.meta.facets;

import java.util.ArrayList;
import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.TypeInformation;

/**
 * <p>Example class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Example extends TypeInformation implements ISimpleFacet {

	protected ArrayList<Annotation> annotations = new ArrayList<>();
	protected String description;

	protected String displayName;

	protected String name;

	protected Object value;

	/**
	 * <p>Constructor for Example.</p>
	 */
	public Example() {
		super(false);
	}

	/**
	 * <p>Constructor for Example.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 * @param parseObjectFromValue a {@link java.lang.Object} object.
	 */
	public Example(String string, Object parseObjectFromValue) {
		super(false);
		this.name = string;
		this.value = parseObjectFromValue;
		
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return name;
	}

	/**
	 * <p>Getter for the field <code>annotations</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<Annotation> getAnnotations() {
		return annotations;
	}

	/**
	 * <p>Getter for the field <code>description</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>Getter for the field <code>displayName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDisplayName() {
		return displayName;
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
	 * <p>isOnlyContent.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isOnlyContent() {
		return this.annotations.isEmpty() && this.description == null && this.displayName == null;
	}

	/**
	 * <p>Setter for the field <code>description</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setDescription(String string) {
		this.description = string;
	}

	/**
	 * <p>Setter for the field <code>displayName</code>.</p>
	 *
	 * @param displayName a {@link java.lang.String} object.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		Object toValidate = this.value;
		return this.ownerType().validateDirect(toValidate);
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

	/** {@inheritDoc} */
	@Override
	public void setValue(Object vl) {
		this.value=vl;
	}

}
