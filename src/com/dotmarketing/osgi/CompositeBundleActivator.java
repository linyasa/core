package com.dotmarketing.osgi;

import com.dotcms.repackage.org.jboss.util.collection.CollectionsUtil;
import com.dotcms.repackage.org.osgi.framework.BundleActivator;
import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.dotmarketing.util.CollectionsUtils;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.ReflectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * In case you need to add two or more bundle activator to the bundle context, you might want to use
 * @author jsanca
 */
public class CompositeBundleActivator implements BundleActivator, Serializable {

    private final GenericBundleActivator genericBundleActivator =
            new ConcreteGenericBundleActivator();

    private List<BundleActivator> bundleActivators =
            CollectionsUtils.getNewList();

    private CompositeBundleActivator () {}

    public static final CompositeBundleActivator createCompositeBundleActivator (
            final Class<BundleActivator>... activators) {

        BundleActivator bundleActivator = null;
        final GenericBundleActivator genericBundleActivator =
                new ConcreteGenericBundleActivator();

        final CompositeBundleActivator compositeBundleActivator =
                new CompositeBundleActivator();

        if (null != activators) {

            for (Class<BundleActivator> activatorClass : activators) {

                bundleActivator =
                        ReflectionUtils.newInstance(activatorClass,
                                genericBundleActivator);

                if (null != bundleActivator) {

                    compositeBundleActivator.bundleActivators.add(bundleActivator);
                } else {

                    if (Logger.isWarnEnabled(CompositeBundleActivator.class)) {

                        Logger.warn(CompositeBundleActivator.class, "Couldn't load the bundle activator: " + activatorClass.getName());
                    }
                }
            }
        }

        return compositeBundleActivator;
    } // createCompositeBundleActivator.

    @Override
    public void start(final BundleContext bundleContext) throws Exception {

        for (BundleActivator bundleActivator: this.bundleActivators) {

            bundleActivator.start(bundleContext);
        }
    }

    @Override
    public void stop(final BundleContext bundleContext) throws Exception {

        Collections.reverse(this.bundleActivators);
        for (BundleActivator bundleActivator: this.bundleActivators) {

            bundleActivator.stop(bundleContext);
        }
    }
} // E:O:F:CompositeBundleActivator.
