
package org.raml.jaxrs.test.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.raml.jaxrs.test.model.Anonimous0;
import org.raml.jaxrs.test.model.Person;

@Path("q")
public interface QQResource {


    /**
     * get some stuff
     * 
     * @param a
     *     
     * @param b
     *     
     * @param c
     *     
     */
    @Valid
    @GET
    @Produces({
        "application/json"
    })
    QQResource.GetQResponse getQ(
        @QueryParam("a")
        @NotNull
        String a,
        @QueryParam("b")
        @NotNull
        String b,
        @QueryParam("c")
        String c)
        throws Exception
    ;

    /**
     * 
     * @param entity
     *     
     */
    @Valid
    @POST
    @Consumes("application/json")
    QQResource.PostQResponse postQ(
        @Valid
        Person entity)
        throws Exception
    ;

    public class GetQResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private GetQResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static QQResource.GetQResponse withJsonOK(Anonimous0 entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new QQResource.GetQResponse(responseBuilder.build());
        }

    }

    public class PostQResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private PostQResponse(Response delegate) {
            super(delegate);
        }

    }

}
