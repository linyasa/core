package com.dotcms.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by freddyrodriguez on 20/5/16.
 */
@Api(tags = {"Test"})
@Path("/testing")
public class SimpleTestResource {

    @ApiOperation(value = "Just a simple test Rest service",
            notes = "Return a hello message")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(){
        return "{\"message\": \"HELLO!!!!!!\"}";
    }
}
