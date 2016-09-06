package org.aml.typesystem.java;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.IFieldModel;
import org.aml.typesystem.IMember;
import org.aml.typesystem.IMethodModel;
import org.aml.typesystem.ITypeModel;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.TypeRegistryImpl;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;

public class JavaTypeBuilder {

	protected TypeRegistryImpl typeRegistry=new TypeRegistryImpl(BuiltIns.getBuiltInTypes());
	protected TypeBuilderConfig config=new TypeBuilderConfig();
	
	public TypeBuilderConfig getConfig() {
		return config;
	}

	public void setConfig(TypeBuilderConfig config) {
		this.config = config;
	}
	

	public AbstractType getType(ITypeModel mdl) {
		if (mdl.getFullyQualifiedName().equals(Object.class.getName())){
			return BuiltIns.OBJECT;
		}
		if (mdl.getFullyQualifiedName().equals(String.class.getName())){
			return BuiltIns.STRING;
		}
		if (mdl.getFullyQualifiedName().equals(boolean.class.getName())){
			return BuiltIns.BOOLEAN;
		}
		String name=config.getNamingConvention().name(mdl);		
		AbstractType existingType = typeRegistry.getType(name);
		if (existingType!=null){
			return existingType;
		}
		AbstractType type = BuiltinsBuilder.getInstance().getType(mdl);
		if (type!=null){
			typeRegistry.registerType(type);
			return type;
		}		
		if (mdl.getComponentType()!=null){
			AbstractType superType=BuiltIns.ARRAY;
			AbstractType tp=TypeOps.derive("",superType);
			tp.addMeta(new ComponentShouldBeOfType(getType(mdl.getComponentType())));
			return tp;
		}
		AbstractType superType=BuiltIns.OBJECT;
		ITypeModel superClass = mdl.getSuperClass();
		if (superClass!=null){
			superType=getType(superClass);
		}
		
		AbstractType tp=TypeOps.derive(name,superType);
		typeRegistry.registerType(tp);
		
		IMemberFilter memberFilter = config.getMemberFilter();
		ArrayList<IMember>members=new ArrayList<>();
		
		for (IFieldModel f:mdl.getFields()){
			if (f.isStatic()){
				continue;
			}
			if (memberFilter.accept(f)){
				members.add(f);
			}
		}
		for (IMethodModel m:mdl.getMethods()){
			if (m.isStatic()){
				continue;
			}
			if (memberFilter.accept(m)){
				members.add(m);
			}
		}
		LinkedHashMap<String, IMember>props=new LinkedHashMap<>();		
		for (IMember m:members){
			String pName=config.getPropertyNameBuilder().buildName(m);
			props.put(pName, m);
		}
		for (String p:props.keySet()){
			IMember iMember = props.get(p);
			tp.declareProperty(p, buildType(iMember), config.getCheckNullable().isOptional(iMember));
		}
		return tp;
	}

	private AbstractType buildType(IMember iMember) {
		if (iMember.isCollection()){
			ITypeModel collectionMemberType = iMember.getCollectionMemberType();
			return TypeOps.array(getType(collectionMemberType));
		}
		AbstractType type = getType(iMember.getType());		
		return type;
	}

	public ITypeRegistry getRegistry() {
		return this.typeRegistry;
	}
}