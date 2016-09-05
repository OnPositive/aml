package org.aml.typesystem;

import java.util.LinkedHashSet;
import java.util.Set;

import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.meta.restrictions.RestrictionsOptimizer;

public class TypeOps {

	public static AbstractType derive(String name, AbstractType... ankestors) {
		if (ankestors.length == 0) {
			throw new IllegalArgumentException("At least one ancestor is needed");
		}
		return new InheritedType(name, ankestors);
	}
	
	public static AbstractType deriveObjectType(String name) {
		return derive(name, BuiltIns.OBJECT);
	}

	public static AbstractType intersect(String name, AbstractType... option) {
		if (option.length == 0) {
			throw new IllegalArgumentException("At least one option is needed");
		}
		LinkedHashSet<Set<AbstractType>> allTypes = new LinkedHashSet<>();
		LinkedHashSet<AbstractRestricton> allRestrictons = new LinkedHashSet<>();
		for (AbstractType q : option) {
			allTypes.add(q.superTypes());
			allRestrictons.addAll(q.restrictions());
		}
		if (allTypes.size() == 1) {
			Set<AbstractRestricton> optimize = RestrictionsOptimizer.optimize(allRestrictons);
			Set<AbstractType> next = allTypes.iterator().next();
			AbstractType derive = TypeOps.derive("", next.toArray(new AbstractType[next.size()]));
			for (AbstractRestricton z : optimize) {
				derive.addMeta(z.clone());
			}
			if (!derive.isConfluent()) {
				return derive;
			}
		}
		return new IntersectionType(name, option);
	}

	public static UnionType union(String name, AbstractType... option) {
		if (option.length == 0) {
			throw new IllegalArgumentException("At least one option is needed");
		}
		return new UnionType(name, option);
	}

	public static AbstractType array(AbstractType componentType) {
		AbstractType derive = TypeOps.derive("", BuiltIns.ARRAY);
		derive.addMeta(new ComponentShouldBeOfType(componentType));
		return derive;
	}

}
