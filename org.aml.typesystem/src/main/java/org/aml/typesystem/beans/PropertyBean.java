package org.aml.typesystem.beans;

import org.aml.typesystem.AbstractType;

public class PropertyBean implements IProperty {

	protected boolean additional;
	protected AbstractType declaredAt;
	protected String id;
	
	protected boolean isMap;

	protected boolean required;

	
	protected AbstractType type;

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

	public AbstractType getDeclaredAt() {
		return declaredAt;
	}

	public XMLHints getXMLHints(){
		return new XMLHints(this.id, type);
	}

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

	@Override
	public String id() {
		return id;
	}

	@Override
	public boolean isAdditional() {
		return additional;
	}

	@Override
	public boolean isMap() {
		return isMap;
	}
	@Override
	public boolean isRequired() {
		return required;
	}
	public AbstractType range() {
		return type;
	}
	
	public void setAdditional(boolean additional) {
		this.additional = additional;
	}

	public void setDeclaredAt(AbstractType declaredAt) {
		this.declaredAt = declaredAt;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setMap(boolean isMap) {
		this.isMap = isMap;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public void setType(AbstractType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return id+":"+type.name();
	}

}