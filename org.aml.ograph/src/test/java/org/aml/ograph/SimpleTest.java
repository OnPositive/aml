package org.aml.ograph;

import org.aml.ograph.builder.GraphBuilder;

import junit.framework.TestCase;

public class SimpleTest extends TestCase {

	
	public void test0(){
		GraphBuilder.build("/crud/api.raml");
	}
}
