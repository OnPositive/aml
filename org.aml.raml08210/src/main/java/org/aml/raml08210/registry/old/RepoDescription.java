package org.aml.raml08210.registry.old;

import java.util.ArrayList;

public class RepoDescription {

	protected String name;
	
	protected String activeBranch;
	
	protected ArrayList<RAMLFile>apis=new ArrayList<>();
	
	public void update(OldApi api){
		for (RAMLFile f : api.getRAMLFiles()) {
			String location=f.getLocation();
			location=location.substring("https://raw.githubusercontent.com/raml-apis".length()+1);
			String name=location.substring(0, location.indexOf('/'));
			this.name=name;
			location=location.substring(location.indexOf('/')+1);
			String branch=location.substring(0, location.indexOf('/'));
			if(activeBranch==null){
				activeBranch=branch;
			}
			else{
				if (branch.equals("production")&&!activeBranch.equals("raml1.0")){
					activeBranch=branch;
				}
				if (branch.equals("raml1.0")){
					activeBranch=branch;
				}
			}			
		}
		for (RAMLFile f : api.getRAMLFiles()) {
			String location=f.getLocation();
			location=location.substring("https://raw.githubusercontent.com/raml-apis".length()+1);
			name=location.substring(0, location.indexOf('/'));
			location=location.substring(location.indexOf('/')+1);
			String branch=location.substring(0, location.indexOf('/'));
			if (branch.equals(activeBranch)){
				String innerLocation=location.substring(location.indexOf('/')+1);
				f.setLocation(innerLocation);
				apis.add(f);
			}
		}
	}
}
