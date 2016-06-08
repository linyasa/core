package com.dotmarketing.osgi;

import com.dotcms.repackage.org.apache.struts.action.ActionMapping;
import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.liferay.portal.model.Portlet;
import org.apache.velocity.tools.view.ToolInfo;

import java.util.Collection;

/**
 * View Tool Info for Actions activation.
 * @author jsanca
 */
public class ToolInfoBundleActivator extends BaseBundleActivator {


    public ToolInfoBundleActivator() {

        super();
    }

    // Only for friends
    protected ToolInfoBundleActivator(final GenericBundleActivator bundleActivator) {

        super(bundleActivator);
    }

    /**
     * Register a ViewTool service using a ToolInfo object
     *
     * @param context
     * @param info
     */
    @SuppressWarnings ("unchecked")
    protected void registerViewToolService ( final BundleContext context,
                                             final ToolInfo info ) {

        this.getBundleActivator().registerViewToolService
                (context, info);
    }

} // E:O:F:ActionBundleActivator.
