package dev.rudrade.log;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

import io.quarkus.logging.Log;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;

class ApiLogger {
    
    @ServerRequestFilter
    public void logRequest(ContainerRequestContext requestContext) {
        Log.debug(" ### API called: "+requestContext.getUriInfo().getPath());
        if (requestContext.getUriInfo().getQueryParameters() != null && !requestContext.getUriInfo().getQueryParameters().isEmpty()) {
            Log.debug("## Query parameters: "+requestContext.getUriInfo().getQueryParameters().toString());
        }
        if (requestContext.getUriInfo().getPathParameters() != null && !requestContext.getUriInfo().getPathParameters().isEmpty()) {
            Log.debug("## Path parameters: "+requestContext.getUriInfo().getPathParameters().toString());
        }

        if (requestContext.hasEntity()) {
            try {
                byte[] arrBytes = requestContext.getEntityStream().readAllBytes();
                Log.debug("## Request body: "+new String(arrBytes));

                // After reading the entity stream, we need to reset it so that it can be read again by the resource method
                requestContext.setEntityStream(new ByteArrayInputStream(arrBytes));

            } catch(IOException e) {
                Log.error("## Error reading request entity", e);
            }
        }
    }

    @ServerResponseFilter
    public void logResponse(ContainerResponseContext responseContext) {
        Log.debug("## Response: "+responseContext.getEntity());
    }

}
