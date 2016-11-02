package org.aml.registry.usages;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FileUsages {

	protected LinkedHashMap<String, ArrayList<String>>usage=new LinkedHashMap<>();

	public LinkedHashMap<String, ArrayList<String>> getUsage() {
		return usage;
	}

	public void setUsage(LinkedHashMap<String, ArrayList<String>> usage) {
		this.usage = usage;
	}

	public void append(String url, ArrayList<String> arrayList) {
		this.usage.put(url, arrayList);
	}

	public void append(String url, String path) {
		ArrayList<String> arrayList = this.usage.get(url);
		if (arrayList==null){
			arrayList=new ArrayList<>();
			this.usage.put(url, arrayList);
		}
		arrayList.add(path);
	}
}
