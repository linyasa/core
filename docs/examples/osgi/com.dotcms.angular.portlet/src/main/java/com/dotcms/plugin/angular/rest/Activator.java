package com.dotcms.plugin.angular.rest;

import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.dotcms.rest.config.RestServiceUtil;
import com.dotmarketing.osgi.GenericBundleActivator;
import com.dotmarketing.util.Logger;

public class Activator extends GenericBundleActivator {

	Class clazz = AngularHelloResource.class;


	public void start(BundleContext context) throws Exception {

		Logger.info(this.getClass(), "Adding new Restful Service:" + clazz.getSimpleName());
		RestServiceUtil.addResource(clazz);
		
		String[] xmls = new String[]{"conf/portlet.xml", "conf/liferay-portlet.xml"};
        registerPortlets( context, xmls );

	}

	public void stop(BundleContext context) throws Exception {

		Logger.info(this.getClass(), "Removing new Restful Service:" + clazz.getSimpleName());
		RestServiceUtil.removeResource(clazz);
		unregisterServices( context );
	}

}