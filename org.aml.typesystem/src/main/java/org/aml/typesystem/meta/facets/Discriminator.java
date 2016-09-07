package org.aml.typesystem.meta.facets;

import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.restrictions.PropertyIs;

/**
 * <p>Discriminator class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Discriminator extends Facet<String> {

	/**
	 * <p>Constructor for Discriminator.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public Discriminator(String value) {
		super(value);
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "discriminator";
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		if (!ownerType.allSuperTypes().contains(BuiltIns.OBJECT)) {
			return new Status(Status.ERROR, 0, "discriminator facet can only be used with objects");
		}
		if (ownerType.isAnonimous()) {
			return new Status(Status.ERROR, 0, "discriminator can not be used with anonimous or final types");
		}
		if (!ownerType.directPropertySet().contains(this.value)) {
			return new Status(Status.ERROR, 0, "unknown property is used as discriminator");
		}
		for (final PropertyIs p : this.ownerType.meta(PropertyIs.class)) {
			if (p.id().equals(this.value)) {
				if (!p.range().allSuperTypes().contains(BuiltIns.SCALAR)) {
					return new Status(Status.ERROR, 0, "only scalar properties may be used as discriminator");
				}
			}
		}
		Set<Discriminator> meta = ownerType.meta(Discriminator.class);
		for (Discriminator d:meta){
			if (d!=this&&d.value!=this.value){
				return new Status(Status.ERROR, 0, "discriminator is not redefinable");
			}
		}
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
