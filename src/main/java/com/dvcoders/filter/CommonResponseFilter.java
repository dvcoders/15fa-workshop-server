package com.dvcoders.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.dropwizard.setup.Environment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Jake Loo (03 July, 2015)
 */
@Provider
public class CommonResponseFilter implements ContainerResponseFilter {

    final static Logger LOG = LoggerFactory.getLogger(CommonResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object entity = responseContext.getEntity();

        Result result = new Result(responseContext.getStatus(), entity);

        responseContext.setEntity(result);
    }

    public static class Result {

        @JsonProperty("code")
        private Integer code;

        @JsonProperty("current_at")
        private Long currentAt;

        @JsonProperty("event_id")
        private String event;

        @JsonUnwrapped
        private Object result;

        public Result() {
        }

        public Result(Integer code, Object result) {
            this.code = code;
            this.result = result;
            this.currentAt = System.currentTimeMillis();
            this.event = MDC.get("event_id");
        }

        public Integer getCode() {
            return code;
        }

        public Object getResult() {
            return result;
        }

        public String getEvent() {
            return event;
        }

        public Long getCurrentAt() {
            return currentAt;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
        }
    }
}
