package org.aml.typesystem.java;

import java.util.ArrayList;

import org.aml.typesystem.IAnnotationModel;

public class AcceptAllAnnotationsFromPackages implements IAnnotationFilter{

	protected ArrayList<String>packages=new ArrayList<>();
	
	public AcceptAllAnnotationsFromPackages(ArrayList<String> packages) {
		super();
		this.packages = packages;
	}

	@Override
	public boolean preserve(IAnnotationModel mdl) {
		String pName=mdl.getType().getPackageName();
		return this.packages.contains(pName);
	}

}
