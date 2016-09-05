package org.aml.typesystem;

import java.util.Collection;
import java.util.LinkedHashMap;

public class TypeRegistryImpl implements ITypeRegistry {

	protected final LinkedHashMap<String, AbstractType> map = new LinkedHashMap<>();
	protected final ITypeRegistry parent;

	public TypeRegistryImpl(ITypeRegistry parent) {
		super();
		this.parent = parent;
	}

	public Collection<AbstractType> allTypes() {
		return map.values();
	}

	@Override
	public AbstractType getType(String type) {
		final AbstractType abstractType = map.get(type);
		if (abstractType != null) {
			return abstractType;
		}
		if (this.parent != null) {
			return parent.getType(type);
		}
		return null;
	}

	public void registerType(AbstractType tp) {
		map.put(tp.name(), tp);
	}
}
