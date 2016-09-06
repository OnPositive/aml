package org.aml.typesystem.java;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.IAnnotationModel;
import org.aml.typesystem.IFieldModel;
import org.aml.typesystem.IMember;
import org.aml.typesystem.IMethodModel;
import org.aml.typesystem.ITypeModel;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.TypeRegistryImpl;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.Default;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.reflection.AnnotationModel;

public class JavaTypeBuilder {

	protected TypeRegistryImpl typeRegistry = new TypeRegistryImpl(BuiltIns.getBuiltInTypes());
	protected TypeRegistryImpl annotationsTypeRegistry = new TypeRegistryImpl(BuiltIns.getBuiltInTypes());
	protected TypeBuilderConfig config = new TypeBuilderConfig();

	public TypeBuilderConfig getConfig() {
		return config;
	}

	public void setConfig(TypeBuilderConfig config) {
		this.config = config;
	}

	static class AnnotationsConfigInput {

		protected boolean required;
		protected boolean nullable;
		protected AbstractType type;
		public boolean isProperty;
	}

	public AbstractType getType(ITypeModel mdl) {
		if (mdl.getFullyQualifiedName().equals(Object.class.getName())) {
			return BuiltIns.OBJECT;
		}
		if (mdl.getFullyQualifiedName().equals(String.class.getName())) {
			return BuiltIns.STRING;
		}
		if (mdl.getFullyQualifiedName().equals(boolean.class.getName())) {
			return BuiltIns.BOOLEAN;
		}
		String name = config.getNamingConvention().name(mdl);
		TypeRegistryImpl typeRegistry = mdl.isAnnotation() ? this.annotationsTypeRegistry : this.typeRegistry;
		AbstractType existingType = typeRegistry.getType(name);
		if (existingType != null) {
			return existingType;
		}
		if (mdl.isAnnotation()) {
			if (mdl.getMethods().length == 0) {
				AbstractType tp = TypeOps.derive(name, BuiltIns.NIL);
				typeRegistry.registerType(tp);
				return tp;
			}
			if (mdl.getMethods().length == 1) {
				IMethodModel iMethodModel = mdl.getMethods()[0];
				if (iMethodModel.getName().equals("value")) {
					ITypeModel type = iMethodModel.getType();
					AbstractType tp = TypeOps.derive(name, getType(type));
					if (iMethodModel.defaultValue() != null) {
						tp.addMeta(new Default(iMethodModel.defaultValue()));
					}
					typeRegistry.registerType(tp);
					return tp;
				}
			}
		}
		AbstractType type = BuiltinsBuilder.getInstance().getType(mdl);
		if (type != null) {
			typeRegistry.registerType(type);
			return type;
		}
		IFieldModel[] fields = mdl.getFields();
		if (mdl.isEnum()) {
			AbstractType superType = BuiltIns.STRING;
			AbstractType tp = TypeOps.derive(name, superType);

			typeRegistry.registerType(tp);
			ArrayList<String> enumValues = new ArrayList<>();
			for (IFieldModel f : fields) {
				if (f.isStatic() && f.isPublic()) {
					enumValues.add(f.getName());
				}
			}
			tp.addMeta(new org.aml.typesystem.meta.restrictions.Enum(enumValues));
			return tp;
		}
		ITypeModel componentType = mdl.getComponentType();
		if (componentType != null) {
			AbstractType superType = BuiltIns.ARRAY;
			AbstractType tp = TypeOps.derive("", superType);
			if (componentType.isAnnotation()){
				
				componentType=new MaskedAnnotationType(componentType);
			}
			tp.addMeta(new ComponentShouldBeOfType(getType(componentType)));
			return tp;
		}

		AbstractType superType = BuiltIns.OBJECT;
		ITypeModel superClass = mdl.getSuperClass();
		if (superClass != null) {
			superType = getType(superClass);
		}

		AbstractType tp = TypeOps.derive(name, superType);
		typeRegistry.registerType(tp);

		IMemberFilter memberFilter = config.getMemberFilter();
		if (mdl.isAnnotation()||mdl instanceof MaskedAnnotationType) {
			memberFilter = new IMemberFilter() {

				@Override
				public boolean accept(IMember member) {
					return member instanceof IMethodModel;
				}
			};
		}
		ArrayList<IMember> members = new ArrayList<>();

		for (IFieldModel f : fields) {
			if (f.isStatic()) {
				continue;
			}
			if (memberFilter.accept(f)) {
				members.add(f);
			}
		}
		for (IMethodModel m : mdl.getMethods()) {
			if (m.isStatic()) {
				continue;
			}
			if (memberFilter.accept(m)) {
				members.add(m);
			}
		}
		LinkedHashMap<String, IMember> props = new LinkedHashMap<>();
		for (IMember m : members) {
			String pName = config.getPropertyNameBuilder().buildName(m);
			props.put(pName, m);
		}
		for (String p : props.keySet()) {
			IMember iMember = props.get(p);
			boolean optional = config.getCheckNullable().isOptional(iMember);
			ArrayList<IAnnotationModel> toProcess = new ArrayList<>();
			AbstractType buildType = buildType(iMember);
			AnnotationsConfigInput cfg = new AnnotationsConfigInput();
			boolean hasDefault = iMember.defaultValue() != null;
			if (hasDefault) {
				if (!buildType.isAnonimous()) {
					buildType = TypeOps.derive("", buildType);
				}
				buildType.addMeta(new Default(iMember.defaultValue()));
			}
			cfg.isProperty = true;
			cfg.type = buildType;
			cfg.required = !optional;

			for (IAnnotationModel annotation : iMember.getAnnotations()) {
				if (!config.getAnnotationsProcessingConfig().process(cfg, annotation)) {
					if (config.getAnnotationsFilter().preserve(annotation)) {
						toProcess.add(annotation);
					}
				}
			}
			cfg.type = appendAnnotations(cfg.type, toProcess, true);
			tp.declareProperty(p, cfg.type, !cfg.required);
		}
		ArrayList<IAnnotationModel> toProcess = new ArrayList<>();
		AnnotationsConfigInput cfg = new AnnotationsConfigInput();
		cfg.type = tp;

		for (IAnnotationModel annotation : mdl.getAnnotations()) {
			if (!config.getAnnotationsProcessingConfig().process(cfg, annotation)) {
				if (config.getAnnotationsFilter().preserve(annotation)) {
					toProcess.add(annotation);
				}
			}
		}
		tp = appendAnnotations(tp, toProcess, false);
		return tp;
	}

	private AbstractType appendAnnotations(AbstractType type, ArrayList<IAnnotationModel> toProcess, boolean prop) {
		if (toProcess.isEmpty()) {
			return type;
		}
		if (!type.isAnonimous() && prop) {
			type = TypeOps.derive("", type);
		}
		for (IAnnotationModel m : toProcess) {
			ITypeModel at = m.getType();

			AbstractType type2 = getType(at);
			Object vl = dumpValue(m);
			type.addMeta(new Annotation("(" + m.getName() + ")", vl, type2));
		}
		return type;
	}

	private Object dumpValue(IAnnotationModel m) {
		LinkedHashMap<String, Object> res = new LinkedHashMap<>();
		Map<String, Object> allValues = m.allValues();
		for (String s : allValues.keySet()) {
			Object value = allValues.get(s);
			if (value instanceof Annotation) {
				value = dumpValue(new AnnotationModel((java.lang.annotation.Annotation) value));
			}
			if (value instanceof Object[]) {
				Object[] arr = (Object[]) value;
				Object[] newVals = new Object[arr.length];
				for (int a = 0; a < arr.length; a++) {
					Object object = arr[a];
					newVals[a] = object;
					Class<?>[] interfaces = object.getClass().getInterfaces();
					if (interfaces.length == 1) {
						Class<?> clz = interfaces[0];
						if (clz.isAnnotation()) {							
							AnnotationModel m2 = new AnnotationModel((java.lang.annotation.Annotation) object);
							ITypeModel type = m2.getType();
							newVals[a] = dumpValue(m2);							
						}
					}
				}
				value = newVals;
			}
			res.put(s, value);
		}
		if (res.size() == 1 && res.containsKey("value") && m.getType().getMethods().length == 1) {
			return res.get("value");
		}
		return res;
	}

	private AbstractType buildType(IMember iMember) {
		
		if (iMember.isCollection()) {
			ITypeModel collectionMemberType = iMember.getCollectionMemberType();
			return TypeOps.array(getType(collectionMemberType));
		}
		if (iMember.getType().isAnnotation()){
			AbstractType type = getType(new MaskedAnnotationType(iMember.getType()));
			return type;
		}
		AbstractType type = getType(iMember.getType());
		return type;
	}

	public ITypeRegistry getRegistry() {
		return this.typeRegistry;
	}

	public ITypeRegistry getAnnotationTypeRegistry() {
		return this.annotationsTypeRegistry;
	}
}