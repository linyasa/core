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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dotcms.filters.interceptor.AbstractWebInterceptorSupportFilter;
import com.dotcms.filters.interceptor.WebInterceptor;
import com.dotcms.filters.interceptor.WebInterceptorDelegate;
import com.dotmarketing.cms.factories.PublicEncryptionFactory;
import com.dotmarketing.cms.login.factories.LoginFactory;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.WebKeys;

/**
 * Auto login is useful to do the auto login based on some configuration and preconditions.
 * DotCMS offers several approaches to do auto login, for instance CAS, JWT, OpenSAML, etc.
 * @author jsanca
 */
public class AutoLoginFilter extends AbstractWebInterceptorSupportFilter {

    @Override
    public void init(final FilterConfig config) throws ServletException {

        this.addDefaultInterceptors (config);
        super.init(config);
    } // init.

    // add the previous legacy code to be align with the interceptor approach.
    private void addDefaultInterceptors(final FilterConfig config) {

        final WebInterceptorDelegate delegate =
                this.getDelegate(config.getServletContext());

        delegate.add(new CasAutoLoginWebInterceptor());
        delegate.add(new DefaultAutoLoginWebInterceptor());
    } // addDefaultInterceptors.

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {

        final HttpServletResponse response = (HttpServletResponse) res;
        final HttpServletRequest request   = (HttpServletRequest) req;

        if (this.runInterceptors(request, response)) {

            chain.doFilter(req, response);
        }
    } // doFilter.
} // E:O:F:AutoLoginFilter.
