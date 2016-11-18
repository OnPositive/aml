package org.aml.apimodel;

import java.util.ArrayList;
import java.util.List;


public interface Action extends Annotable,IHasBody{

	/**
     * Method that can be called
     **/
    String method();

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

    /**
     * 
     * @return description of this method
     */
    String description();


    /**
     * 
     * @return display name associated with this method
     */
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
	
	
	/**
	 * 
	 * @return security schemes configuration associated with this method
	 */
	ArrayList<SecuredByConfig>securedBy();
	
	default INamedParam header(String name){
		return this.headers().stream().filter(x -> x.getKey().equals(name)).findFirst().orElse(null);
	}
	default INamedParam queryParam(String name){
		return this.queryParameters().stream().filter(x -> x.getKey().equals(name)).findFirst().orElse(null);
	}
	default Response response(String name){
		return this.responses().stream().filter(x -> x.code().equals(name)).findFirst().orElse(null);
	}
	
	default List<INamedParam> parameters(){
		ArrayList<INamedParam>results=new ArrayList<>();
		results.addAll(queryParameters());
		results.addAll(headers());
		return results;
	}
}