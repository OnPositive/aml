package org.aml.graphmodel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.aml.typesystem.IType;

/**
 * This interfaces describes resource having different possible representations
 * as well as methods
 * 
 * @author Pavel
 *
 */
public interface IResourceModel {

	/**
	 * 
	 * @return name of resource
	 */
	String name();

	/***
	 * 
	 * @return primary shape of resource usually it represents version of
	 *         resource associated with read operation (but it is not necessary)
	 */
	IType primaryShape();

	/**
	 * 
	 * @param context
	 * @return how this resource model should be represented in a particular
	 *         context
	 */
	IType shape(SemanticContext context);


	/**
	 * 
	 * @return full list of operations that accept or return representation of this type
	 */
	List<IOperation>allRelatedOperations();
	
	
	default Optional<IOperation> readOperation(SemanticContext context){
		return allWithContex(SemanticContext.DETAILS).findFirst();
	}
	default Optional<IOperation> updateOperation(SemanticContext context){
		return allWithContex(SemanticContext.UPDATE).findFirst();
	}
	
	default Optional<IOperation> createOperation(SemanticContext context){
		return allWithContex(SemanticContext.CREATE).findFirst();
	}
	
	default Optional<IOperation> deleteOperation(SemanticContext context){
		return allWithContex(SemanticContext.DELETE).findFirst();
	}
	
	
	default Stream<IOperation> allWithContex(SemanticContext context){
		return allRelatedOperations().stream().filter(x->context.equals(x.context()));
	}	
	
}