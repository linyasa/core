package com.dotcms.rest.api.v1.user;

import com.dotcms.repackage.com.google.common.annotations.VisibleForTesting;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.server.JSONP;
import com.dotcms.rest.WebResource;
import com.dotcms.rest.annotation.NoCache;
import com.dotcms.rest.exception.BadRequestException;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.ApiProvider;
import com.dotmarketing.business.Role;
import com.dotmarketing.exception.DotDataException;
import com.liferay.portal.model.User;
import javax.servlet.http.HttpServletRequest;

@Path("/v1/users")
public class UserResource {

    private final WebResource webResource;

    @SuppressWarnings("unused")
    public UserResource() {
        this(new WebResource(new ApiProvider()));
    }

    @VisibleForTesting
    protected UserResource(WebResource webResource) {
        this.webResource = webResource;
    }

    @GET
    @JSONP
    @Path("/current")
    @NoCache
    @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
    public RestUser self(@Context HttpServletRequest request) {
        User user = webResource.init(true, request, true).getUser();
        RestUser.Builder currentUser = new RestUser.Builder();
        if(user != null) {
            try {
                Role role = APILocator.getRoleAPI().getUserRole(user);
                currentUser.userId(user.getUserId())
                    .givenName(user.getFirstName())
                    .surname(user.getLastName())
                    .roleId(role.getId());
            } catch (DotDataException e) {
                throw new BadRequestException("Could not provide current user.");
            }
        }
        return currentUser.build();
    }
}
