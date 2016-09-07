package org.aml.typesystem.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

import org.aml.typesystem.IAnnotationModel;
import org.aml.typesystem.IBasicModel;
import org.aml.typesystem.ITypeModel;

/**
 * <p>Abstract BasicReflectionMember class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class BasicReflectionMember<T extends AnnotatedElement> implements IBasicModel{

	private static final String VALUE = "value";
	protected T element;

	/**
	 * <p>getParameterTypes.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ITypeModel> getParameterTypes() {		
		return Utils.getParameterTypes(this.element);
	}
	
	/**
	 * <p>getCollectionMemberType.</p>
	 *
	 * @return a {@link org.aml.typesystem.ITypeModel} object.
	 */
	public ITypeModel getCollectionMemberType() {
		List<ITypeModel> parameterTypes = getParameterTypes();
		if(parameterTypes!=null&&parameterTypes.size()==1){
			return parameterTypes.get(0);
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>hashCode.</p>
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		return result;
	}

	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		BasicReflectionMember other = (BasicReflectionMember) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		return true;
	}

	/**
	 * <p>Constructor for BasicReflectionMember.</p>
	 *
	 * @param element a T object.
	 */
	public BasicReflectionMember(T element) {
		super();
		this.element = element;
	}

	
	/**
	 * {@inheritDoc}
	 *
	 * <p>getName.</p>
	 */
	@Override
	public abstract String getName() ;

	
	/**
	 * {@inheritDoc}
	 *
	 * <p>getDocumentation.</p>
	 */
	@Override
	public String getDocumentation() {
		return "type some documentation here";
	}

	
	/**
	 * {@inheritDoc}
	 *
	 * <p>getAnnotations.</p>
	 */
	@Override
	public IAnnotationModel[] getAnnotations() {
		Annotation[] annotations = element.getAnnotations();
		IAnnotationModel[] ml=new IAnnotationModel[annotations.length];
		for (int a=0;a<annotations.length;a++){
			ml[a]=new AnnotationModel(annotations[a]);
		}
		return ml;
	}

	
	/** {@inheritDoc} */
	@Override
	public String getAnnotationValue(String annotation) {
		IAnnotationModel[] annotations = getAnnotations();
		for (IAnnotationModel q:annotations){
			if (getAnnotationName(q).equals(annotation)){
				return q.getValue(VALUE);
			}
		}
		return null;
	}


	private String getAnnotationName(IAnnotationModel q) {
		String name = q.getName();
		int ind = name.lastIndexOf('.');
		ind++;
		return name.substring(ind);
	}

	
	/** {@inheritDoc} */
	@Override
	public String[] getAnnotationValues(String annotation) {
		IAnnotationModel[] annotations = getAnnotations();
		for (IAnnotationModel q:annotations){
			if (getAnnotationName(q).equals(annotation)){
				return q.getValues(VALUE);
			}
		}
		return null;
	}

	
	/** {@inheritDoc} */
	@Override
	public boolean hasAnnotation(String name) {
		IAnnotationModel[] annotations = getAnnotations();
		for (IAnnotationModel q:annotations){
			if (getAnnotationName(q).equals(name)){
				return true;
			}
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public IAnnotationModel getAnnotation(String name) {
		IAnnotationModel[] annotations = getAnnotations();
		for (IAnnotationModel m:annotations){
			if (getAnnotationName(m).equals(name)){
				return m;
			}
		}
		return null;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasAnnotationWithCanonicalName(String name) {
		IAnnotationModel[] annotations = getAnnotations();
		for (IAnnotationModel q:annotations){
			if (q.getCanonicalName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public IAnnotationModel getAnnotationByCanonicalName(String name) {
		IAnnotationModel[] annotations = getAnnotations();
		for (IAnnotationModel m:annotations){
			if (m.getCanonicalName().equals(name)){
				return m;
			}
		}
		return null;
	}

	/**
	 * <p>Getter for the field <code>element</code>.</p>
	 *
	 * @return a T object.
	 */
	public T getElement() {
		return element;
	}
	
	
}
