package org.aml.typesystem.beans;

import java.util.ArrayList;
import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.facets.XMLFacet;
import org.aml.typesystem.meta.facets.internal.OriginalName;

public class XMLHints implements IXMLHints {

	public boolean attribute;

	public String name;

	public String namespace;

	public String prefix;

	public boolean wrapped;

	private List<String> order = new ArrayList<>();

	public XMLHints(String id) {
		String v = id;
		if (v.charAt(0) == '@') {
			attribute = true;
			v = v.substring(1);
		}
		final int indexOf = v.indexOf(':');
		if (indexOf != -1) {
			prefix = v.substring(0, indexOf);
			v = v.substring(indexOf + 1);
		}
		final int indexOf2 = v.indexOf('/');
		if (indexOf2 != -1) {
			namespace = v.substring(indexOf2 + 1);
			v = v.substring(0, indexOf2);
		}
		this.name = v;
	}

	public XMLHints(String name, AbstractType tp) {
		this.name = name;

		if (name == null) {
			this.name = tp.name();
			OriginalName oneMeta = tp.oneMeta(OriginalName.class);
			if (oneMeta!=null){
				this.name=oneMeta.value();
			}
		}
		final XMLFacet oneMeta = tp.oneMeta(XMLFacet.class);
		if (oneMeta != null) {
			wrapped = oneMeta.isWrapped();
			final List<String> order2 = oneMeta.getOrder();
			if (order2 != null) {
				this.order = order2;
			}
			this.attribute = oneMeta.isAttribute();
			this.prefix = oneMeta.getPrefix();
			this.namespace = oneMeta.getNamespace();
			if (oneMeta.getName() != null) {
				this.name = oneMeta.getName();
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final XMLHints other = (XMLHints) obj;
		if (attribute != other.attribute) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (namespace == null) {
			if (other.namespace != null) {
				return false;
			}
		} else if (!namespace.equals(other.namespace)) {
			return false;
		}
		if (prefix == null) {
			if (other.prefix != null) {
				return false;
			}
		} else if (!prefix.equals(other.prefix)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (attribute ? 1231 : 1237);
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (namespace == null ? 0 : namespace.hashCode());
		result = prime * result + (prefix == null ? 0 : prefix.hashCode());
		return result;
	}

	public String qualifiedName() {
		if (prefix != null) {
			return prefix + ":" + name;
		}
		return name;
	}

	@Override
	public String toString() {
		final StringBuilder bld = new StringBuilder();
		if (attribute) {
			bld.append('@');
		}
		if (prefix != null) {
			bld.append(prefix);
			bld.append(':');
		}
		bld.append(name);
		if (namespace != null) {
			bld.append('/');
			bld.append(namespace);
		}
		return bld.toString();
	}

	public List<String> order() {

		return order;
	}

	@Override
	public boolean isAttribute() {
		return attribute;
	}

	@Override
	public String localName() {
		return name;
	}

	@Override
	public boolean wrapped() {
		return wrapped;
	}
}