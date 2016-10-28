package org.aml.apimodel;

import java.util.List;
import java.util.Optional;

public interface IHasResources {

	List<Resource> resources();
	
	public default Optional<Resource> getResourceOpt(String relativeUri) {
		final Optional<Resource> findFirst = this.resources().stream().filter(x -> x.relativeUri().equals(relativeUri)).findFirst();
		if (findFirst.isPresent()){
			return findFirst;
		}
		int p=relativeUri.indexOf('/',1);
		if (p!=-1){
			Resource r=getResource(relativeUri.substring(0,p));
			if (r!=null){
				return r.getResourceOpt(relativeUri.substring(p));
			}
		}
		return findFirst;
	}
	
	public default Resource getResource(String relativeUri) {
		final Optional<Resource> resourceOpt = getResourceOpt(relativeUri);
		if (resourceOpt.isPresent()){
			return resourceOpt.get();
		}
		return null;
	}
	
	public default List<Resource> allResources(){
		return ResourceCollector.collect(this);
	}
	
	public default List<Action> allMethods(){
		return ResourceCollector.collectActions(this);
	}
}
