package com.dotmarketing.osgi;

import com.dotcms.repackage.org.apache.struts.action.ActionMapping;
import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.liferay.portal.model.Portlet;

import java.util.Collection;

/**
 * Bundle for Actions activation.
 * @author jsanca
 */
public class ActionBundleActivator extends BaseBundleActivator {


    public ActionBundleActivator() {

        super();
    }

    // Only for friends
    protected ActionBundleActivator(final GenericBundleActivator bundleActivator) {

        super(bundleActivator);
    }

    /**
     * Register a given ActionMapping
     *
     * @param actionMapping
     * @throws Exception
     */
    protected final void registerActionMapping ( final ActionMapping actionMapping ) throws Exception {

        this.getBundleActivator().registerActionMapping(actionMapping);
    } // registerActionMapping.

    /**
     * Register the portlets on the given configuration files
     *
     * @param xmls
     * @throws Exception
     */
    @SuppressWarnings ("unchecked")
    protected final Collection<Portlet> registerPortlets (final BundleContext context,
                                                    final String[] xmls ) throws Exception {

        return this.getBundleActivator().registerPortlets(context, xmls);
    } // registerPortlets
} // E:O:F:ActionBundleActivator.
