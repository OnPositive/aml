
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
import org.aml.persistance.ResourceWithPersitanceManager;
import org.raml.jaxrs.test.model.Anonimous0;
import org.raml.jaxrs.test.model.Anonimous1;
import org.raml.jaxrs.test.model.Anonimous2;
import org.raml.jaxrs.test.model.Anonimous3;
import org.raml.jaxrs.test.model.Comment;
import org.raml.jaxrs.test.model.Issue;
import org.raml.jaxrs.test.model.Label;
import org.raml.jaxrs.test.model.Project;

@Path("projects")
public class ProjectsResourceImpl
    extends ResourceWithPersitanceManager
    implements ProjectsResource
{

    private final static String getProjects_meta = "[{\"basicPaging\":{\"total\":\"total\",\"offset\":\"offset\",\"limit\":\"limit\",\"results\":\"items\"},\"name\":\"\"},{\"name\":\"offset\"},{\"name\":\"limit\"}]";
    private final static String postProjects_meta = "[{\"name\":\"\"},{\"name\":\"body\"}]";
    private final static String putProjectsByProjectId_meta = "[{\"name\":\"\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"},{\"name\":\"body\"}]";
    private final static String getProjectsByProjectId_meta = "[{\"name\":\"\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"}]";
    private final static String deleteProjectsByProjectId_meta = "[{\"name\":\"\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"}]";
    private final static String getProjectsByProjectIdLabels_meta = "[{\"basicPaging\":{\"total\":\"total\",\"offset\":\"offset\",\"limit\":\"limit\",\"results\":\"items\"},\"name\":\"\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"},{\"name\":\"offset\"},{\"name\":\"limit\"}]";
    private final static String postProjectsByProjectIdLabels_meta = "[{\"name\":\"\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"},{\"name\":\"body\"}]";
    private final static String putProjectsByProjectIdLabelsByLabelId_meta = "[{\"name\":\"\"},{\"reference\":\"Label.id\",\"name\":\"labelId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"},{\"name\":\"body\"}]";
    private final static String getProjectsByProjectIdLabelsByLabelId_meta = "[{\"name\":\"\"},{\"reference\":\"Label.id\",\"name\":\"labelId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"}]";
    private final static String deleteProjectsByProjectIdLabelsByLabelId_meta = "[{\"name\":\"\"},{\"reference\":\"Label.id\",\"name\":\"labelId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"}]";
    private final static String getProjectsByProjectIdIssues_meta = "[{\"basicPaging\":{\"total\":\"total\",\"offset\":\"offset\",\"limit\":\"limit\",\"results\":\"items\"},\"name\":\"\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"},{\"name\":\"offset\"},{\"name\":\"limit\"}]";
    private final static String postProjectsByProjectIdIssues_meta = "[{\"name\":\"\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"},{\"name\":\"body\"}]";
    private final static String putProjectsByProjectIdIssuesByIssueId_meta = "[{\"name\":\"\"},{\"reference\":\"Issue.id\",\"name\":\"issueId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"},{\"name\":\"body\"}]";
    private final static String getProjectsByProjectIdIssuesByIssueId_meta = "[{\"name\":\"\"},{\"reference\":\"Issue.id\",\"name\":\"issueId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"}]";
    private final static String deleteProjectsByProjectIdIssuesByIssueId_meta = "[{\"name\":\"\"},{\"reference\":\"Issue.id\",\"name\":\"issueId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"}]";
    private final static String getProjectsByProjectIdIssuesByIssueIdCommments_meta = "[{\"basicPaging\":{\"total\":\"total\",\"offset\":\"offset\",\"limit\":\"limit\",\"results\":\"items\"},\"name\":\"\"},{\"reference\":\"Issue.id\",\"name\":\"issueId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"},{\"name\":\"offset\"},{\"name\":\"limit\"}]";
    private final static String postProjectsByProjectIdIssuesByIssueIdCommments_meta = "[{\"name\":\"\"},{\"reference\":\"Issue.id\",\"name\":\"issueId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"},{\"name\":\"body\"}]";
    private final static String putProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId_meta = "[{\"name\":\"\"},{\"reference\":\"Comment.id\",\"name\":\"commentId\"},{\"reference\":\"Issue.id\",\"name\":\"issueId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"},{\"name\":\"body\"}]";
    private final static String getProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId_meta = "[{\"name\":\"\"},{\"reference\":\"Comment.id\",\"name\":\"commentId\"},{\"reference\":\"Issue.id\",\"name\":\"issueId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"}]";
    private final static String deleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId_meta = "[{\"name\":\"\"},{\"reference\":\"Comment.id\",\"name\":\"commentId\"},{\"reference\":\"Issue.id\",\"name\":\"issueId\"},{\"reference\":\"Project.id\",\"name\":\"projectId\"}]";

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
    public GetProjectsResponse getProjects(
        @QueryParam("offset")
        Integer offset,
        @QueryParam("limit")
        Integer limit)
        throws Exception
    {
        Anonimous0 result = manager.list(Anonimous0 .class, getProjects_meta, offset, limit);
        return GetProjectsResponse.withJsonOK(result);
    }

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
    public PostProjectsResponse postProjects(
        @Valid
        Project entity)
        throws Exception
    {
        Project result = manager.create(Project.class, postProjects_meta, entity);
        return PostProjectsResponse.withJsonCreated(result);
    }

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
    public PutProjectsByProjectIdResponse putProjectsByProjectId(
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Project entity)
        throws Exception
    {
        Project result = manager.update(Project.class, putProjectsByProjectId_meta, projectId, entity);
        return PutProjectsByProjectIdResponse.withJsonNonAuthoritativeInformation(result);
    }

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
    public GetProjectsByProjectIdResponse getProjectsByProjectId(
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    {
        Project result = manager.get(Project.class, getProjectsByProjectId_meta, projectId);
        return GetProjectsByProjectIdResponse.withJsonOK(result);
    }

    /**
     * 
     * @param projectId
     *     
     */
    @Valid
    @DELETE
    @Path("{projectId}")
    public DeleteProjectsByProjectIdResponse deleteProjectsByProjectId(
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    {
        Project result = manager.delete(Project.class, deleteProjectsByProjectId_meta, projectId);
        return DeleteProjectsByProjectIdResponse.withNoContent();
    }

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
    public GetProjectsByProjectIdLabelsResponse getProjectsByProjectIdLabels(
        @PathParam("projectId")
        @NotNull
        int projectId,
        @QueryParam("offset")
        Integer offset,
        @QueryParam("limit")
        Integer limit)
        throws Exception
    {
        Anonimous1 result = manager.list(Anonimous1 .class, getProjectsByProjectIdLabels_meta, projectId, offset, limit);
        return GetProjectsByProjectIdLabelsResponse.withJsonOK(result);
    }

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
    public PostProjectsByProjectIdLabelsResponse postProjectsByProjectIdLabels(
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Label entity)
        throws Exception
    {
        Label result = manager.create(Label.class, postProjectsByProjectIdLabels_meta, projectId, entity);
        return PostProjectsByProjectIdLabelsResponse.withJsonCreated(result);
    }

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
    public PutProjectsByProjectIdLabelsByLabelIdResponse putProjectsByProjectIdLabelsByLabelId(
        @PathParam("labelId")
        @NotNull
        int labelId,
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Label entity)
        throws Exception
    {
        Label result = manager.update(Label.class, putProjectsByProjectIdLabelsByLabelId_meta, labelId, projectId, entity);
        return PutProjectsByProjectIdLabelsByLabelIdResponse.withJsonNonAuthoritativeInformation(result);
    }

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
    public GetProjectsByProjectIdLabelsByLabelIdResponse getProjectsByProjectIdLabelsByLabelId(
        @PathParam("labelId")
        @NotNull
        int labelId,
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    {
        Label result = manager.get(Label.class, getProjectsByProjectIdLabelsByLabelId_meta, labelId, projectId);
        return GetProjectsByProjectIdLabelsByLabelIdResponse.withJsonOK(result);
    }

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
    public DeleteProjectsByProjectIdLabelsByLabelIdResponse deleteProjectsByProjectIdLabelsByLabelId(
        @PathParam("labelId")
        @NotNull
        int labelId,
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    {
        Label result = manager.delete(Label.class, deleteProjectsByProjectIdLabelsByLabelId_meta, labelId, projectId);
        return DeleteProjectsByProjectIdLabelsByLabelIdResponse.withNoContent();
    }

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
    public GetProjectsByProjectIdIssuesResponse getProjectsByProjectIdIssues(
        @PathParam("projectId")
        @NotNull
        int projectId,
        @QueryParam("offset")
        Integer offset,
        @QueryParam("limit")
        Integer limit)
        throws Exception
    {
        Anonimous2 result = manager.list(Anonimous2 .class, getProjectsByProjectIdIssues_meta, projectId, offset, limit);
        return GetProjectsByProjectIdIssuesResponse.withJsonOK(result);
    }

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
    public PostProjectsByProjectIdIssuesResponse postProjectsByProjectIdIssues(
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Issue entity)
        throws Exception
    {
        Issue result = manager.create(Issue.class, postProjectsByProjectIdIssues_meta, projectId, entity);
        return PostProjectsByProjectIdIssuesResponse.withJsonCreated(result);
    }

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
    public PutProjectsByProjectIdIssuesByIssueIdResponse putProjectsByProjectIdIssuesByIssueId(
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Issue entity)
        throws Exception
    {
        Issue result = manager.update(Issue.class, putProjectsByProjectIdIssuesByIssueId_meta, issueId, projectId, entity);
        return PutProjectsByProjectIdIssuesByIssueIdResponse.withJsonNonAuthoritativeInformation(result);
    }

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
    public GetProjectsByProjectIdIssuesByIssueIdResponse getProjectsByProjectIdIssuesByIssueId(
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    {
        Issue result = manager.get(Issue.class, getProjectsByProjectIdIssuesByIssueId_meta, issueId, projectId);
        return GetProjectsByProjectIdIssuesByIssueIdResponse.withJsonOK(result);
    }

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
    public DeleteProjectsByProjectIdIssuesByIssueIdResponse deleteProjectsByProjectIdIssuesByIssueId(
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId)
        throws Exception
    {
        Issue result = manager.delete(Issue.class, deleteProjectsByProjectIdIssuesByIssueId_meta, issueId, projectId);
        return DeleteProjectsByProjectIdIssuesByIssueIdResponse.withNoContent();
    }

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
    public GetProjectsByProjectIdIssuesByIssueIdCommmentsResponse getProjectsByProjectIdIssuesByIssueIdCommments(
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
    {
        Anonimous3 result = manager.list(Anonimous3 .class, getProjectsByProjectIdIssuesByIssueIdCommments_meta, issueId, projectId, offset, limit);
        return GetProjectsByProjectIdIssuesByIssueIdCommmentsResponse.withJsonOK(result);
    }

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
    public PostProjectsByProjectIdIssuesByIssueIdCommmentsResponse postProjectsByProjectIdIssuesByIssueIdCommments(
        @PathParam("issueId")
        @NotNull
        int issueId,
        @PathParam("projectId")
        @NotNull
        int projectId,
        @Valid
        Comment entity)
        throws Exception
    {
        Comment result = manager.create(Comment.class, postProjectsByProjectIdIssuesByIssueIdCommments_meta, issueId, projectId, entity);
        return PostProjectsByProjectIdIssuesByIssueIdCommmentsResponse.withJsonCreated(result);
    }

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
    public PutProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse putProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId(
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
    {
        Comment result = manager.update(Comment.class, putProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId_meta, commentId, issueId, projectId, entity);
        return PutProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse.withJsonNonAuthoritativeInformation(result);
    }

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
    public GetProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse getProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId(
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
    {
        Comment result = manager.get(Comment.class, getProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId_meta, commentId, issueId, projectId);
        return GetProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse.withJsonOK(result);
    }

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
    public DeleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse deleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId(
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
    {
        Comment result = manager.delete(Comment.class, deleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentId_meta, commentId, issueId, projectId);
        return DeleteProjectsByProjectIdIssuesByIssueIdCommmentsByCommentIdResponse.withNoContent();
    }

}
