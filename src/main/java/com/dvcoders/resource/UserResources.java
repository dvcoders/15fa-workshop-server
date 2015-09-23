package com.dvcoders.resource;


import com.codahale.metrics.annotation.Timed;
import com.dvcoders.model.User;
import com.dvcoders.other.Datastore;
import com.dvcoders.other.Utils;
import com.google.common.collect.ImmutableMap;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * @author Jake Loo (03 July, 2015)
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResources {

    @Path("/list")
    @GET
    @Timed
    public List<User> getUsers() {
        List<User> users = Datastore.getInstance().getDatastore()
                .createQuery(User.class).asList();

        return users;
    }

    @Path("/null")
    @GET
    @Timed
    public User internalError() {
        int i = 1 / 0;
        return new User();
    }

    @GET
    @Timed
    public User getUser(@QueryParam("student_id") String studentId) {
        if (Utils.isEmpty(studentId)) {
            throw new WebApplicationException("student_id is required",
                    Response.Status.BAD_REQUEST);
        }

        User user = getUserFromDatastore(studentId);

        if (user == null) {
            throw new WebApplicationException("User not found",
                    Response.Status.NOT_FOUND);
        }

        return user;
    }

    @POST
    @Timed
    public User createUser(User user) {
        if (Utils.isEmpty(user.getStudentId())) {
            throw new WebApplicationException("student_id is required",
                    Response.Status.BAD_REQUEST);
        }

        User existingUser = getUserFromDatastore(user.getStudentId());
        if (existingUser != null) {
            throw new WebApplicationException("User is exist",
                    Response.Status.CONFLICT);
        }

        Datastore.getInstance().getDatastore().save(user);

        return user;
    }

    @PUT
    @Timed
    public User updateUser(User user) {
        if (Utils.isEmpty(user.getStudentId())) {
            throw new WebApplicationException("student_id is required",
                    Response.Status.BAD_REQUEST);
        }

        User existUser = getUserFromDatastore(user.getStudentId());

        if (existUser == null) {
            throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
        }

        if (Utils.notEmpty(user.getFirstName())) {
            existUser.setFirstName(user.getFirstName());
        }
        if (Utils.notEmpty(user.getLastName())) {
            existUser.setLastName(user.getLastName());
        }
        if (Utils.notEmpty(user.getGithubUrl())) {
            existUser.setGithubUrl(user.getGithubUrl());
        }

        Datastore.getInstance().getDatastore().save(existUser);
        return existUser;
    }

    @DELETE
    @Timed
    public Map<String, Object> deleteUser(User user) {
        throw new WebApplicationException("You're do not have the privilege",
                Response.Status.UNAUTHORIZED);

//        User existingUser = getUserFromDatastore(user.getStudentId());
//        if (existingUser == null) {
//            throw new WebApplicationException("User not found",
//                    Response.Status.NOT_FOUND);
//        }
//
//        Datastore.getInstance().getDatastore().delete(User.class, user);
//
//        return ImmutableMap.of("student_id", user.getStudentId(), "delete", true);
    }

    public static User getUserFromDatastore(String studentId) {
        try {
            User user = Datastore.getInstance().getDatastore()
                    .find(User.class, "studentId", studentId).get();

            return user;
        } catch (Exception e) {}
        return null;
    }
}
