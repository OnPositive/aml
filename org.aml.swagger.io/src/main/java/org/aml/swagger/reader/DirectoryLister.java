package org.aml.swagger.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.aml.apimodel.Api;
import org.aml.apimodel.impl.ApiImpl;
import org.apache.commons.io.FileUtils;

public class DirectoryLister {

	
	protected ArrayList<Api>mdl=new ArrayList<>();
	
	
	protected void process(File root){
		for (File f:root.listFiles()){
			if (f.isDirectory()){
				process(f);
			}
			if (f.getName().endsWith(".json")){
				try {
					String readFileToString = FileUtils.readFileToString(f);
					if (readFileToString.contains("swagger")&&readFileToString.contains("paths")){
						ApiImpl read = new SwaggerReader().read(readFileToString);
						if (read==null){
							System.out.println("Can not parse swagger:"+f.getAbsolutePath());
						}
						mdl.add(read);
					}
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			}
		}
		
	}
	
	public ArrayList<Api> parse(File root){
		this.mdl.clear();
		process(root);
		return new ArrayList<>(this.mdl);
	}
}
