package org.aml.typesystem;

import java.util.LinkedHashSet;
import java.util.Set;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.meta.restrictions.ExternalSchemaMeta;
import org.aml.typesystem.meta.restrictions.RestrictionsOptimizer;

/**
 * <p>TypeOps class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class TypeOps {

	/**
	 * <p>derive.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param ankestors a {@link org.aml.typesystem.AbstractType} object.
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public static AbstractType derive(String name, AbstractType... ankestors) {
		
		if (ankestors.length == 0) {
			throw new IllegalArgumentException("At least one ancestor is needed");
		}
		InheritedType inheritedType = new InheritedType(name, ankestors);
		boolean hasNullable=false;
		boolean hasNotNullable=false;
		for (AbstractType t:ankestors){
			if(t.isNullable()){
				hasNullable=true;
			}
			else{
				hasNotNullable=true;
			}
		}
		if (hasNullable&&!hasNotNullable){
			inheritedType.setNullable(true);
		}
		return inheritedType;
	}
	
	/**
	 * <p>deriveObjectType.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public static AbstractType deriveObjectType(String name) {
		return derive(name, BuiltIns.OBJECT);
	}

	/**
	 * <p>intersect.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param option a {@link org.aml.typesystem.AbstractType} object.
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
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

	/**
	 * <p>union.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param option a {@link org.aml.typesystem.AbstractType} object.
	 * @return a {@link org.aml.typesystem.UnionType} object.
	 */
	public static UnionType union(String name, AbstractType... option) {
		
		if (option.length == 0) {
			throw new IllegalArgumentException("At least one option is needed");
		}
		return new UnionType(name, option);
	}

	/**
	 * <p>array.</p>
	 *
	 * @param componentType a {@link org.aml.typesystem.AbstractType} object.
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public static AbstractType array(AbstractType componentType) {
		AbstractType derive = TypeOps.derive("", BuiltIns.ARRAY);
		derive.addMeta(new ComponentShouldBeOfType(componentType));
		return derive;
	}

	
	public static AbstractType deriveExternal(String name,String schemaContent){
		AbstractType derive = TypeOps.derive(name,BuiltIns.EXTERNAL);
		derive.addMeta(new ExternalSchemaMeta(schemaContent));
		return derive;
		
	}
}
