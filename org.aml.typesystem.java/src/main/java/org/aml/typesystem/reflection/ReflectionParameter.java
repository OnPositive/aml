package org.aml.typesystem.reflection;

import java.lang.annotation.Annotation;

import org.aml.typesystem.IAnnotationModel;
import org.aml.typesystem.IParameterModel;
import org.aml.typesystem.ITypeModel;

/**
 * <p>ReflectionParameter class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class ReflectionParameter implements IParameterModel{

	private static final String VALUE = "value";
	protected ReflectionType type;
	protected String name;	
	/**
	 * <p>Constructor for ReflectionParameter.</p>
	 *
	 * @param type a {@link org.aml.typesystem.reflection.ReflectionType} object.
	 * @param model an array of {@link org.aml.typesystem.reflection.AnnotationModel} objects.
	 * @param name a {@link java.lang.String} object.
	 */
	public ReflectionParameter(ReflectionType type, AnnotationModel[] model,String name) {
		super();
		this.type = type;
		this.model = model;
		this.name = name;
	}
	
	/**
	 * <p>Constructor for ReflectionParameter.</p>
	 *
	 * @param cl a {@link java.lang.Class} object.
	 * @param annotations an array of {@link java.lang.annotation.Annotation} objects.
	 * @param name a {@link java.lang.String} object.
	 */
	public ReflectionParameter(Class<?> cl, Annotation[] annotations, String name) {
		this.type=new ReflectionType(cl);
		this.name = name;
		model=new AnnotationModel[annotations.length];
		int i=0;
		for (Annotation a:annotations){
			model[i++]=new AnnotationModel(a);
		}
	}
	
	protected AnnotationModel[] model;

	
	/**
	 * {@inheritDoc}
	 *
	 * <p>getDocumentation.</p>
	 */
	@Override
	public String getDocumentation() {
		return "";
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

	
	/** {@inheritDoc} */
	@Override
	public String[] getAnnotationValues(String annotation) {
		IAnnotationModel[] annotations = getAnnotations();
		for (IAnnotationModel q:annotations){
			if (getAnnotationName(q).equals(annotation)){
				return q.getValues(annotation);
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

	private String getAnnotationName(IAnnotationModel q) {
		String n = q.getName();
		int ind = n.lastIndexOf('.');
		ind++;
		return n.substring(ind);
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
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>getName.</p>
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>Getter for the field <code>type</code>.</p>
	 */
	@Override
	public String getParameterType() {
		String name = type.getName();		
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>required.</p>
	 */
	@Override
	public boolean required() {
		return type.element.isPrimitive();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>getAnnotations.</p>
	 */
	@Override
	public IAnnotationModel[] getAnnotations() {
		return model;
	}

	@Override
	public ITypeModel getType() {
		return this.type;
	}
}
