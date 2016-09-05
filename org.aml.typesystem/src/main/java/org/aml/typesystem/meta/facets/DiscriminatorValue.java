package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

public class DiscriminatorValue extends Facet<Object> {

	public DiscriminatorValue(Object value) {
		super(value);
	}

	@Override
	public String facetName() {
		return "discriminatorValue";
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		if (!this.ownerType.isSubTypeOf(BuiltIns.OBJECT)){
			return new Status(Status.ERROR, 1, "discriminatorValue can only be used with object types");
		}
		final Discriminator d = this.ownerType.oneMeta(Discriminator.class);
		if (d == null) {
			return new Status(Status.ERROR, 1, "discriminatorValue can only be used when discriminator is defined.");
		}
		return Status.OK_STATUS;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
