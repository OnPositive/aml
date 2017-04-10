package org.aml.graphmodel;

import java.util.List;

public interface IAPIModule {

	List<IResourceModel> models();
	
	List<IOperation> globals();
}
