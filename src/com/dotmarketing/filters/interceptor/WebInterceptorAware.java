package com.dotmarketing.filters.interceptor;

import java.io.Serializable;

/**
 * This contract is just used to add a new web interceptor to a filter.
 * @author jsanca
 */
public interface WebInterceptorAware extends Serializable {

    /**
     * Called when the apps wants to add a new interceptor to the filter
     * @param webInterceptor {@link WebInterceptor}
     */
    public void add (WebInterceptor  webInterceptor);

} // E:O:F:WebInterceptorAware.
