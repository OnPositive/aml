package org.aml.typesystem.acbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.acbuilder.CompositeAcElement.TypeFamily;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.meta.facets.Discriminator;
import org.aml.typesystem.meta.facets.DiscriminatorValue;

public class AcSchemeBuilder {

	static class ACProto{
		public ACProto(Set<String> rs, CompositeAcElement compositeAcElement) {
			this.required=new HashSet<>(rs);
			this.original=rs;
			this.element=compositeAcElement;
		}
		protected Set<String>original;
		protected HashSet<String>required;
		protected CompositeAcElement element;
	}
	
	public AcScheme build(AbstractType type) {
		if (type.isUnion()) {
			Set<AbstractType> unionTypeFamily = extendedUnionTypeFamily(type);
			AcScheme scheme = new AcScheme(type);
			HashSet<TypeFamily> filledKinds = new HashSet<>();
			HashMap<Set<String>, CompositeAcElement> objectTypes = new HashMap<>();
			for (AbstractType t : unionTypeFamily) {
				if (t.isScalar()) {
					if (t.isNumber()) {
						if (!filledKinds.add(TypeFamily.NUMBER)) {
							throw new IllegalArgumentException(
									"two number types inside of union type can not be discriminated properly");
						}
						scheme.getSchemes().put(t, new CompositeAcElement(TypeFamily.NUMBER));
					}
					if (t.isBoolean()) {
						if (!filledKinds.add(TypeFamily.BOOLEAN)) {
							throw new IllegalArgumentException(
									"two number types inside of union type can not be discriminated properly");
						}
						scheme.getSchemes().put(t, new CompositeAcElement(TypeFamily.BOOLEAN));
					} else {
						if (!filledKinds.add(TypeFamily.STRING)) {
							throw new IllegalArgumentException(
									"two string derived types inside of union type can not be discriminated properly");
						}
						scheme.getSchemes().put(t, new CompositeAcElement(TypeFamily.STRING));
					}
				}
				if (t.isObject()) {
					CompositeAcElement value = new CompositeAcElement(TypeFamily.OBJECT);
					value.setAssocitatedType(t);
					scheme.getSchemes().put(t, value);
					Discriminator oneMeta = t.oneMeta(Discriminator.class);
					if (oneMeta != null) {
						String propName = oneMeta.value();
						String propValue = t.name();
						DiscriminatorValue oneMeta2 = t.oneMeta(DiscriminatorValue.class);
						if (oneMeta2 != null) {
							propValue = "" + oneMeta2.value();
						}
						value.getChildren().add(new TestPropertyValueAcElement(propName, propValue));
					} else {
						Set<String> requiredPropertySet = t.requiredPropertySet();
						if (objectTypes.containsKey(requiredPropertySet)) {
							CompositeAcElement compositeAcElement = objectTypes.get(requiredPropertySet);
							throw new IllegalArgumentException(
									"can not discriminate two object types with similar required property set and no discriminator:"
											+ (requiredPropertySet) + ": " + t.name()
											+ compositeAcElement.getAssocitatedType());
							
						}
						else{
							objectTypes.put(requiredPropertySet, value);
						}
					}

				}
				if (t.isArray()) {
					if (!filledKinds.add(TypeFamily.ARRAY)) {
						throw new IllegalArgumentException("array discrimination is not supported");
					}
					scheme.getSchemes().put(t, new CompositeAcElement(TypeFamily.ARRAY));
				}
			}
			ArrayList<ACProto>protos=new ArrayList<>();
			for (Set<String>rs:objectTypes.keySet()){
				ACProto pr=new ACProto(rs,objectTypes.get(rs));
				protos.add(pr);
			}
			for (Set<String>rs:objectTypes.keySet()){
				for (ACProto p:protos){
					if (rs!=p.original){
						p.required.removeAll(rs);
					}
				}				
			}
			Collections.sort(protos,new Comparator<ACProto>() {

				@Override
				public int compare(ACProto o1, ACProto o2) {
					return o2.required.size()-o1.required.size();
				}
			});
			for (ACProto p:protos){
				for (String s:p.required){
					p.element.getChildren().add(new TestPropertyAcElement(s, false));
				}
			}
			for (ACProto p:protos){
				scheme.schemes.remove(p.element.associtatedType);
			}
//			scheme.schemes.clear();
			for (ACProto p:protos){
				scheme.schemes.put(p.element.associtatedType, p.element);
			}
			return scheme;
		}
		
		return null;
	}
	protected HashMap<AbstractType,Set<AbstractType>>extendedFamily=new HashMap<>();
	
	public Set<AbstractType> extendedUnionTypeFamily(AbstractType type) {
		if (extendedFamily.containsKey(type)){
			return extendedFamily.get(type);
		}
		Set<AbstractType> unionTypeFamily = type.unionTypeFamily();
		if (!type.toPropertiesView().properties().isEmpty()){
			LinkedHashSet<AbstractType>tps=new LinkedHashSet<>();
			for (AbstractType z:unionTypeFamily){
				AbstractType newType=TypeOps.derive(type.name()+z.name(), z);
				for (IProperty p:type.toPropertiesView().properties()){
					newType.declareProperty(p.id(), p.range(), !p.isRequired());
				}
				if (z.oneMeta(DiscriminatorValue.class)==null){
					newType.addMeta(new DiscriminatorValue(z.name()));
				}				
				tps.add(newType);					
			}
			unionTypeFamily=tps;
		}
		this.extendedFamily.put(type, unionTypeFamily);
		return unionTypeFamily;
	}
}
