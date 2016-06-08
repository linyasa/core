package com.dotmarketing.osgi;

import com.dotcms.repackage.org.apache.felix.http.api.ExtHttpService;
import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.dotcms.repackage.org.osgi.framework.ServiceReference;
import com.dotmarketing.filters.CMSFilter;

import java.util.List;

/**
 * Bundle for Filter activation.
 * @author jsanca
 */
public abstract class FilterBundleActivator extends BaseBundleActivator  {

    private List<FilterBean>  filters;

    private ExtHttpService extHttpService;

    public FilterBundleActivator() {
        super();
    }

    protected FilterBundleActivator(final GenericBundleActivator bundleActivator) {
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

        this.filters =
                this.getFilters(bundleContext);

        if (null != this.filters) {

            this.extHttpService =
                    this.getExtHttpService(bundleContext);

            for (FilterBean filterBean : this.filters) {

                this.extHttpService.registerFilter
                        (filterBean.getFilter(),
                                filterBean.getPattern(),
                                filterBean.getInitParams(),
                                filterBean.getRanking(),
                                filterBean.getHttpContext());

                CMSFilter.addExclude(filterBean.getExcludePath());
            }
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

        if (null != this.filters) {

            for (FilterBean filterBean : this.filters) {

                // todo: ask Jonathan which one to used
                CMSFilter.removeExclude
                        (filterBean.getExcludePath());
            }
        }

        super.stop(bundleContext);
    }

    /**
     * Returns the filters configuration
     * @param bundleContext BundleContext
     * @return List
     */
    protected abstract List<FilterBean> getFilters (final BundleContext bundleContext);


} // E:O:F:PublishBundleActivator.
