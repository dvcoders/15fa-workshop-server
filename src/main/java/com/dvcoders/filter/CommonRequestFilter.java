package com.dvcoders.filter;

import org.jboss.logging.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Jake Loo (03 July, 2015)
 */
public class CommonRequestFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        MDC.put("event_id", UUID.randomUUID());
    }
}
