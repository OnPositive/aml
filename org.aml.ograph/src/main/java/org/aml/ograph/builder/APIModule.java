package org.aml.ograph.builder;

import java.util.List;

import org.aml.graphmodel.IAPIModule;
import org.aml.graphmodel.IOperation;
import org.aml.graphmodel.IResourceModel;

public class APIModule implements IAPIModule{

	@Override
	public List<IResourceModel> models() {
		return null;
	}

	@Override
	public List<IOperation> globals() {
		return null;
	}

}
