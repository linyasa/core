package com.dotcms.rest.poc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.dotcms.repackage.org.codehaus.jettison.json.JSONObject;
import com.dotcms.rest.BaseRestPortlet;
import com.dotcms.rest.InitDataObject;
import com.dotcms.rest.ResourceResponse;
import com.dotcms.rest.WebResource;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.poc.JWTUtil;
import com.liferay.portal.util.CookieKeys;
import com.liferay.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jonathan Gamba
 *         5/16/16
 */
@Path("/jwt")
public class AngularResourcePortlet extends BaseRestPortlet {

    private final WebResource webResource = new WebResource();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("poc1")
    public Response poc1(@Context HttpServletRequest request, @Context HttpServletResponse response) throws DotDataException, DotSecurityException {

        //No user authentication here as we are testing JWT
        InitDataObject initData = webResource.init(null, false, request, false, null);
        ResourceResponse responseResource = new ResourceResponse(initData.getParamsMap());

        try {
            JSONObject json = new JSONObject();

            //If the token does not exist or is not valid an exception will be thrown
            String accessToken = CookieUtil.get(request.getCookies(), CookieKeys.JWT_ACCESS_TOKEN);
            JWTUtil.parseToken(accessToken);

            //If we are here is because the JWT is valid
            json.put("Success", "Working!!!");

            if ( request.getParameter("pretty") != null ) {
                return responseResource.response(json.toString(4));
            } else {
                return responseResource.response(json.toString());
            }
        } catch (Exception e) {
            Logger.error(this.getClass(), "Error processing :" + e.getMessage(), e);
            return responseResource.responseError(e.getMessage());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("poc2")
    public Response poc2(@Context HttpServletRequest request, @Context HttpServletResponse response) throws DotDataException, DotSecurityException {

        //No user authentication here as we are testing JWT
        InitDataObject initData = webResource.init(null, false, request, false, null);
        ResourceResponse responseResource = new ResourceResponse(initData.getParamsMap());

        try {
            JSONObject json = new JSONObject();

            //If the token does not exist or is not valid an exception will be thrown
            String accessToken = CookieUtil.get(request.getCookies(), CookieKeys.JWT_ACCESS_TOKEN);
            JWTUtil.parseToken(accessToken);

            //If we are here is because the JWT is valid
            json.put("Success", "Working!!!");

            if ( request.getParameter("pretty") != null ) {
                return responseResource.response(json.toString(4));
            } else {
                return responseResource.response(json.toString());
            }
        } catch (Exception e) {
            Logger.error(this.getClass(), "Error processing :" + e.getMessage(), e);
            return responseResource.responseError(e.getMessage());
        }
    }

}