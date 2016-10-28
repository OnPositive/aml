package org.aml.apimodel;

import java.util.ArrayList;
import java.util.List;

public class ResourceCollector {

	
	public static List<Resource> collect(IHasResources r){
		ArrayList<Resource>result=new ArrayList<>();
		innerCollect(r, result); 
		return result;		
	}
	
	static void innerCollect(IHasResources r,List<Resource>result){		
		r.resources().forEach(x->{result.add(x);innerCollect(x, result);});		
	}

	public static List<Action> collectActions(IHasResources iHasResources) {
		ArrayList<Action>results=new ArrayList<Action>();
		List<Resource> collect = collect(iHasResources);
		for (Resource r:collect){
			results.addAll(r.methods());
		}
		return results;
	}
}
