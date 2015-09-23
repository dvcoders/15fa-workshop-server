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

/**
 * @author Jake Loo (23 September, 2015)
 */
@Path("/github")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GithubResources {

    @Path("/{studentId}")
    @GET
    @Timed
    public void redirectGithub(@Context HttpServletRequest request,
                               @Context HttpServletResponse response,
                               @PathParam("studentId") String studentId) throws IOException {
        User user = UserResources.getUserFromDatastore(studentId);

        if (user == null) {
            throw new WebApplicationException("User not found",
                    Response.Status.NOT_FOUND);
        }

        String githubUrl = user.getGithubUrl();

        if (Utils.isEmpty(githubUrl)) {
            throw new WebApplicationException("Github url is not set",
                    Response.Status.NOT_FOUND);
        }

        response.setStatus(307);
        response.sendRedirect(githubUrl);
    }
}

