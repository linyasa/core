package com.dotmarketing.osgi;

import com.dotcms.repackage.org.apache.felix.http.api.ExtHttpService;
import com.dotcms.repackage.org.osgi.framework.BundleActivator;
import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.dotcms.repackage.org.osgi.framework.ServiceReference;
import com.dotmarketing.portlets.rules.actionlet.RuleActionlet;
import com.dotmarketing.portlets.rules.conditionlet.Conditionlet;

import java.io.Serializable;

/**
 * Bundle for Rules activation.
 * @author jsanca
 */
public class RuleBundleActivator extends BaseBundleActivator {

    public RuleBundleActivator() {
        super();
    }

    protected RuleBundleActivator(final GenericBundleActivator bundleActivator) {
        super(bundleActivator);
    }

    /**
     * Register a Rules Engine RuleActionlet service
     */
    @SuppressWarnings("unchecked")
    protected final void registerRuleActionlet(final BundleContext context,
                                               final RuleActionlet actionlet) {

        this.getBundleActivator().registerRuleActionlet(context, actionlet);
    } // registerRuleActionlet.

    /**
     * Register a Rules Engine Conditionlet service
     *
     * @param context
     * @param conditionlet
     */
    @SuppressWarnings ("unchecked")
    protected final void registerRuleConditionlet (final BundleContext context,
                                                   final Conditionlet conditionlet) {

        this.getBundleActivator().registerRuleConditionlet(context, conditionlet);
    } // registerRuleConditionlet.
} // E:O:F:PublishBundleActivator.
