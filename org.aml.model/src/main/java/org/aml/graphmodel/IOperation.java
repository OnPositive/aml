package org.aml.graphmodel;

import java.util.List;

import org.aml.apimodel.Action;

public interface IOperation {
	
	String name();
	
	Action httpMethod();
	
	SemanticContext context();

	boolean isSafe();
	
	List<Value> parameters();
	
	Value result();
}