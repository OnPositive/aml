package org.aml.apimodel;

import java.util.List;


public interface Response extends Annotable{
	String code();

	/**
	 * Detailed information about any response headers returned by this method
	 **/
	List<AbstractParam> headers();

	/**
	 * The body of the response: a body declaration
	 **/
	List<MimeType> body();

	/**
	 * A longer, human-friendly description of the response
	 **/
	// --def-system-mod--
	String description();

	boolean hasBody();

}
