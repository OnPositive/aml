package org.aml.typesystem;

/**
 * <p>IMember interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IMember extends IBasicModel{

	/**
	 * <p>isStatic.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isStatic();
	/**
	 * <p>isPublic.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isPublic();
	
	/**
	 * <p>getType.</p>
	 *
	 * @return a {@link org.aml.typesystem.ITypeModel} object.
	 */
	ITypeModel getType();
	
	/**
	 * <p>getJAXBType.</p>
	 *
	 * @return a {@link org.aml.typesystem.ITypeModel} object.
	 */
	ITypeModel getCollectionMemberType();
	/**
	 * <p>getJavaType.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	public abstract Class<?> getJavaType();
	
	
	/**
	 * <p>isCollection.</p>
	 *
	 * @return whether the model type is collection
	 */
	boolean isCollection();
	
	
	/**
	 * <p>isMap.</p>
	 *
	 * @return whether the model type is map
	 */
	boolean isMap();
	
	/**
	 * <p>defaultValue.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	Object defaultValue();
}
