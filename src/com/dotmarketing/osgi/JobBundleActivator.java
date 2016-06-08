package com.dotmarketing.osgi;

import com.dotmarketing.quartz.ScheduledTask;

/**
 * Bundle for Jobs activation.
 * @author jsanca
 */
public class JobBundleActivator  extends BaseBundleActivator  {

    public JobBundleActivator() {
        super();
    }

    protected JobBundleActivator(final GenericBundleActivator bundleActivator) {
        super(bundleActivator);
    }

    /**
     * Register a given Quartz Job scheduled task
     *
     * @param scheduledTask
     * @throws Exception
     */
    protected final void scheduleQuartzJob ( final ScheduledTask scheduledTask ) throws Exception {

        this.getBundleActivator().scheduleQuartzJob(scheduledTask);
    } // registerActionlet.
} // E:O:F:ActionLetBundleActivator.
