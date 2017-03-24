package org.aml.examples;

import org.raml.issues.resource.ProjectsResourceImpl;

public class MyCustomHandler extends ProjectsResourceImpl{

	@Override
	public GetProjectsResponse getProjects(Integer offset, Integer limit) throws Exception {
		return super.getProjects(offset, limit);
	}
}
