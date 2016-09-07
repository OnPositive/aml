package org.aml.typesystem.meta.facets.internal;

import java.util.ArrayList;
import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.ITransientMeta;
import org.aml.typesystem.meta.TypeInformation;

/**
 * <p>PropertyOrderMeta class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class PropertyOrderMeta extends TypeInformation implements ITransientMeta {

	protected List<String> order = new ArrayList<>();

	/**
	 * <p>Constructor for PropertyOrderMeta.</p>
	 */
	public PropertyOrderMeta() {
		super(false);
	}

	/**
	 * <p>Getter for the field <code>order</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<String> getOrder() {
		return order;
	}

	/**
	 * <p>Setter for the field <code>order</code>.</p>
	 *
	 * @param order a {@link java.util.List} object.
	 */
	public void setOrder(List<String> order) {
		this.order = order;
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
