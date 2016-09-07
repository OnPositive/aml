package org.aml.typesystem;

import java.util.LinkedHashSet;
import java.util.Set;

import org.aml.typesystem.meta.BasicMeta;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Polymorphic;
import org.aml.typesystem.meta.facets.internal.OriginalName;
import org.aml.typesystem.meta.restrictions.KnownPropertyRestricton;

/**
 * <p>InheritedType class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class InheritedType extends AbstractType {

	protected final LinkedHashSet<AbstractType> superTypes = new LinkedHashSet<>();

	/**
	 * <p>Constructor for InheritedType.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param types a {@link org.aml.typesystem.AbstractType} object.
	 */
	protected InheritedType(String name, AbstractType... types) {
		super(name);
		for (final AbstractType t : types) {
			addSuperType(t);
		}
	}

	/**
	 * <p>addSuperType.</p>
	 *
	 * @param t a {@link org.aml.typesystem.AbstractType} object.
	 */
	protected void addSuperType(AbstractType t) {
		this.superTypes.add(t);
		if (!t.metaInfo.contains(BasicMeta.BUILTIN)) {
			t.subTypes.add(this);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void fillDependencies(LinkedHashSet<AbstractType> ts) {
		super.fillDependencies(ts);
		for (final AbstractType t : this.superTypes) {
			addPotentialDependency(ts, t);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Set<TypeInformation> meta() {
		LinkedHashSet<TypeInformation>result=new LinkedHashSet<>();
		for (final AbstractType t : this.superTypes()) {
			final Set<TypeInformation> meta2 = t.meta();
			for (final TypeInformation m : meta2) {
				if (m.isInheritable() && !(m instanceof KnownPropertyRestricton)) {
					result.add(m);
				}
			}
		}
		result.addAll(super.meta());	
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public Set<AbstractType> superTypes() {
		return new LinkedHashSet<>(superTypes);
	}
	
	/** {@inheritDoc} */
	@Override
	public AbstractType noPolymorph(){
		if (this.oneMeta(Polymorphic.class)==null){
			return this;
		}
		final InheritedType abstractType = new InheritedType(this.name+"NoPolymorph");
		for (AbstractType t: this.superTypes){
			abstractType.superTypes.add(t.noPolymorph());
		}
		for (TypeInformation i:this.metaInfo){
			if (!(i instanceof Polymorphic)){
				abstractType.metaInfo.add(i);
			}
			
		}
		abstractType.addMeta(new OriginalName(this.name));
		return abstractType;
	}
}
