
package org.raml.jaxrs.test.resource;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.raml.jaxrs.test.model.Org;
import org.raml.jaxrs.test.resource.support.ResponseWrapper;

@Path("orgs/{orgId}")
public interface OrgsOrgIdResource {


    /**
     * 
     * @param orgId
     *     
     */
    @Valid
    @GET
    @Produces({
        "application/json"
    })
    OrgsOrgIdResource.GetOrgsByOrgIdResponse getOrgsByOrgId(
        @PathParam("orgId")
        String orgId)
        throws Exception
    ;

    public class GetOrgsByOrgIdResponse
        extends ResponseWrapper
    {


        private GetOrgsByOrgIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static OrgsOrgIdResource.GetOrgsByOrgIdResponse withJsonOK(Org entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new OrgsOrgIdResource.GetOrgsByOrgIdResponse(responseBuilder.build());
        }

    }

}
