package dev.rudrade.exceptions;

import org.hibernate.PropertyValueException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.RestResponse.StatusCode;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.ws.rs.core.MediaType;

public class ExceptionMapper {

    @ServerExceptionMapper
    public RestResponse<?> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseBuilder
            .create(StatusCode.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON)
            .entity(exception.getLocalizedMessage())
            .build();
    }


    @ServerExceptionMapper
    public RestResponse<?> handlePropertyValueException(PropertyValueException exception) {
        return ResponseBuilder
            .create(StatusCode.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON)
            .entity("Invalid property value of "+exception.getPropertyName())
            .build();
    }
}