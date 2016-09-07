package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.TypeInformation;

/**
 * <p>FacetDeclaration class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class FacetDeclaration extends TypeInformation {

	protected AbstractType facetType;
	protected String name;

	/**
	 * <p>Constructor for FacetDeclaration.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param ts a {@link org.aml.typesystem.AbstractType} object.
	 */
	public FacetDeclaration(String name, AbstractType ts) {
		super(true);
		this.name = name;
		this.facetType = ts;
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
	 * <p>getType.</p>
	 *
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public AbstractType getType() {
		return facetType;
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		if (ownerType.isAnonimous()) {
			return new Status(Status.ERROR, 0, "facet can not be declared in anonimous or final types");
		}
		if (facetType.isAnonimous()) {
			return facetType.validateMeta(null);
		}
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
