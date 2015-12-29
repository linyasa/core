package com.liferay.portlet;

import com.dotcms.repackage.javax.portlet.*;

import java.io.IOException;

public class NoOpPortlet implements Portlet{
    @Override
    public void init(PortletConfig portletConfig) throws PortletException {

    }

    @Override
    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {

    }

    @Override
    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {

    }

    @Override
    public void destroy() {

    }
}
