package com.dvcoders.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import io.dropwizard.validation.ConstraintViolations;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.jersey.internal.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;
/**
 * @author Jake Loo (23 September, 2015)
 */
@Provider
public class RootExceptionMapper implements ExceptionMapper<Exception> {

    private static final Joiner joiner = Joiner.on(", ");

    final static Logger LOG = LoggerFactory.getLogger(RootExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        //multi exceptions with only one exception will just be treated as a single exception
        if (exception instanceof MultiException) {
            MultiException me = ((MultiException) exception);
            if (me.getErrors().size() == 1) {
                exception = (Exception) me.getErrors().get(0);
            }
        }

        if (exception instanceof WebApplicationException) {
            WebApplicationException ex = ((WebApplicationException) exception);

            String message = ex.getMessage();
            int status = ex.getResponse().getStatus();

            if (status >= 500) {
                LOG.error("server error {}", message);
            }

            return makeResponse(message, status);

        } else if (exception instanceof ConstraintViolationException) {
            //these happen when something fails validation
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
            ImmutableList<String> errs = ConstraintViolations.formatUntyped(constraintViolations);
            return makeResponse(joiner.join(errs), Status.BAD_REQUEST.getStatusCode());

        } else if (exception instanceof NullPointerException) {
            LOG.error("null pointer", exception);

                //TODO: return something else here?
            return makeResponse("Null Pointer Exception", Status.INTERNAL_SERVER_ERROR.getStatusCode());

        } else if (exception instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException e = ((UnrecognizedPropertyException) exception);

            String message = e.getPropertyName() + " is not a valid paramater; valid=" + e.getKnownPropertyIds();

            return makeResponse(message, Status.BAD_REQUEST.getStatusCode());

        }

        LOG.error("exception", exception);
        return makeResponse(exception.getMessage(), Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    public static Response makeResponse(String message, int code) {
        return Response
                .status(code)
                .entity(new ErrorMessage(message))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private static class ErrorMessage {

        @JsonProperty("message")
        private String message;

        public ErrorMessage() {
        }

        public ErrorMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
