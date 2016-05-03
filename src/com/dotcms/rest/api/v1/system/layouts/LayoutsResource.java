package com.dotcms.rest.api.v1.system.layouts;

import com.dotcms.repackage.com.google.common.annotations.VisibleForTesting;
import com.dotcms.repackage.com.google.common.collect.Maps;
import com.dotcms.repackage.javax.ws.rs.GET;
import com.dotcms.repackage.javax.ws.rs.Path;
import com.dotcms.repackage.javax.ws.rs.Produces;
import com.dotcms.repackage.javax.ws.rs.core.Context;
import com.dotcms.repackage.javax.ws.rs.core.MediaType;
import com.dotcms.repackage.javax.ws.rs.core.Response;
import com.dotcms.rest.BaseRestPortlet;
import com.dotcms.rest.WebResource;
import com.dotcms.rest.api.v1.system.ruleengine.actionlets.ActionletTransform;
import com.dotcms.rest.exception.BadRequestException;
import com.dotcms.rest.exception.ForbiddenException;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.ApiProvider;
import com.dotmarketing.business.Layout;
import com.dotmarketing.business.LayoutAPI;
import com.dotmarketing.business.portal.PortletAPI;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.InvalidLicenseException;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portlet.PortletURLImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Path("/v1/system/layouts")
public class LayoutsResource {

    private final LayoutAPI layoutAPI;
    private final WebResource webResource;
    private final ActionletTransform transform = new ActionletTransform();
    private PortletAPI portletAPI;

    public LayoutsResource() {
        this(new ApiProvider());
    }

    private LayoutsResource(ApiProvider apiProvider) {
        this(APILocator.getPortletAPI(), apiProvider, new WebResource(apiProvider));
    }

    @VisibleForTesting
    protected LayoutsResource(PortletAPI portletAPI, ApiProvider apiProvider, WebResource webResource) {
        this.portletAPI = portletAPI;
        this.layoutAPI = apiProvider.layoutAPI();
        this.webResource = webResource;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@Context HttpServletRequest request) {
        User user = getUser(request);

        Map<String, RestLayout> layouts = getLayoutsInternal(request, user);
        return Response.ok(layouts).build();
    }

    public Map<String, RestLayout> getLayoutsInternal(HttpServletRequest request, User user) {
        try {
            List<Layout> layouts = layoutAPI.loadLayoutsForUser(user);
            Map<String, RestLayout> map = Maps.newHashMap();
            LayoutTransform transform = new LayoutTransform();

            for (Layout layout : layouts) {

                List<String> portletIDs = layout.getPortletIds();

                if(portletIDs.isEmpty()) {
                    continue;
                }

                String portletName = portletIDs.get(0);
                PortletURLImpl portletURLImpl = new PortletURLImpl(request, portletName, layout.getId(), false);
                String tabUrl = portletURLImpl.toString() + "&dm_rlout=1&r=" + System.currentTimeMillis();


                Portlet portlet = portletAPI.findPortlet(portletName);
                Object object = Class.forName(portlet.getPortletClass()).newInstance();
                if(object instanceof BaseRestPortlet) {
                    tabUrl = "javascript:dotAjaxNav.show('/api/portlet/" + portletName + "/', '" + layout + "');";
                }

                RestLayout restLayout = transform.appToRest(layout, tabUrl);
                map.put(layout.getId(), restLayout);
            }
            return map;
        } catch (DotDataException | ClassNotFoundException | InstantiationException e) {
            throw new BadRequestException(e, e.getMessage());
        } catch (IllegalAccessException | InvalidLicenseException e) {
            throw new ForbiddenException(e, e.getMessage());
        }
    }

    @VisibleForTesting
    User getUser(@Context HttpServletRequest request) {
        return webResource.init(true, request, true).getUser();
    }
}
