package org.aml.registry.operations;

import java.util.function.Function;

import org.aml.registry.model.Registry;
import org.aml.registry.model.SubRegistryDescription;

public class ResolveRegistry implements Function<Registry, Registry> {

	public static final ResolveRegistry INSTANCE = new ResolveRegistry();

	public Registry apply(Registry t) {
		Registry result = new Registry();
		result.getApis().addAll(t.getApis());
		result.getLibraries().addAll(t.getLibraries());
		result.setName(t.getName());
		for (SubRegistryDescription d : t.getIncludes()) {
			Registry sub=INSTANCE.apply(new LoadRegistry(d.getLocation()).get());
			result.getApis().addAll(sub.getApis());
			result.getTools().addAll(sub.getTools());
			result.getLibraries().addAll(sub.getLibraries());
			
		}		
		return result;
	}

}
