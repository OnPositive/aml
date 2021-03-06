package org.aml.apimodel;

import java.util.ArrayList;
import java.util.List;

public interface MethodBase extends Annotable{
	 /**
     * Parent resource of the Method
     * @return the parent resource or null if there is none
     */
    Resource resource();
    
    /**
     * Some method verbs expect the resource to be sent as a request body. For example, to create a resource, the request must include the details of the resource to create. Resources CAN have alternate representations. For example, an API might support both JSON and XML representations. A method's body is defined in the body property as a hashmap, in which the key MUST be a valid media type.
     **/
    List<MimeType> body();


    /**
     * A method can override the protocols specified in the resource or at the API root, by employing this property.
     **/
    List<String> protocols();



    String description();


    String displayName();
    
    /**
     * An APIs resources MAY be filtered (to return a subset of results) or altered (such as transforming  a response body from JSON to XML format) by the use of query strings. If the resource or its method supports a query string, the query string MUST be defined by the queryParameters property
     **/
    List<? extends INamedParam> queryParameters();


    /**
     * Headers that allowed at this position
     **/
    List<? extends INamedParam> headers();

    
    /**
     * Information about the expected responses to a request
     **/
    List<Response> responses();
	

	boolean hasBody();

	ArrayList<String> getIs();

	String name();
	
	List<SecuredByConfig> securedBy();
}
