package org.aml.typesystem.beans;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.aml.typesystem.AbstractType;

public class DeclarationsBase extends TypesCollection {

	protected LinkedHashMap<String, DeclarationsBase> uses = new LinkedHashMap<>();

	protected LinkedHashMap<String, AbstractType> annotationTypes = new LinkedHashMap<>();

	
	public void addAnnotationType(AbstractType tp){
		this.annotationTypes.put(tp.name(), tp);
	}
	
	public void registerUses(String prefix,DeclarationsBase base){
		uses.put(prefix, base);
	}
	
	public DeclarationsBase getLibrary(String prefix) {
		return uses.get(prefix);
	}

	public AbstractType getAnnotationType(String name) {
		return annotationTypes.get(name);
	}

	public Collection<AbstractType> getAnnotationTypes() {
		return annotationTypes.values();
	}
}
