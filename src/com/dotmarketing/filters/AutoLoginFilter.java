/*
 * WebSessionFilter
 *
 * A filter that recognizes return users who have
 * chosen to have their login information remembered.
 * Creates a valid WebSession object and
 * passes it a contact to use to fill its information
 *
 */
package com.dotmarketing.filters;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dotmarketing.cms.factories.PublicEncryptionFactory;
import com.dotmarketing.cms.login.factories.LoginFactory;
import com.dotmarketing.filters.interceptor.WebInterceptor;
import com.dotmarketing.filters.interceptor.WebInterceptorAware;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.WebKeys;

public class AutoLoginFilter implements Filter, WebInterceptorAware {

    private boolean alreadyStarted = false;

    private final List<WebInterceptor> interceptors =
            new CopyOnWriteArrayList<>();

    public void destroy() {

        if (!this.interceptors.isEmpty()) {

            this.interceptors.forEach(interceptor -> interceptor.destroy());
        }
    }

    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException,
            ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = request.getSession(false);
        boolean shouldContinue = true;
		
		boolean useCasFilter = Config.getBooleanProperty("FRONTEND_CAS_FILTER_ON");
		
        if (useCasFilter){
        	String userID = (String)session.getAttribute("edu.yale.its.tp.cas.client.filter.user");
        	Logger.debug(AutoLoginFilter.class, "Doing CasAutoLogin Filter for: " + userID);
            if(UtilMethods.isSet(userID)){                
            	LoginFactory.doCookieLogin(PublicEncryptionFactory.encryptString(userID), request, response);      	
            }
        }
        else{ // todo: should we remove this functionality???
	        String encryptedId = UtilMethods.getCookieValue(request.getCookies(), WebKeys.CMS_USER_ID_COOKIE);
	 
	        if (((session != null && session.getAttribute(WebKeys.CMS_USER) == null) || session == null)&& 
	        		UtilMethods.isSet(encryptedId)) {
	            Logger.debug(AutoLoginFilter.class, "Doing AutoLogin for " + encryptedId);
	            LoginFactory.doCookieLogin(encryptedId, request, response);
	        } else {

                if (!this.interceptors.isEmpty()) {

                    for (WebInterceptor webInterceptor : this.interceptors) {

                        shouldContinue &= webInterceptor.intercept(req, response);

                        if (!shouldContinue) {
                            // if just one interceptor failed; we stopped the loop and do not continue the chain call
                            return;
                        }
                    }
                }
            }
	    }
        chain.doFilter(req, response);
    }
    public void init(final FilterConfig config) throws ServletException {

        if (!this.interceptors.isEmpty()) {

            this.interceptors.forEach(interceptor -> interceptor.init());
        }

        this.alreadyStarted = true;
    }

    @Override
    public void add(final WebInterceptor webInterceptor) {

        if (null != webInterceptor) {

            if (this.alreadyStarted) {

                webInterceptor.init();
            }

            this.interceptors.add(webInterceptor);
        }
    }
}
