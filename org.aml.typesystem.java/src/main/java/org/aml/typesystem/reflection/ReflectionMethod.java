package org.aml.typesystem.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.List;
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
	 * <p>getParameters.</p>
	 *
	 * @return an array of {@link org.aml.typesystem.IParameterModel} objects.
	 */
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
	 * <p>getBasicDocInfo.</p>
	 *
	 * @return a {@link org.aml.typesystem.IDocInfo} object.
	 */
	public IDocInfo getBasicDocInfo() {
		return new IDocInfo() {
			
			
			public String getReturnInfo() {
				return "";
			}
			
			
			public String getDocumentation(String pName) {
				return "";
			}
			
			
			public String getDocumentation() {
				return "";
			}
		};
	}

	
	/**
	 * <p>getReturnedType.</p>
	 *
	 * @return a {@link org.aml.typesystem.ITypeModel} object.
	 */
	public ITypeModel getReturnedType() {
		Class<?> returnType = element.getReturnType();
		return new ReflectionType(returnType);
	}

	
	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return element.getName();
	}

	
	/**
	 * <p>getBodyType.</p>
	 *
	 * @return a {@link org.aml.typesystem.ITypeModel} object.
	 */
	public ITypeModel getBodyType() {
		return null;
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
	public List<ITypeModel> getJAXBTypes() {
		return Utils.getJAXBTypes(this.element);
	}


	/** {@inheritDoc} */
	@Override
	public Class<?> getJavaType() {
		return element.getReturnType();
	}

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
	
	public boolean isCollection() {
		return Collection.class.isAssignableFrom(this.element.getReturnType());
	}

	public boolean isMap() {
		return Map.class.isAssignableFrom(this.element.getReturnType());
	}
}
