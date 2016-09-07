package org.aml.typesystem.meta;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

/**
 * <p>Abstract TypeInformation class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class TypeInformation implements Cloneable {

	protected final boolean inheritable;

	protected AbstractType ownerType;
	
	/**
	 * <p>requiredType.</p>
	 *
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public abstract AbstractType requiredType();

	/**
	 * <p>Constructor for TypeInformation.</p>
	 *
	 * @param inheritable a boolean.
	 */
	public TypeInformation(boolean inheritable) {
		super();
		this.inheritable = inheritable;
	}

	/** {@inheritDoc} */
	@Override
	public TypeInformation clone() {
		try {
			return (TypeInformation) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * <p>isInheritable.</p>
	 *
	 * @return a boolean.
	 */
	public final boolean isInheritable() {
		return this.inheritable;
	}

	/**
	 * <p>ownerType.</p>
	 *
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public AbstractType ownerType() {
		return ownerType;
	}

	/**
	 * <p>Setter for the field <code>ownerType</code>.</p>
	 *
	 * @param ownerType a {@link org.aml.typesystem.AbstractType} object.
	 */
	public void setOwnerType(AbstractType ownerType) {
		this.ownerType = ownerType;
	}

	/**
	 * <p>validate.</p>
	 *
	 * @param registry a {@link org.aml.typesystem.ITypeRegistry} object.
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	public abstract Status validate(ITypeRegistry registry);

}
