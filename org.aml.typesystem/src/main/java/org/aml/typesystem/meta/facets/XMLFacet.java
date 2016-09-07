package org.aml.typesystem.meta.facets;

import java.util.List;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.internal.annotations.Name;
import org.aml.typesystem.meta.TypeInformation;

/**
 * <p>XMLFacet class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
@Name("xml")
public class XMLFacet extends TypeInformation {

	private boolean attribute;
	private String name;
	private String namespace;
	private List<String> order;
	private String prefix;
	private boolean wrapped;

	/**
	 * <p>Constructor for XMLFacet.</p>
	 */
	public XMLFacet() {
		super(true);
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
	 * <p>Getter for the field <code>namespace</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNamespace() {
		return namespace;
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
	 * <p>Getter for the field <code>prefix</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * <p>isAttribute.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isAttribute() {
		return attribute;
	}

	/**
	 * <p>isWrapped.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isWrapped() {
		return wrapped;
	}

	/**
	 * <p>Setter for the field <code>attribute</code>.</p>
	 *
	 * @param attribute a boolean.
	 */
	public void setAttribute(boolean attribute) {
		this.attribute = attribute;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>Setter for the field <code>namespace</code>.</p>
	 *
	 * @param namespace a {@link java.lang.String} object.
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * <p>Setter for the field <code>order</code>.</p>
	 *
	 * @param order a {@link java.util.List} object.
	 */
	public void setOrder(List<String> order) {
		this.order = order;
	}

	/**
	 * <p>Setter for the field <code>prefix</code>.</p>
	 *
	 * @param prefix a {@link java.lang.String} object.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * <p>Setter for the field <code>wrapped</code>.</p>
	 *
	 * @param wrapped a boolean.
	 */
	public void setWrapped(boolean wrapped) {
		this.wrapped = wrapped;
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}
}
