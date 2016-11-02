package org.aml.registry.usages;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class UsageIndex {

	protected LinkedHashMap<String, EntryUsages> usages = new LinkedHashMap<>();
	protected LinkedHashMap<String,String>fileToNum=new LinkedHashMap<>();
	
	public LinkedHashMap<String, String> getFileToNum() {
		return fileToNum;
	}

	public void setFileToNum(LinkedHashMap<String, String> fileToNum) {
		this.fileToNum = fileToNum;
	}

	public LinkedHashMap<String, EntryUsages> getUsages() {
		return usages;
	}

	public void setUsages(LinkedHashMap<String, EntryUsages> usages) {
		this.usages = usages;
	}

	public void append(String url, LinkedHashMap<String, ArrayList<String>> results) {
		for (String k : results.keySet()) {
			EntryUsages fileUsages = usages.get(renum(k));
			if (fileUsages == null) {
				fileUsages = new EntryUsages();
				usages.put(renum(k), fileUsages);
			}

			ArrayList<String> arrayList = results.get(k);
			
			for (String s : arrayList) {
				String[] split = s.split(";");
				String usageCode = split[split.length - 2] + split[split.length - 1];
				StringBuilder usageInFile=new StringBuilder();
				for (int i=0;i<split.length-2;i++){
					usageInFile.append(';');
					usageInFile.append(split[i]);
				}
				String path=usageInFile.toString();
				fileUsages.append(usageCode,renum(url),path);
			}
		}
	}

	private String renum(String k) {
		if (fileToNum.containsKey(k)){
			return fileToNum.get(k);
		}
		String value = ""+fileToNum.size();
		fileToNum.put(k, value);
		return value;
	}
}
