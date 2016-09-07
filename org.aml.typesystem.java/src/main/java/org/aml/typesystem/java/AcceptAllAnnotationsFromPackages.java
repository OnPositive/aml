package org.aml.typesystem.java;

import java.util.ArrayList;

import org.aml.typesystem.IAnnotationModel;

/**
 * <p>AcceptAllAnnotationsFromPackages class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class AcceptAllAnnotationsFromPackages implements IAnnotationFilter{

	protected ArrayList<String>packages=new ArrayList<>();
	
	/**
	 * <p>Constructor for AcceptAllAnnotationsFromPackages.</p>
	 *
	 * @param packages a {@link java.util.ArrayList} object.
	 */
	public AcceptAllAnnotationsFromPackages(ArrayList<String> packages) {
		super();
		this.packages = packages;
	}

	/** {@inheritDoc} */
	@Override
	public boolean preserve(IAnnotationModel mdl) {
		String pName=mdl.getType().getPackageName();
		return this.packages.contains(pName);
	}

}
