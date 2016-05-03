package com.dotcms.rest.api.v1.system.layouts;

import com.dotcms.rest.exception.InternalServerException;
import com.dotmarketing.business.Layout;
import com.liferay.portal.model.User;

/**
 * @author Geoff M. Granum
 */
public class LayoutTransform {

    public Layout restToApp(RestLayout rest, User user) {
        Layout app = new Layout();
        return applyRestToApp(rest, app, user);
    }

    public Layout applyRestToApp(RestLayout rest, Layout layout, User user) {
        layout.setId(rest.id);
        layout.setName(rest.name);
        layout.setDescription(rest.description);
        layout.setTabOrder(rest.tabOrder);
        layout.setPortletIds(rest.portletIds);
        return layout;
    }

    public RestLayout appToRest(Layout app, String tabUrl) {
        try {
            return new RestLayout.Builder()
                .id(app.getId())
                .name(app.getName())
                .description(app.getDescription())
                .tabOrder(app.getTabOrder())
                .portletIds(app.getPortletIds())
                .tabUrl(tabUrl)
                .build();
        } catch (Exception e) {
            throw new InternalServerException(e, "Could not create ReST Layout from Server, Layout id '%s'", app.getId());
        }
    }
}

