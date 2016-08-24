package com.dotcms.filters.interceptor;

import com.dotmarketing.util.Config;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Just encapsulates the Base logic to support interceptors in a filter.
 *
 * @author jsanca
 */
public abstract class AbstractWebInterceptorSupportFilter implements Filter {

    @Override
    public void init(final FilterConfig config) throws ServletException {

        this.getDelegate(config.getServletContext()).init();
    }

    @Override
    public void destroy() {

        this.getDelegate(Config.CONTEXT).destroy();
    } // destroy.

    /**
     * Runs all interceptors associated to this filter.
     * @param req {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     * @return boolean true if all interceptors ran successfully.
     * @throws IOException
     */
    protected boolean runInterceptors (final HttpServletRequest req, HttpServletResponse res) throws IOException {

        return this.getDelegate(req).intercept(req, res);
    } // runInterceptor.


    protected WebInterceptorDelegate getDelegate (final HttpServletRequest req) {

        final FilterWebInterceptorProvider filterWebInterceptorProvider =
                FilterWebInterceptorProvider.getInstance(req);

        final WebInterceptorDelegate delegate =
                filterWebInterceptorProvider.getDelegate(this.getClass());

        return delegate;
    }

    protected WebInterceptorDelegate getDelegate (final ServletContext context) {

        final FilterWebInterceptorProvider filterWebInterceptorProvider =
                FilterWebInterceptorProvider.getInstance(context);

        final WebInterceptorDelegate delegate =
                filterWebInterceptorProvider.getDelegate(this.getClass());

        return delegate;
    }

} // E:O:F:AbstractWebInterceptorSupportFilter.
