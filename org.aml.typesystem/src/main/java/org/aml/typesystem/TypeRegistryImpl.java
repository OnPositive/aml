package org.aml.typesystem;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * <p>TypeRegistryImpl class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class TypeRegistryImpl implements ITypeRegistry {

	protected final LinkedHashMap<String, AbstractType> map = new LinkedHashMap<>();
	protected final ITypeRegistry parent;

	/**
	 * <p>Constructor for TypeRegistryImpl.</p>
	 *
	 * @param parent a {@link org.aml.typesystem.ITypeRegistry} object.
	 */
	public TypeRegistryImpl(ITypeRegistry parent) {
		super();
		this.parent = parent;
	}

	/**
	 * <p>allTypes.</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<AbstractType> allTypes() {
		return map.values();
	}

	/** {@inheritDoc} */
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

	/**
	 * <p>registerType.</p>
	 *
	 * @param tp a {@link org.aml.typesystem.AbstractType} object.
	 */
	public void registerType(AbstractType tp) {
		map.put(tp.name(), tp);
	}

	/** {@inheritDoc} */
	@Override
	public Collection<AbstractType> types() {
		return map.values();
	}

	public boolean hasDeclaration(String typeName) {
		return map.containsKey(typeName);
	}

	@Override
	public Iterator<AbstractType> iterator() {
		return map.values().iterator();
	}
}
