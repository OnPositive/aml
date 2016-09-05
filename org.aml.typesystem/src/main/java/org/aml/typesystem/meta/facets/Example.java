package org.aml.typesystem.meta.facets;

import java.util.ArrayList;
import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.TypeInformation;

public class Example extends TypeInformation implements ISimpleFacet {

	protected ArrayList<Annotation> annotations = new ArrayList<>();
	protected String description;

	protected String displayName;

	protected String name;

	protected Object value;

	public Example() {
		super(false);
	}

	public Example(String string, Object parseObjectFromValue) {
		super(false);
		this.name = string;
		this.value = parseObjectFromValue;
		
	}

	@Override
	public String facetName() {
		return name;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public String getDescription() {
		return description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getName() {
		return name;
	}

	public boolean isOnlyContent() {
		return this.annotations.isEmpty() && this.description == null && this.displayName == null;
	}

	public void setDescription(String string) {
		this.description = string;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		Object toValidate = this.value;
		return this.ownerType().validateDirect(toValidate);
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
