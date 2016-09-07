package org.aml.typesystem.beans;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.aml.typesystem.AbstractType;

/**
 * <p>DeclarationsBase class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class DeclarationsBase extends TypesCollection {

	protected LinkedHashMap<String, DeclarationsBase> uses = new LinkedHashMap<>();

	protected LinkedHashMap<String, AbstractType> annotationTypes = new LinkedHashMap<>();

	
	/**
	 * <p>addAnnotationType.</p>
	 *
	 * @param tp a {@link org.aml.typesystem.AbstractType} object.
	 */
	public void addAnnotationType(AbstractType tp){
		this.annotationTypes.put(tp.name(), tp);
	}
	
	/**
	 * <p>registerUses.</p>
	 *
	 * @param prefix a {@link java.lang.String} object.
	 * @param base a {@link org.aml.typesystem.beans.DeclarationsBase} object.
	 */
	public void registerUses(String prefix,DeclarationsBase base){
		uses.put(prefix, base);
	}
	
	/**
	 * <p>getLibrary.</p>
	 *
	 * @param prefix a {@link java.lang.String} object.
	 * @return a {@link org.aml.typesystem.beans.DeclarationsBase} object.
	 */
	public DeclarationsBase getLibrary(String prefix) {
		return uses.get(prefix);
	}

	/**
	 * <p>getAnnotationType.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public AbstractType getAnnotationType(String name) {
		return annotationTypes.get(name);
	}

	/**
	 * <p>Getter for the field <code>annotationTypes</code>.</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<AbstractType> getAnnotationTypes() {
		return annotationTypes.values();
	}
}
