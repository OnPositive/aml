package org.aml.registry.usages;

import java.util.HashSet;

public class UsagesInFile {

	protected String fileUrl;
	public UsagesInFile(String fileUrl) {
		super();
		this.fileUrl = fileUrl;
	}
	protected HashSet<String>usagePointers=new HashSet<String>();
}
