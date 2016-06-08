package com.dotmarketing.osgi;

import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.dotmarketing.portlets.workflows.actionlet.WorkFlowActionlet;

/**
 * Bundle for Actions Let activation.
 * @author jsanca
 */
public class ActionLetBundleActivator extends BaseBundleActivator {

    public ActionLetBundleActivator() {
        super();
    }

    protected ActionLetBundleActivator(final GenericBundleActivator bundleActivator) {
        super(bundleActivator);
    }

    /**
     * Register a WorkFlowActionlet service
     *
     * @param context
     * @param actionlet
     */
    protected final void registerActionlet ( final BundleContext context,
                                             final WorkFlowActionlet actionlet ) {

        this.getBundleActivator().registerActionlet(context, actionlet);
    } // registerActionlet.
} // E:O:F:ActionLetBundleActivator.
