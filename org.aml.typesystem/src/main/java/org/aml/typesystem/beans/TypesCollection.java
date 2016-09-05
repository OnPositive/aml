package org.aml.typesystem.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.aml.typesystem.AbstractType;

public class TypesCollection implements Iterable<AbstractType> {

	protected ArrayList<AbstractType> types = new ArrayList<>();

	public void add(AbstractType t) {
		types.add(t);
	}

	public AbstractType getType(String name) {
		for (final AbstractType t : this.types) {
			if (t.name().equals(name)) {
				return t;
			}
		}
		return null;
	}

	public Collection<AbstractType> getTypes() {
		return types;
	}

	@Override
	public Iterator<AbstractType> iterator() {
		return types.iterator();
	}
}
