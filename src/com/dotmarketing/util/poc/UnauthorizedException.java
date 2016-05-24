package com.dotmarketing.util.poc;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by freddyrodriguez on 20/5/16.
 */
public class UnauthorizedException extends WebApplicationException {
    private static final long serialVersionUID = 1L;


    public UnauthorizedException() {
        super(Response.status(Response.Status.UNAUTHORIZED)
                .entity("Please authenticate.").build());
    }
}