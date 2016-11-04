package org.aml.raml08210.registry.old;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.aml.registry.model.ApiDescription;
import org.aml.registry.model.Registry;

public class RepoRegistry {

	protected HashMap<String, RepoDescription>map=new HashMap<>();
	
	public void append(OldApi api){
		RepoDescription ds=new RepoDescription();
		ds.update(api);
		map.put(ds.name, ds);
	}
	
	public void cloneRepos(File targetFile) throws IOException{
		for (RepoDescription d:map.values()){
			File apiDir=targetFile;
			String url="https://github.com/raml-apis/"+d.name+"/archive/"+d.activeBranch+".zip";
			System.out.println("Extracting:"+d.name);
			ExtractZipFileWithSubdirectories.extractZipFromUrl(url, apiDir);
		}
	}

	public Registry toNewRegistry() {
		Registry reg=new Registry();
		for (RepoDescription d:map.values()){
			for (RAMLFile f:d.apis){
				ApiDescription e = new ApiDescription();
				e.setOrg(d.name);
				e.setLocation("https://github.com/raml-apis/"+d.activeBranch+"/"+f.getLocation());
				e.setName(f.name);
				e.setVersion("raml-org-"+d.activeBranch);
				e.setIcon(f.logo);
				reg.getApis().add(e);
				
			}
		}
		return reg;		
	}
}
