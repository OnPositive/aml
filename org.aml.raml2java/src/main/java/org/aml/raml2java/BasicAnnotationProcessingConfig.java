package org.aml.raml2java;

import java.util.HashSet;

import org.aml.typesystem.AbstractType;

public class BasicAnnotationProcessingConfig implements IAnnotationProcessingConfig{

	protected HashSet<String>namespacesToSkipDefinition=new HashSet<>();
	protected HashSet<String>namespacesToSkipReference=new HashSet<>();
	
	
	protected HashSet<String>idsToSkipDefinition=new HashSet<>();
	protected HashSet<String>idsToSkipReference=new HashSet<>();
	protected boolean skipAllDefinitions;
	protected boolean skipAllReferences;
	
	public boolean isSkipAllReferences() {
		return skipAllReferences;
	}
	public void setSkipAllReferences(boolean skipAllReferences) {
		this.skipAllReferences = skipAllReferences;
	}
	public boolean isSkipAllDefinitions() {
		return skipAllDefinitions;
	}
	public void setSkipAllDefinitions(boolean skipAllDefinitions) {
		this.skipAllDefinitions = skipAllDefinitions;
	}
	public void addNamespaceToSkipReference(String namespace){
		namespacesToSkipReference.add(namespace);
	}
	public void addNamespaceToSkipDefinition(String namespace){
		namespacesToSkipDefinition.add(namespace);
	}
	
	public void addIdToSkipReference(String namespace){
		idsToSkipReference.add(namespace);
	}
	public void addIdToSkipDefinition(String namespace){
		idsToSkipDefinition.add(namespace);
	}
	
	@Override
	public boolean skipDefinition(AbstractType t) {
		if (skipAllDefinitions){
			return true;
		}
		String nm=t.getNameSpaceId();
		if (namespacesToSkipDefinition.contains(nm)){
			return true;
		}
		String id=nm+"."+t.name();
		if (idsToSkipDefinition.contains(id)){
			return true;
		}
		return false;
	}

	@Override
	public boolean skipReference(AbstractType t) {
		if (skipAllReferences){
			return true;
		}
		String nm=t.getNameSpaceId();
		if (namespacesToSkipReference.contains(nm)){
			return true;
		}
		String id=nm+"."+t.name();
		if (idsToSkipReference.contains(id)){
			return true;
		}
		return false;
	}

}
