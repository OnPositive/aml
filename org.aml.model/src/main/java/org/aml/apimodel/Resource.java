package org.aml.apimodel;

import java.util.List;

public interface Resource extends Annotable{

    /**
     * Relative URL of this resource from the parent resource
     **/
    String relativeUri();


    /**
     * The displayName attribute specifies the resource display name. It is a friendly name used only for  display or documentation purposes. If displayName is not specified, it defaults to the element's key (the name of the property itself).
     **/
    String displayName();


    /**
     * A nested resource is identified as any property whose name begins with a slash ("&#47;") and is therefore treated as a relative URI.
     **/
    List<Resource> resources();


    
    Resource parentResource();
    
    /**
     * Methods that are part of this resource type definition
     **/
    List<Action> methods();


    String description();


    /**
     * Detailed information about any URI parameters of this resource
     **/
    List<? extends INamedParam> uriParameters();


	String getUri();


	Api getApi();


	

}