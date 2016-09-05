package org.aml.typesystem.meta.facets.internal;

import java.util.ArrayList;
import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.ITransientMeta;
import org.aml.typesystem.meta.TypeInformation;

public class PropertyOrderMeta extends TypeInformation implements ITransientMeta {

	protected List<String> order = new ArrayList<>();

	public PropertyOrderMeta() {
		super(false);
	}

	public List<String> getOrder() {
		return order;
	}

	public void setOrder(List<String> order) {
		this.order = order;
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
