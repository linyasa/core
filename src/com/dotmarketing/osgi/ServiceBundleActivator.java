package com.dotmarketing.osgi;

import com.dotcms.repackage.org.apache.struts.action.ActionMapping;
import com.dotcms.repackage.org.osgi.framework.BundleActivator;
import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.liferay.portal.model.Portlet;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Bundle for Services activation.
 * @author jsanca
 */
public class ServiceBundleActivator extends BaseBundleActivator {

    public ServiceBundleActivator() {
        super();
    }

    protected ServiceBundleActivator(final GenericBundleActivator bundleActivator) {
        super(bundleActivator);
    }

    @Override
    public  void start(final BundleContext bundleContext) throws Exception {

        final List<ServiceBean<?>> services =
                this.getServices(bundleContext);

        if (null != services) {

            for(ServiceBean<?> serviceBean : services) {

                this.getBundleActivator().registerService(bundleContext,
                        serviceBean.getService(), serviceBean.getProperties());
            }
        }

    }

    @Override
    public  void stop(final BundleContext bundleContext) throws Exception {

        // NOTE: the service is automatically unregistered
    }

    /**
     * Get the list of services to register
     * @return List
     */
    protected List<ServiceBean<?>> getServices (final BundleContext bundleContext) {

        return null; // return here the list of services
    }

} // E:O:F:ActionBundleActivator.
