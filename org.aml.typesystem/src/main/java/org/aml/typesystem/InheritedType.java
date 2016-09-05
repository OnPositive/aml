package org.aml.typesystem;

import java.util.LinkedHashSet;
import java.util.Set;

import org.aml.typesystem.meta.BasicMeta;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Polymorphic;
import org.aml.typesystem.meta.facets.internal.OriginalName;
import org.aml.typesystem.meta.restrictions.KnownPropertyRestricton;

public class InheritedType extends AbstractType {

	protected final LinkedHashSet<AbstractType> superTypes = new LinkedHashSet<>();

	protected InheritedType(String name, AbstractType... types) {
		super(name);
		for (final AbstractType t : types) {
			addSuperType(t);
		}
	}

	protected void addSuperType(AbstractType t) {
		this.superTypes.add(t);
		if (!t.metaInfo.contains(BasicMeta.BUILTIN)) {
			t.subTypes.add(this);
		}
	}

	@Override
	protected void fillDependencies(LinkedHashSet<AbstractType> ts) {
		super.fillDependencies(ts);
		for (final AbstractType t : this.superTypes) {
			addPotentialDependency(ts, t);
		}
	}

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

	@Override
	public Set<AbstractType> superTypes() {
		return new LinkedHashSet<>(superTypes);
	}
	
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