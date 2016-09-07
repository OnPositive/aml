package org.aml.typesystem.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.aml.typesystem.IFieldModel;
import org.aml.typesystem.IMethodModel;
import org.aml.typesystem.ITypeModel;

/**
 * <p>ReflectionType class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class ReflectionType extends ReflectionGenericElement<Class<?>> implements ITypeModel{

	/**
	 * <p>Constructor for ReflectionType.</p>
	 *
	 * @param element a {@link java.lang.Class} object.
	 */
	public ReflectionType(Class<?> element) {
		super(element);
	
	}

	
	/**
	 * <p>getMethods.</p>
	 *
	 * @return an array of {@link org.aml.typesystem.IMethodModel} objects.
	 */
	public IMethodModel[] getMethods() {
		Method[] declaredMethods = element.getDeclaredMethods();
		IMethodModel[] methods=new IMethodModel[declaredMethods.length];
		int a=0;
		for (Method m:declaredMethods){
			methods[a++]=new ReflectionMethod(m);
		}
		return methods;
	}

	
	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return element.getSimpleName();
	}

	
	/**
	 * <p>getFullyQualifiedName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFullyQualifiedName() {
		return element.getCanonicalName();
	}


	/** {@inheritDoc} */
	@Override
	public IFieldModel[] getFields() {
		Field[] declaredFields= element.getDeclaredFields();
		IFieldModel[] fields=new IFieldModel[declaredFields.length];
		int a=0;
		for (Field m:declaredFields){
			fields[a++]=new ReflectionField(m);
		}
		return fields;
	}


	@Override
	public ITypeModel getSuperClass() {
		Class<?> superClass = this.element.getSuperclass();		
		return superClass!=null ? new ReflectionType(superClass) : null;
	}


	@Override
	public ITypeModel[] getImplementedInterfaces() {
		Class<?>[] interfaces = this.element.getInterfaces();
		if(interfaces==null||interfaces.length==0){
			return new ITypeModel[0];
		}
		ITypeModel[] arr = new ITypeModel[interfaces.length];
		for(int i = 0 ; i < interfaces.length ; i++){
			arr[i] = new ReflectionType(interfaces[i]);
		}
		return arr;
	}


	@Override
	public ITypeModel resolveClass(String qualifiedName) {
		try {
			Class<?> clazz = this.element.getClassLoader().loadClass(qualifiedName);
			if(clazz==null){
				return null;
			}
			return new ReflectionType(clazz);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}


	@Override
	public boolean isCollection() {
		if (this.element.isArray()){
			return true;
		}
		if (Collection.class.isAssignableFrom(this.element)){
			return true;
		}
		return false;
	}


	@Override
	public ITypeModel getComponentType() {
		if (this.element.isArray()){
			return new ReflectionType(element.getComponentType());
		}
		return null;
	}


	@Override
	public boolean isEnum() {
		return element.isEnum();
	}


	@Override
	public boolean isAnnotation() {
		return element.isAnnotation();
	}


	@Override
	public String getPackageName() {
		return element.getPackage().getName();
	}

}
