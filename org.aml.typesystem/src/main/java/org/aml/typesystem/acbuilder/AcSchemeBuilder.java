package org.aml.typesystem.acbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.acbuilder.CompositeAcElement.TypeFamily;
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
			Set<AbstractType> unionTypeFamily = type.unionTypeFamily();
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
			for (ACProto p:protos){
				for (String s:p.required){
					p.element.getChildren().add(new TestPropertyAcElement(s, false));
				}
			}
			return scheme;
		}
		
		return null;
	}
}
