package org.aml.typesystem.raml.model;

import java.util.List;

import org.aml.typesystem.AbstractType;

public interface Response extends Annotable{
	String code();

	/**
	 * Detailed information about any response headers returned by this method
	 **/
	List<AbstractType> headers();

	/**
	 * The body of the response: a body declaration
	 **/
	List<AbstractType> body();

	/**
	 * A longer, human-friendly description of the response
	 **/
	// --def-system-mod--
	String description();

}
