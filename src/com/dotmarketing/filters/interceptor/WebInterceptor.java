package com.dotmarketing.filters.interceptor;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Encapsulates an Interceptor; an interceptor is a good way to extends a filter behavior
 * Usually useful to be added as an OSGI plugin
 * @author jsanca
 */
public interface WebInterceptor extends Serializable {

    /**
     * Called on destroy
     */
    void destroy();

    /**
     * Called on init
     */
    void init();

    /**
     * Called in any request. Returns true if you want to continue the chain call, false otherwise.
     * @param req
     * @param res
     * @return boolean
     * @throws IOException
     */
    boolean intercept(ServletRequest req, ServletResponse res)
            throws IOException;
} // E:O:F:WebInterceptor.
