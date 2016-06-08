package com.dotmarketing.osgi;

import com.dotcms.repackage.org.osgi.framework.BundleActivator;
import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.dotcms.rest.config.RestServiceUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Bundle for Rest Activation,
 * Expects a list of a Rest Resources class
 * @author jsanca
 */
public abstract class RestBundleActivator extends BaseBundleActivator {

    private List<Class> resources;

    public RestBundleActivator() {
        super();
    }

    protected RestBundleActivator(final GenericBundleActivator bundleActivator) {
        super(bundleActivator);
    }

    @Override
    public  void start(final BundleContext bundleContext) throws Exception {

        this.resources = this.getResources(bundleContext);

        if (null != this.resources) {

            for (Class aClass : this.resources) {

                RestServiceUtil.addResource(aClass);
            }
        }
    }

    @Override
    public  void stop(final BundleContext bundleContext) throws Exception {

        if (null != this.resources) {

            for (Class aClass : this.resources) {

                RestServiceUtil.addResource(aClass);
            }
        }
    }

    /**
     * Returns the List of resources class.
     * @param bundleContext {@link BundleContext}
     * return List
     */
    protected abstract List<Class> getResources (final BundleContext bundleContext);


} // E:O:F:RestBundleActivator.
