package org.aml.registry.operations;

import java.util.HashSet;
import java.util.function.Function;

import org.aml.registry.model.ApiDescription;
import org.aml.registry.model.Registry;
import org.aml.registry.model.RegistryStat;

public class BuildStat implements Function<Registry	, RegistryStat>{

	public RegistryStat apply(Registry t) {
		HashSet<String>names=new HashSet<String>();
		for (ApiDescription d:t.getApis()){
			String name = d.getName();
			names.add(name);
		}
		RegistryStat rs=new RegistryStat();
		rs.setUniqueSpecCount(t.getApis().size()+t.getLibraries().size());
		rs.setApisCount(names.size());		
		return rs;
	}

}
