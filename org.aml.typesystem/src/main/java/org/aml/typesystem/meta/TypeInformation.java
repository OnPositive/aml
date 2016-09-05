package org.aml.typesystem.meta;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

public abstract class TypeInformation implements Cloneable {

	protected final boolean inheritable;

	protected AbstractType ownerType;
	
	public abstract AbstractType requiredType();

	public TypeInformation(boolean inheritable) {
		super();
		this.inheritable = inheritable;
	}

	@Override
	public TypeInformation clone() {
		try {
			return (TypeInformation) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

	public final boolean isInheritable() {
		return this.inheritable;
	}

	public AbstractType ownerType() {
		return ownerType;
	}

	public void setOwnerType(AbstractType ownerType) {
		this.ownerType = ownerType;
	}

	public abstract Status validate(ITypeRegistry registry);

}
