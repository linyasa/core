package com.dotcms.rest;

import com.dotcms.repackage.javax.ws.rs.Consumes;
import com.dotcms.repackage.javax.ws.rs.QueryParam;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by freddyrodriguez on 20/5/16.
 */
@Api(tags = {"Test"})
@Path("/testing")
public class SimpleTestResource {

    @ApiOperation(value = "Just another simple test Rest service",
            notes = "Return a hello message with a user name",
            response = Message.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "If user is null") })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user")
    public Response get(
            @ApiParam(value = "User name", required = true)
            @QueryParam("user") String user){

        if (user == null){
            return Response.status(500).build();
        }else{
            return Response.status(200).entity(new Message("HELLO!!!!!!" + user)).build();
        }
    }

    @ApiOperation(value = "Save a new message",
            notes = "This service dont have any sense but it show how use swagger with POST Rest service")
    @ApiResponses(value = {@ApiResponse(code = 500, message = "If message is null"),
            @ApiResponse(code = 405, message = "if message content the word 'error'"),
            @ApiResponse(code = 301, message = "if the message was save succesfully")})
    @POST
    public Response post(
            @ApiParam(value = "Message to save", required = true)
            @QueryParam("message") String message){

        if (message == null){
            return Response.status(500).build();
        }else if(message.indexOf("error") != -1){
            return Response.status(405).build();
        }else{
            return Response.status(301).build();
        }
    }

    @ApiOperation(value = "Delete a message",
            notes = "This service dont have any sense but it show how use swagger with POST Rest service")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "if message id is null"),
            @ApiResponse(code = 200, message = "if the message was deleted succesfully")})
    @DELETE
    @Path("/{id}")
    public Response delete(
            @ApiParam(value = "Message to delete", required = true)
            @QueryParam("id") String id){

        return Response.status(200).build();
    }

    @ApiOperation(value = "Just a simple test Rest service",
            notes = "Return a hello message")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(){
        return "{\"message\": \"HELLO!!!!!!\"}";
    }
}
