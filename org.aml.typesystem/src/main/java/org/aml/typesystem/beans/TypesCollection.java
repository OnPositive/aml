package org.aml.typesystem.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.aml.typesystem.AbstractType;

/**
 * <p>TypesCollection class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class TypesCollection implements Iterable<AbstractType> {

	protected ArrayList<AbstractType> types = new ArrayList<>();

	/**
	 * <p>add.</p>
	 *
	 * @param t a {@link org.aml.typesystem.AbstractType} object.
	 */
	public void add(AbstractType t) {
		types.add(t);
	}

	/**
	 * <p>getType.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public AbstractType getType(String name) {
		for (final AbstractType t : this.types) {
			if (t.name().equals(name)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * <p>Getter for the field <code>types</code>.</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<AbstractType> getTypes() {
		return types;
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<AbstractType> iterator() {
		return types.iterator();
	}
}
