package com.dotcms.rest.validation;

import com.dotcms.rest.MenuResource;
import com.dotmarketing.business.Layout;
import com.liferay.portal.model.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by freddyrodriguez on 18/5/16.
 */
public class MenuContext {

    private final User user;
    private final MenuResource.App appFrom;
    private HttpServletRequest httpServletRequest;
    private Layout layout;
    private String portletId;
    private int layoutIndex;

    public MenuContext(HttpServletRequest httpServletRequest, User user, MenuResource.App appFrom) {
        this.httpServletRequest = httpServletRequest;
        this.user = user;
        this.appFrom = appFrom;
    }

    public int getLayoutIndex() {
        return layoutIndex;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public Layout getLayout() {
        return layout;
    }

    public String getPortletId() {
        return portletId;
    }

    public void setLayoutIndex(int layoutIndex) {
        this.layoutIndex = layoutIndex;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void setPortletId(String portletId) {
        this.portletId = portletId;
    }

    public User getUser() {
        return user;
    }

    public MenuResource.App getAppFrom() {
        return appFrom;
    }
}
