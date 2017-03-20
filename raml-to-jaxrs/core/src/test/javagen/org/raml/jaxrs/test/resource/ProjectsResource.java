
package org.raml.jaxrs.test.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.raml.jaxrs.test.model.Anonimous0;
import org.raml.jaxrs.test.model.Anonimous1;
import org.raml.jaxrs.test.model.Anonimous2;
import org.raml.jaxrs.test.model.Anonimous3;
import org.raml.jaxrs.test.model.Comment;
import org.raml.jaxrs.test.model.Issue;
import org.raml.jaxrs.test.model.Label;
import org.raml.jaxrs.test.model.Project;

@Path("projects")
public interface ProjectsResource {


    /**
     * 
     * @param offset
     *     
     * @param limit
     *     
     */
    @Valid
    @GET
    @Produces({
        "application/json"
    })
    ProjectsResource.GetProjectsResponse getProjects(
        @QueryParam("offset")
        Integer offset,
        @QueryParam("limit")
        Integer limit)
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
    @Produces({
        "application/json"
    })
    ProjectsResource.PostProjectsResponse postProjects(
        @Valid
        Project entity)
        throws Exception
    ;

    /**
     * 
     * @param projectId
     *     
     * @param entity
     *     
     */
    @Valid
    @PUT
    @Path("{projectId}")
    @Consumes("application/json")
    @Produces({
        "application/json"
    })
    ProjectsResource.PutProjectsByProjectIdResponse putProjectsByProjectId(
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Project entity)
        throws Exception
    ;

    /**
     * 
     * @param projectId
     *     
     */
    @Valid
    @GET
    @Path("{projectId}")
    @Produces({
        "application/json"
    })
    ProjectsResource.GetProjectsByProjectIdResponse getProjectsByProjectId(
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    ;

    /**
     * 
     * @param projectId
     *     
     */
    @Valid
    @DELETE
    @Path("{projectId}")
    ProjectsResource.DeleteProjectsByProjectIdResponse deleteProjectsByProjectId(
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    ;

    /**
     * 
     * @param offset
     *     
     * @param limit
     *     
     * @param projectId
     *     
     */
    @Valid
    @GET
    @Path("{projectId}/labels")
    @Produces({
        "application/json"
    })
    ProjectsResource.GetProjectsByProjectIdLabelsResponse getProjectsByProjectIdLabels(
        @PathParam("projectId")
        @NotNull
        int projectId,
        @QueryParam("offset")
        Integer offset,
        @QueryParam("limit")
        Integer limit)
        throws Exception
    ;

    /**
     * 
     * @param projectId
     *     
     * @param entity
     *     
     */
    @Valid
    @POST
    @Path("{projectId}/labels")
    @Consumes("application/json")
    @Produces({
        "application/json"
    })
    ProjectsResource.PostProjectsByProjectIdLabelsResponse postProjectsByProjectIdLabels(
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Label entity)
        throws Exception
    ;

    /**
     * 
     * @param labelId
     *     
     * @param projectId
     *     
     * @param entity
     *     
     */
    @Valid
    @PUT
    @Path("{projectId}/labels/{labelId}")
    @Consumes("application/json")
    @Produces({
        "application/json"
    })
    ProjectsResource.PutProjectsByProjectIdLabelsByLabelIdResponse putProjectsByProjectIdLabelsByLabelId(
        @PathParam("labelId")
        @NotNull
        int labelId,
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Label entity)
        throws Exception
    ;

    /**
     * 
     * @param labelId
     *     
     * @param projectId
     *     
     */
    @Valid
    @GET
    @Path("{projectId}/labels/{labelId}")
    @Produces({
        "application/json"
    })
    ProjectsResource.GetProjectsByProjectIdLabelsByLabelIdResponse getProjectsByProjectIdLabelsByLabelId(
        @PathParam("labelId")
        @NotNull
        int labelId,
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    ;

    /**
     * 
     * @param labelId
     *     
     * @param projectId
     *     
     */
    @Valid
    @DELETE
    @Path("{projectId}/labels/{labelId}")
    ProjectsResource.DeleteProjectsByProjectIdLabelsByLabelIdResponse deleteProjectsByProjectIdLabelsByLabelId(
        @PathParam("labelId")
        @NotNull
        int labelId,
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    ;

    /**
     * 
     * @param offset
     *     
     * @param limit
     *     
     * @param projectId
     *     
     */
    @Valid
    @GET
    @Path("{projectId}/issues")
    @Produces({
        "application/json"
    })
    ProjectsResource.GetProjectsByProjectIdIssuesResponse getProjectsByProjectIdIssues(
        @PathParam("projectId")
        @NotNull
        int projectId,
        @QueryParam("offset")
        Integer offset,
        @QueryParam("limit")
        Integer limit)
        throws Exception
    ;

    /**
     * 
     * @param projectId
     *     
     * @param entity
     *     
     */
    @Valid
    @POST
    @Path("{projectId}/issues")
    @Consumes("application/json")
    @Produces({
        "application/json"
    })
    ProjectsResource.PostProjectsByProjectIdIssuesResponse postProjectsByProjectIdIssues(
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Issue entity)
        throws Exception
    ;

    /**
     * 
     * @param issueId
     *     
     * @param projectId
     *     
     * @param entity
     *     
     */
    @Valid
    @PUT
    @Path("{projectId}/issues/{issueId}")
    @Consumes("application/json")
    @Produces({
        "application/json"
    })
    ProjectsResource.PutProjectsByProjectIdIssuesByIssueIdResponse putProjectsByProjectIdIssuesByIssueId(
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Issue entity)
        throws Exception
    ;

    /**
     * 
     * @param issueId
     *     
     * @param projectId
     *     
     */
    @Valid
    @GET
    @Path("{projectId}/issues/{issueId}")
    @Produces({
        "application/json"
    })
    ProjectsResource.GetProjectsByProjectIdIssuesByIssueIdResponse getProjectsByProjectIdIssuesByIssueId(
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    ;

    /**
     * 
     * @param issueId
     *     
     * @param projectId
     *     
     */
    @Valid
    @DELETE
    @Path("{projectId}/issues/{issueId}")
    ProjectsResource.DeleteProjectsByProjectIdIssuesByIssueIdResponse deleteProjectsByProjectIdIssuesByIssueId(
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    ;

    /**
     * 
     * @param issueId
     *     
     * @param offset
     *     
     * @param limit
     *     
     * @param projectId
     *     
     */
    @Valid
    @GET
    @Path("{projectId}/issues/{issueId}/commments")
    @Produces({
        "application/json"
    })
    ProjectsResource.GetProjectsByProjectIdIssuesByIssueIdCommmentsResponse getProjectsByProjectIdIssuesByIssueIdCommments(
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId,
        @QueryParam("offset")
        Integer offset,
        @QueryParam("limit")
        Integer limit)
        throws Exception
    ;

    /**
     * 
     * @param issueId
     *     
     * @param projectId
     *     
     * @param entity
     *     
     */
    @Valid
    @POST
    @Path("{projectId}/issues/{issueId}/commments")
    @Consumes("application/json")
    @Produces({
        "application/json"
    })
    ProjectsResource.PostProjectsByProjectIdIssuesByIssueIdCommmentsResponse postProjectsByProjectIdIssuesByIssueIdCommments(
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Comment entity)
        throws Exception
    ;

    /**
     * 
     * @param issueId
     *     
     * @param commentId
     *     
     * @param projectId
     *     
     * @param entity
     *     
     */
    @Valid
    @PUT
    @Path("{projectId}/issues/{issueId}/commments/{commentId}")
    @Consumes("application/json")
    @Produces({
        "application/json"
    })
    ProjectsResource.PutProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse putProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId(
        @PathParam("commentId")
        @NotNull
        int commentId,
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Comment entity)
        throws Exception
    ;

    /**
     * 
     * @param issueId
     *     
     * @param commentId
     *     
     * @param projectId
     *     
     */
    @Valid
    @GET
    @Path("{projectId}/issues/{issueId}/commments/{commentId}")
    @Produces({
        "application/json"
    })
    ProjectsResource.GetProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse getProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId(
        @PathParam("commentId")
        @NotNull
        int commentId,
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    ;

    /**
     * 
     * @param issueId
     *     
     * @param commentId
     *     
     * @param projectId
     *     
     */
    @Valid
    @DELETE
    @Path("{projectId}/issues/{issueId}/commments/{commentId}")
    ProjectsResource.DeleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse deleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId(
        @PathParam("commentId")
        @NotNull
        int commentId,
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    ;

    public class DeleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private DeleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         */
        public static ProjectsResource.DeleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse withNoContent() {
            Response.ResponseBuilder responseBuilder = Response.status(204);
            return new ProjectsResource.DeleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse(responseBuilder.build());
        }

    }

    public class DeleteProjectsByProjectIdIssuesByIssueIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private DeleteProjectsByProjectIdIssuesByIssueIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         */
        public static ProjectsResource.DeleteProjectsByProjectIdIssuesByIssueIdResponse withNoContent() {
            Response.ResponseBuilder responseBuilder = Response.status(204);
            return new ProjectsResource.DeleteProjectsByProjectIdIssuesByIssueIdResponse(responseBuilder.build());
        }

    }

    public class DeleteProjectsByProjectIdLabelsByLabelIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private DeleteProjectsByProjectIdLabelsByLabelIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         */
        public static ProjectsResource.DeleteProjectsByProjectIdLabelsByLabelIdResponse withNoContent() {
            Response.ResponseBuilder responseBuilder = Response.status(204);
            return new ProjectsResource.DeleteProjectsByProjectIdLabelsByLabelIdResponse(responseBuilder.build());
        }

    }

    public class DeleteProjectsByProjectIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private DeleteProjectsByProjectIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         */
        public static ProjectsResource.DeleteProjectsByProjectIdResponse withNoContent() {
            Response.ResponseBuilder responseBuilder = Response.status(204);
            return new ProjectsResource.DeleteProjectsByProjectIdResponse(responseBuilder.build());
        }

    }

    public class GetProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private GetProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.GetProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse withJsonOK(Comment entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.GetProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse(responseBuilder.build());
        }

    }

    public class GetProjectsByProjectIdIssuesByIssueIdCommmentsResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private GetProjectsByProjectIdIssuesByIssueIdCommmentsResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.GetProjectsByProjectIdIssuesByIssueIdCommmentsResponse withJsonOK(Anonimous3 entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.GetProjectsByProjectIdIssuesByIssueIdCommmentsResponse(responseBuilder.build());
        }

    }

    public class GetProjectsByProjectIdIssuesByIssueIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private GetProjectsByProjectIdIssuesByIssueIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.GetProjectsByProjectIdIssuesByIssueIdResponse withJsonOK(Issue entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.GetProjectsByProjectIdIssuesByIssueIdResponse(responseBuilder.build());
        }

    }

    public class GetProjectsByProjectIdIssuesResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private GetProjectsByProjectIdIssuesResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.GetProjectsByProjectIdIssuesResponse withJsonOK(Anonimous2 entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.GetProjectsByProjectIdIssuesResponse(responseBuilder.build());
        }

    }

    public class GetProjectsByProjectIdLabelsByLabelIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private GetProjectsByProjectIdLabelsByLabelIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.GetProjectsByProjectIdLabelsByLabelIdResponse withJsonOK(Label entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.GetProjectsByProjectIdLabelsByLabelIdResponse(responseBuilder.build());
        }

    }

    public class GetProjectsByProjectIdLabelsResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private GetProjectsByProjectIdLabelsResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.GetProjectsByProjectIdLabelsResponse withJsonOK(Anonimous1 entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.GetProjectsByProjectIdLabelsResponse(responseBuilder.build());
        }

    }

    public class GetProjectsByProjectIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private GetProjectsByProjectIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.GetProjectsByProjectIdResponse withJsonOK(Project entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.GetProjectsByProjectIdResponse(responseBuilder.build());
        }

    }

    public class GetProjectsResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private GetProjectsResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.GetProjectsResponse withJsonOK(Anonimous0 entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.GetProjectsResponse(responseBuilder.build());
        }

    }

    public class PostProjectsByProjectIdIssuesByIssueIdCommmentsResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private PostProjectsByProjectIdIssuesByIssueIdCommmentsResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.PostProjectsByProjectIdIssuesByIssueIdCommmentsResponse withJsonCreated(Comment entity) {
            Response.ResponseBuilder responseBuilder = Response.status(201).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.PostProjectsByProjectIdIssuesByIssueIdCommmentsResponse(responseBuilder.build());
        }

    }

    public class PostProjectsByProjectIdIssuesResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private PostProjectsByProjectIdIssuesResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.PostProjectsByProjectIdIssuesResponse withJsonCreated(Issue entity) {
            Response.ResponseBuilder responseBuilder = Response.status(201).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.PostProjectsByProjectIdIssuesResponse(responseBuilder.build());
        }

    }

    public class PostProjectsByProjectIdLabelsResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private PostProjectsByProjectIdLabelsResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.PostProjectsByProjectIdLabelsResponse withJsonCreated(Label entity) {
            Response.ResponseBuilder responseBuilder = Response.status(201).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.PostProjectsByProjectIdLabelsResponse(responseBuilder.build());
        }

    }

    public class PostProjectsResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private PostProjectsResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.PostProjectsResponse withJsonCreated(Project entity) {
            Response.ResponseBuilder responseBuilder = Response.status(201).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.PostProjectsResponse(responseBuilder.build());
        }

    }

    public class PutProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private PutProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.PutProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse withJsonNonAuthoritativeInformation(Comment entity) {
            Response.ResponseBuilder responseBuilder = Response.status(203).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.PutProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse(responseBuilder.build());
        }

    }

    public class PutProjectsByProjectIdIssuesByIssueIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private PutProjectsByProjectIdIssuesByIssueIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.PutProjectsByProjectIdIssuesByIssueIdResponse withJsonNonAuthoritativeInformation(Issue entity) {
            Response.ResponseBuilder responseBuilder = Response.status(203).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.PutProjectsByProjectIdIssuesByIssueIdResponse(responseBuilder.build());
        }

    }

    public class PutProjectsByProjectIdLabelsByLabelIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private PutProjectsByProjectIdLabelsByLabelIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.PutProjectsByProjectIdLabelsByLabelIdResponse withJsonNonAuthoritativeInformation(Label entity) {
            Response.ResponseBuilder responseBuilder = Response.status(203).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.PutProjectsByProjectIdLabelsByLabelIdResponse(responseBuilder.build());
        }

    }

    public class PutProjectsByProjectIdResponse
        extends org.raml.jaxrs.test.resource.support.ResponseWrapper
    {


        private PutProjectsByProjectIdResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static ProjectsResource.PutProjectsByProjectIdResponse withJsonNonAuthoritativeInformation(Project entity) {
            Response.ResponseBuilder responseBuilder = Response.status(203).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new ProjectsResource.PutProjectsByProjectIdResponse(responseBuilder.build());
        }

    }

}
