package org.aml.typesystem.beans;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.facets.Default;
import org.aml.typesystem.meta.facets.Description;

/**
 * <p>PropertyBean class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class PropertyBean implements IProperty {

	protected boolean additional;
	protected boolean positional;
	protected AbstractType declaredAt;
	protected String id;
	
	protected boolean isMap;

	protected boolean required;

	
	protected AbstractType type;

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyBean other = (PropertyBean) obj;
		if (additional != other.additional)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isMap != other.isMap)
			return false;
		if (required != other.required)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType getDeclaredAt() {
		return declaredAt;
	}

	/** {@inheritDoc} */
	@Override
	public XMLHints getXMLHints(){
		return new XMLHints(this.id, type);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (additional ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isMap ? 1231 : 1237);
		result = prime * result + (required ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public String id() {
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAdditional() {
		return additional;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isMap() {
		return isMap;
	}
	/** {@inheritDoc} */
	@Override
	public boolean isRequired() {
		return required;
	}
	/** {@inheritDoc} */
	@Override
	public AbstractType range() {
		return type;
	}
	
	/**
	 * <p>Setter for the field <code>additional</code>.</p>
	 *
	 * @param additional a boolean.
	 */
	public void setAdditional(boolean additional) {
		this.additional = additional;
	}

	/**
	 * <p>Setter for the field <code>declaredAt</code>.</p>
	 *
	 * @param declaredAt a {@link org.aml.typesystem.AbstractType} object.
	 */
	public void setDeclaredAt(AbstractType declaredAt) {
		this.declaredAt = declaredAt;
	}
	
	/**
	 * <p>Setter for the field <code>id</code>.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * <p>setMap.</p>
	 *
	 * @param isMap a boolean.
	 */
	public void setMap(boolean isMap) {
		this.isMap = isMap;
	}

	/**
	 * <p>Setter for the field <code>required</code>.</p>
	 *
	 * @param required a boolean.
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * <p>Setter for the field <code>type</code>.</p>
	 *
	 * @param type a {@link org.aml.typesystem.AbstractType} object.
	 */
	public void setType(AbstractType type) {
		this.type = type;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return id+":"+type.name();
	}

	@Override
	public String description() {
		Description oneMeta = type.oneMeta(Description.class);
		if (oneMeta!=null){
			return oneMeta.value();
		}
		return null;
	}
	
	public Object defaultValue() {
		Default oneMeta = type.oneMeta(Default.class);
		if (oneMeta!=null){
			return oneMeta.value();
		}
		return null;
	}

	@Override
	public boolean isPositional() {	
		return positional;
	}

	public void setPositional(boolean positional) {
		this.positional = positional;
	}

}
