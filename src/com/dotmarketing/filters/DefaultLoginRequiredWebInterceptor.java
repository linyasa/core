package com.dotmarketing.filters;

import com.dotcms.filters.interceptor.Result;
import com.dotcms.filters.interceptor.WebInterceptor;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.WebKeys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Default implementation.
 * @author jsanca
 */
public class DefaultLoginRequiredWebInterceptor implements WebInterceptor {

    @Override
    public Result intercept(final HttpServletRequest request,
                             final HttpServletResponse response) throws IOException {

        final HttpSession session = request.getSession(false);
        final boolean ADMIN_MODE = (session.getAttribute
                (com.dotmarketing.util.WebKeys.ADMIN_MODE_SESSION) != null);
        Result result = Result.NEXT;

        // if we are not logged in, go to login page
        if (session.getAttribute(WebKeys.CMS_USER) == null && !ADMIN_MODE) {

            Logger.warn(this.getClass(),
                    "Doing LoginRequiredFilter for RequestURI: " +
                            request.getRequestURI() + "?" + request.getQueryString());

            //if we don't have a redirect yet
            session.setAttribute(WebKeys.REDIRECT_AFTER_LOGIN,
                    request.getRequestURI() + "?" + request.getQueryString());

            final ActionMessages ams = new ActionMessages();
            ams.add(Globals.MESSAGE_KEY, new ActionMessage("message.login.required"));
            session.setAttribute(Globals.MESSAGE_KEY, ams);
            response.sendError(401);
            result = Result.SKIP_NO_CHAIN; // needs to stop the filter chain.
        }

        return result; // if it is log in, continue!
    } // intercept.

} // E:O:F:DefaultLoginRequiredWebInterceptor.
