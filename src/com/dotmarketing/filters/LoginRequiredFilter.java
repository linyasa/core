/*
 * WebSessionFilter
 * 
 * A filter that recognizes return users who have chosen to have their login
 * information remembered. Creates a valid WebSession object and passes it a
 * contact to use to fill its information
 *  
 */
package com.dotmarketing.filters;

import com.dotcms.filters.interceptor.AbstractWebInterceptorSupportFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This Filter is in charge if checking if the user is logged in or not.
 * By default will use an interceptor that check if the user is logged in, if it is not, will returns a 401 and set a REDIRECT_AFTER_LOGIN
 * it does not apply if ADMIN_MODE is on.
 *
 * In addition you can extends the intercept functionality by implementing your on filter.
 * @author jsanca
 */
public class LoginRequiredFilter extends AbstractWebInterceptorSupportFilter {

    @Override
    public void init(final FilterConfig config) throws ServletException {

        this.addDefaultInterceptors (config);
        super.init(config);
    } // init.

    // add the previous legacy code to be align with the interceptor approach.
    private void addDefaultInterceptors(final FilterConfig config) {

        this.getDelegate(config.getServletContext()).add(new DefaultLoginRequiredWebInterceptor());
    } // addDefaultInterceptors.

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
       
    	final HttpServletResponse response = (HttpServletResponse) res;
        final HttpServletRequest request   = (HttpServletRequest) req;

        if (this.runInterceptors(request, response)) {

            chain.doFilter(req, response);
        }
    } // doFilter.

} // LoginRequiredFilter.