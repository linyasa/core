package com.dotmarketing.osgi;

import com.dotcms.repackage.org.apache.felix.http.api.ExtHttpService;
import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.dotcms.repackage.org.osgi.framework.ServiceReference;

import java.util.List;

/**
 * Bundle for Servet activation.
 * @author jsanca
 */
public abstract class ServletBundleActivator extends BaseBundleActivator  {

    private ExtHttpService extHttpService;
    protected List<ServletBean>  servlets;

    public ServletBundleActivator() {
        super();
    }

    protected ServletBundleActivator(final GenericBundleActivator bundleActivator) {
        super(bundleActivator);
    }


    protected final ExtHttpService getExtHttpService (final BundleContext bundleContext) {

        final ServiceReference serviceReference =
                bundleContext.getServiceReference ( ExtHttpService.class.getName() );

        return (null != serviceReference)?
                (ExtHttpService)bundleContext.getService(serviceReference):
                null;
    }


    @Override
    public void start(BundleContext bundleContext) throws Exception {

        super.start(bundleContext);

        this.servlets =
                this.getServlets(bundleContext);

        if (null != servlets) {

            this.extHttpService =
                    this.getExtHttpService(bundleContext);

            for (ServletBean servletBean : this.servlets) {

                this.extHttpService.registerServlet
                        (servletBean.getAlias(),
                                servletBean.getServlet(),
                                servletBean.getInitParams(),
                                servletBean.getHttpContext());
            }
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

        if ( null != this.extHttpService
                && null !=  this.servlets) {

            for (ServletBean servletBean : this.servlets) {

                this.extHttpService.unregisterServlet(servletBean.getServlet());
            }
        }

        super.stop(bundleContext);
    }

    /**
     * Get the servlet list config
     * @param bundleContext {@link BundleContext}
     * @return List
     */
    protected abstract List<ServletBean> getServlets (final BundleContext bundleContext);

} // E:O:F:PublishBundleActivator.
