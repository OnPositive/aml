package org.aml.typesystem.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Map;

import org.aml.typesystem.IDocInfo;
import org.aml.typesystem.IMethodModel;
import org.aml.typesystem.IParameterModel;
import org.aml.typesystem.ITypeModel;

/**
 * <p>ReflectionMethod class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class ReflectionMethod extends ReflectionGenericElement<Method> implements IMethodModel {

	/**
	 * <p>Constructor for ReflectionMethod.</p>
	 *
	 * @param element a {@link java.lang.reflect.Method} object.
	 */
	public ReflectionMethod(Method element) {
		super(element);
	}

	
	/**
	 * {@inheritDoc}
	 *
	 * <p>getParameters.</p>
	 */
	@Override
	public IParameterModel[] getParameters() {
		Class<?>[] parameterTypes = element.getParameterTypes();
		Annotation[][] parameterAnnotations = element.getParameterAnnotations();
		IParameterModel[] models=new IParameterModel[parameterTypes.length];
		int a=0;
		for (Class<?>cl:parameterTypes){
			models[a++]=new ReflectionParameter(cl, parameterAnnotations[a-1], "arg"+a);
		}
		return models;
	}

	
	/**
	 * {@inheritDoc}
	 *
	 * <p>getBasicDocInfo.</p>
	 */
	@Override
	public IDocInfo getBasicDocInfo() {
		return new IDocInfo() {
			
			
			@Override
			public String getReturnInfo() {
				return "";
			}
			
			
			@Override
			public String getDocumentation(String pName) {
				return "";
			}
			
			
			@Override
			public String getDocumentation() {
				return "";
			}
		};
	}

	
	/**
	 * {@inheritDoc}
	 *
	 * <p>getReturnedType.</p>
	 */
	@Override
	public ITypeModel getReturnedType() {
		Class<?> returnType = element.getReturnType();
		return new ReflectionType(returnType);
	}

	
	/**
	 * {@inheritDoc}
	 *
	 * <p>getName.</p>
	 */
	@Override
	public String getName() {
		return element.getName();
	}

	
	


	/** {@inheritDoc} */
	@Override
	public boolean isStatic() {
		return Modifier.isStatic(element.getModifiers());
	}


	/** {@inheritDoc} */
	@Override
	public boolean isPublic() {
		return Modifier.isPublic(element.getModifiers());		
	}


	/** {@inheritDoc} */
	@Override
	public ITypeModel getType() {
		return getReturnedType();
	}


	


	/** {@inheritDoc} */
	@Override
	public Class<?> getJavaType() {
		return element.getReturnType();
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasGenericReturnType() {

		Type gType = this.element.getGenericReturnType();
		String typeName = this.element.getReturnType().getName();
		String gTypeName = gType.toString();
		if(gTypeName.startsWith("class ")){
			gTypeName = gTypeName.substring("class ".length());
		}
		
		if(!gTypeName.startsWith(typeName)){
			return true;
		}		
		if(gType instanceof ParameterizedType){
			Type[] args = ((ParameterizedType)gType).getActualTypeArguments();
			if(args!=null&&args.length!=0){
				for(Type arg : args){
					if(arg instanceof TypeVariable){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isCollection() {
		return Collection.class.isAssignableFrom(this.element.getReturnType());
	}

	/** {@inheritDoc} */
	@Override
	public boolean isMap() {
		return Map.class.isAssignableFrom(this.element.getReturnType());
	}


	/** {@inheritDoc} */
	@Override
	public Object defaultValue() {
		return this.element.getDefaultValue();
	}
}
