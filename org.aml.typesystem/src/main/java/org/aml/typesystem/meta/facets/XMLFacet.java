package org.aml.typesystem.meta.facets;

import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.internal.annotations.Name;
import org.aml.typesystem.meta.TypeInformation;

@Name("xml")
public class XMLFacet extends TypeInformation {

	private boolean attribute;
	private String name;
	private String namespace;
	private List<String> order;
	private String prefix;
	private boolean wrapped;

	public XMLFacet() {
		super(true);
	}

	public String getName() {
		return name;
	}

	public String getNamespace() {
		return namespace;
	}

	public List<String> getOrder() {
		return order;
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean isAttribute() {
		return attribute;
	}

	public boolean isWrapped() {
		return wrapped;
	}

	public void setAttribute(boolean attribute) {
		this.attribute = attribute;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setOrder(List<String> order) {
		this.order = order;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setWrapped(boolean wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		if (this.wrapped && !this.ownerType.isScalar()) {
			return new Status(Status.ERROR, 0, "scalars can not be wrapped explicitly");
		}
		if (this.attribute&&this.wrapped) {
			return new Status(Status.ERROR, 0, "wrapped and attribute can not be specified together");
		}
		if (this.attribute && !this.ownerType.isScalar()) {
			return new Status(Status.ERROR, 0, "only scalar types may be serialized as attributes");
		}
		return Status.OK_STATUS;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}
}