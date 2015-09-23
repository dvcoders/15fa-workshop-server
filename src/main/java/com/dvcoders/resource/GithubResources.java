package com.dvcoders.resource;

import com.codahale.metrics.annotation.Timed;
import com.dvcoders.model.User;
import com.dvcoders.other.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;

/**
 * @author Jake Loo (23 September, 2015)
 */
@Path("/github")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MEDIA_TYPE_WILDCARD)
public class GithubResources {

    @Path("/{studentId}")
    @GET
    @Timed
    public Response redirectGithub(@Context HttpServletRequest request,
                               @Context HttpServletResponse response,
                               @PathParam("studentId") String studentId) throws IOException {
        User user = UserResources.getUserFromDatastore(studentId);

        if (user == null) {
            throw new WebApplicationException("User not found",
                    Response.Status.NOT_FOUND);
        }

        String githubUrl = user.getGithubUrl();

        if (Utils.isEmpty(githubUrl)) {
            throw new WebApplicationException("No github url associated with the account",
                    Response.Status.FORBIDDEN);
        }

        return Response
                .temporaryRedirect(URI.create(githubUrl))
                .build();
    }
}

