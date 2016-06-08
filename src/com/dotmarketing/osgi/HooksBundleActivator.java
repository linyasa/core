package com.dotmarketing.osgi;

/**
 * Bundle for Hooks activation.
 * @author jsanca
 */
public class HooksBundleActivator extends BaseBundleActivator {

    public HooksBundleActivator() {
        super();
    }

    protected HooksBundleActivator(final GenericBundleActivator bundleActivator) {
        super(bundleActivator);
    }

    /**
     * Adds a hook to the end of the chain
     *
     * @param preHook
     * @throws Exception
     */
    protected final void addPreHook ( final Object preHook ) throws Exception {

        this.getBundleActivator().addPreHook(preHook);
    } // addPreHook

    /**
     * Adds a hook to the end of the chain
     *
     * @param postHook
     * @throws Exception
     */
    protected final void addPostHook ( final Object postHook ) throws Exception {

        this.getBundleActivator().addPostHook(postHook);
    } // addPostHook

} // E:O:F:PublishBundleActivator.
