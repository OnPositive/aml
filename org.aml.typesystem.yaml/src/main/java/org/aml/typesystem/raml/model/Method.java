package org.aml.typesystem.raml.model;

import java.util.List;

import javax.annotation.Nullable;

import org.aml.typesystem.AbstractType;
import org.raml.v2.api.model.v10.methods.TraitRef;
import org.raml.v2.api.model.v10.security.SecuritySchemeRef;

public interface Method extends Annotable{

	/**
     * Method that can be called
     **/
    String method();

    /**
     * Parent resource of the Method
     * @return the parent resource or null if there is none
     */
    @Nullable
    Resource resource();
    
    /**
     * Some method verbs expect the resource to be sent as a request body. For example, to create a resource, the request must include the details of the resource to create. Resources CAN have alternate representations. For example, an API might support both JSON and XML representations. A method's body is defined in the body property as a hashmap, in which the key MUST be a valid media type.
     **/
    List<AbstractType> body();


    /**
     * A method can override the protocols specified in the resource or at the API root, by employing this property.
     **/
    List<String> protocols();


    /**
     * Instantiation of applyed traits
     **/
    List<TraitRef> is();


    /**
     * securityScheme may also be applied to a resource by using the securedBy key, which is equivalent to applying the securityScheme to all methods that may be declared, explicitly or implicitly, by defining the resourceTypes or traits property for that resource. To indicate that the method may be called without applying any securityScheme, the method may be annotated with the null securityScheme.
     **/
    List<SecuritySchemeRef> securedBy();


    String description();


    String displayName();
    
    /**
     * An APIs resources MAY be filtered (to return a subset of results) or altered (such as transforming  a response body from JSON to XML format) by the use of query strings. If the resource or its method supports a query string, the query string MUST be defined by the queryParameters property
     **/
    List<AbstractType> queryParameters();


    /**
     * Headers that allowed at this position
     **/
    List<AbstractType> headers();


    /**
     * Specifies the query string needed by this method. Mutually exclusive with queryParameters.
     **/
    AbstractType queryString();


    /**
     * Information about the expected responses to a request
     **/
    List<Response> responses();
}