package com.dotmarketing.osgi;

import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.dotcms.repackage.org.tuckey.web.filters.urlrewrite.Rule;
import com.dotmarketing.util.ConversionUtils;
import com.dotmarketing.util.Converter;

import java.util.Arrays;
import java.util.List;

/**
 * Bundle for Tuckey activation.
 * @author jsanca
 */
public abstract class TuckeyBundleActivator extends ServletBundleActivator {

    protected List<TuckeyBean>  tuckeys;

    private ConversionUtils conversionUtils = ConversionUtils.INSTANCE;

    private Converter<TuckeyBean, ServletBean> converter = new Converter<TuckeyBean, ServletBean>() {
        @Override
        public ServletBean convert(final TuckeyBean tuckeyBean) {

            return (ServletBean)tuckeyBean;
        }
    };

    public TuckeyBundleActivator() {
        super();
    }


    protected TuckeyBundleActivator(final GenericBundleActivator bundleActivator) {
        super(bundleActivator);
    }

    @Override
    public  void start(final BundleContext bundleContext) throws Exception {

        super.start(bundleContext);

        if (null == this.tuckeys) {

            this.tuckeys =
                    this.getTuckeys(bundleContext);
        }

        if (null != this.tuckeys) {

            for (TuckeyBean tuckeyBean : this.tuckeys) {

                addRules(tuckeyBean);
            }
        }
    }

    private void addRules(final TuckeyBean tuckeyBean) throws Exception {

        for (Rule rule : tuckeyBean.getRules()) {

            if (null != rule) {

                this.getBundleActivator().addRewriteRule(rule);
            }
        }
    }

    @Override
    public  void stop(final BundleContext bundleContext) throws Exception {

        super.stop(bundleContext);
    }

    @Override
    protected List<ServletBean> getServlets(BundleContext bundleContext) {

        if (null == this.tuckeys) {

            this.tuckeys =
                    this.getTuckeys(bundleContext);
        }

        return this.conversionUtils.convert(this.tuckeys, this.converter);
    }

    protected abstract List<TuckeyBean> getTuckeys(final BundleContext bundleContext);


} // E:O:F:ActionBundleActivator.
