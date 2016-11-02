package org.aml.registry.usages;

import java.util.LinkedHashMap;

public class EntryUsages {

	protected LinkedHashMap<String, FileUsages> usages = new LinkedHashMap<>();
	
	

	public LinkedHashMap<String, FileUsages> getUsages() {
		return usages;
	}

	public void setUsages(LinkedHashMap<String, FileUsages> usages) {
		this.usages = usages;
	}

	public void append(String usageCode, String url, String path) {
		FileUsages fileUsages = this.usages.get(usageCode);
		if (fileUsages==null){
			fileUsages=new FileUsages();
			this.usages.put(usageCode, fileUsages);
		}
		fileUsages.append(url, path);
	}
}
