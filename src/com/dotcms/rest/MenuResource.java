package com.dotcms.rest;

import com.dotcms.repackage.javax.ws.rs.*;
import com.dotcms.repackage.javax.ws.rs.core.Context;
import com.dotcms.repackage.javax.ws.rs.core.MediaType;
import com.dotcms.repackage.org.glassfish.jersey.server.JSONP;
import com.dotcms.rest.annotation.NoCache;
import com.dotcms.rest.validation.MenuContext;
import com.dotcms.spring.portlet.PortletController;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.Layout;
import com.dotmarketing.business.LayoutAPI;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.util.poc.JWTUtil;
import com.dotmarketing.util.poc.UnauthorizedException;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.ejb.UserLocalManagerUtil;
import com.liferay.portal.language.LanguageException;
import com.liferay.portal.language.LanguageUtil;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.util.CookieKeys;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.PortletURLImpl;
import com.liferay.portlet.StrutsPortlet;
import com.liferay.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by freddyrodriguez on 18/5/16.
 */
@Path("/{from}/menu")
public class MenuResource {

    public enum App{CORE, CORE_WEB};

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Collection<Menu> getMenus(@Context HttpServletResponse response, @PathParam("from") String from, @Context HttpServletRequest httpServletRequest)
            throws SystemException, PortalException, DotDataException, ClassNotFoundException {

        checkToken(httpServletRequest);
        //response.setHeader("Access-Control-Allow-Origin", "*");
        App appFrom = App.valueOf(from.toUpperCase());
        Collection<Menu> menus = new ArrayList<Menu>();

        HttpSession session = httpServletRequest.getSession();
        LayoutAPI api= APILocator.getLayoutAPI();
        User user = UserLocalManagerUtil.getUserById((String) session.getAttribute(WebKeys.USER_ID));

        List<Layout> layouts = api.loadLayoutsForUser(user);

        MenuContext menuContext = new MenuContext(httpServletRequest, user, appFrom);

        for (int layoutIndex = 0; layoutIndex < layouts.size(); layoutIndex++) {
            Layout layout = layouts.get( layoutIndex );
            String tabName = LanguageUtil.get(user, layout.getName());
            String tabDescription = LanguageUtil.get(user, layout.getDescription());
            List<String> portletIds = layout.getPortletIds();

            menuContext.setLayout(layout);
            menuContext.setPortletId(portletIds.get(0));
            menuContext.setLayoutIndex(layoutIndex);

            String url = getUrl(menuContext);
            Menu menu = new Menu( tabName, tabDescription, url );

            List<MenuItem> menuItems = getMenuItems(menuContext);

            menu.setMenuItems( menuItems );
            menus.add( menu  );
        }

        return menus;
    }

    private List<MenuItem> getMenuItems(MenuContext menuContext)
            throws LanguageException, ClassNotFoundException {

        List<MenuItem> menuItems = new ArrayList<>();
        List<String> portletIds = menuContext.getLayout().getPortletIds();

        for (String portletId : portletIds) {
            menuContext.setPortletId( portletId );
            String linkHREF = getUrl(menuContext);
            String linkName = LanguageUtil.get(menuContext.getUser(), "com.dotcms.repackage.javax.portlet.title." + portletId);
            boolean isAngular = isAngular( portletId );

            menuItems.add ( new MenuItem(portletId, linkHREF, linkName, isAngular) );
        }
        return menuItems;
    }

    private boolean isAngular(String portletId) throws ClassNotFoundException {
        Portlet portlet = APILocator.getPortletAPI().findPortlet(portletId);
        String portletClass = portlet.getPortletClass();
        Class classs = Class.forName( portletClass );
        return PortletController.class.isAssignableFrom( classs );
    }

    private String getUrl(MenuContext menuContext) throws ClassNotFoundException {
        Portlet portlet = APILocator.getPortletAPI().findPortlet( menuContext.getPortletId() );

        String portletClass = portlet.getPortletClass();
        Class classs = Class.forName( portletClass );
        App appFrom = menuContext.getAppFrom();

        System.out.println("### getPortletId" + menuContext.getPortletId());
        System.out.println("### portletClass" + portletClass);
        if(StrutsPortlet.class.isAssignableFrom( classs )) {
            PortletURLImpl portletURLImpl = new PortletURLImpl(menuContext.getHttpServletRequest(),
                    menuContext.getPortletId(), menuContext.getLayout().getId(), false);
            return portletURLImpl.toString() + "&dm_rlout=1&r=" + System.currentTimeMillis();
        }else if(BaseRestPortlet.class.isAssignableFrom( classs )) {
            if (App.CORE.equals( appFrom )){
                return "javascript:dotAjaxNav.show('/api/portlet/" + menuContext.getPortletId() + "/', '" + menuContext.getLayoutIndex() + "');";
            }else {
               return PortalUtil.getPortalURL(menuContext.getHttpServletRequest(), true) +
                       "/api/portlet/" + menuContext.getPortletId() + "/', '" + menuContext.getLayoutIndex();
            }
        }else if(PortletController.class.isAssignableFrom( classs )){
            if (App.CORE.equals( appFrom )) {
                return "/spring/portlet/" + menuContext.getPortletId();
            }else{
                return "/html/ng/p/" + menuContext.getPortletId();
            }
        }

        return null;
    }

    private void checkToken(HttpServletRequest httpServletRequest){
        try {
            //If the token does not exist or is not valid an exception will be thrown
            String accessToken = CookieUtil.get(httpServletRequest.getCookies(), CookieKeys.JWT_ACCESS_TOKEN);
            JWTUtil.parseToken(accessToken);
        }catch(Exception e){
            throw new UnauthorizedException();
        }
    }

}
