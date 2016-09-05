package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.TypeInformation;

public class Annotation extends TypeInformation implements ISimpleFacet {

	protected String name;
	protected Object value;

	public Annotation(String annotationName, Object value) {
		super(false);
		this.name = annotationName;
		this.value = value;
	}

	@Override
	public String facetName() {
		return "(" + name + ")";
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	

	@Override
	public Status validate(ITypeRegistry registry) {
		final AbstractType type = registry.getType(this.name);
		if (type == null) {
			return new Status(Status.ERROR, 1231, "unknown annotation type " + name);
		} else {
			return type.validate(this.value);
		}
	}

	@Override
	public Object value() {
		return value;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}
}