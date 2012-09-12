package com.dotmarketing.osgi;

import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.Interceptor;
import com.dotmarketing.portlets.workflows.actionlet.WorkFlowActionlet;
import com.dotmarketing.portlets.workflows.business.WorkflowAPIOsgiService;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import org.apache.velocity.tools.view.PrimitiveToolboxManager;
import org.apache.velocity.tools.view.ToolInfo;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.beans.IntrospectionException;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Jonathan Gamba
 * Date: 7/23/12
 */
public abstract class GenericBundleActivator implements BundleActivator {

    private PrimitiveToolboxManager toolboxManager;
    private WorkflowAPIOsgiService workflowOsgiService;
    private Collection<ToolInfo> viewTools;
    private Collection<WorkFlowActionlet> actionlets;

    /**
     * Allow to this bundle/elements to be visible and accessible from the host classpath
     */
    public void publishBundleServices ( BundleContext context ) {

        //Felix classloader
        ClassLoader felixClassLoader = getFelixClassLoader();

        //Create a new class loader where we can "combine" our classloaders
        CombinedLoader combinedLoader;
        if ( Thread.currentThread().getContextClassLoader() instanceof CombinedLoader ) {
            combinedLoader = (CombinedLoader) Thread.currentThread().getContextClassLoader();
            combinedLoader.addLoader( felixClassLoader );
        } else {
            combinedLoader = new CombinedLoader( Thread.currentThread().getContextClassLoader() );
            combinedLoader.addLoader( felixClassLoader );
        }

        //Force the loading of some classes that may be already loaded on the host classpath but we want to override with the ones on this bundle and we specified
        String overrideClasses = context.getBundle().getHeaders().get( "Override-Classes" );
        if ( overrideClasses != null ) {
            String[] forceOverride = overrideClasses.split( "," );
            for ( String classToOverride : forceOverride ) {
                try {
                    //Just loading the custom implementation will allows to override the one the classloader already had loaded
                    combinedLoader.loadClass( classToOverride.trim() );
                } catch ( ClassNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }

        //Use this new "combined" class loader
        Thread.currentThread().setContextClassLoader( combinedLoader );
    }

    /**
     * Unpublish this bundle elements
     */
    public void unpublishBundleServices () {

        //Get the current classloader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if ( classLoader instanceof CombinedLoader ) {

            //Try to remove this class loader
            ClassLoader felixClassLoader = getFelixClassLoader();
            ((CombinedLoader) classLoader).removeLoader( felixClassLoader );
        }
    }

    /**
     * Register a bundle library, this library must be a bundle inside the felix load folder.
     *
     * @param bundleJarFileName bundle file name
     * @throws Exception
     */
    public void registerBundleLibrary ( String bundleJarFileName ) throws Exception {

        //Felix directories
        String felixDirectory = Config.CONTEXT.getRealPath( File.separator + "WEB-INF" + File.separator + "felix" );
        String autoLoadDirectory = felixDirectory + File.separator + "load";

        //Adding the library to the application classpath
        addFileToClasspath( autoLoadDirectory + File.separator + bundleJarFileName );
    }

    /**
     * Adds a file to the classpath.
     *
     * @param filePath a String pointing to the file
     * @throws java.io.IOException
     */
    public void addFileToClasspath ( String filePath ) throws Exception {

        File fileToAdd = new File( filePath );
        addFileToClasspath( fileToAdd );
    }

    /**
     * Adds a file to the classpath
     *
     * @param toAdd the file to be added
     * @throws java.io.IOException
     */
    public void addFileToClasspath ( File toAdd ) throws Exception {

        addURLToApplicationClassLoader( toAdd.toURI().toURL() );
    }

    private void addURLToApplicationClassLoader ( URL url ) throws IntrospectionException {

        ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();

        // Create a ClassLoader using the given url
        URLClassLoader urlClassLoader = new URLClassLoader( new URL[]{url} );

        CombinedLoader combinedLoader;
        if ( currentThreadClassLoader instanceof CombinedLoader ) {
            combinedLoader = (CombinedLoader) currentThreadClassLoader;
            //Chain to the current thread classloader
            combinedLoader.addLoader( urlClassLoader );
        } else {
            combinedLoader = new CombinedLoader( Thread.currentThread().getContextClassLoader() );
            //Chain to the current thread classloader
            combinedLoader.addLoader( urlClassLoader );
        }

        //Use this new "combined" class loader
        Thread.currentThread().setContextClassLoader( combinedLoader );
    }

    private ClassLoader getFelixClassLoader () {
        return this.getClass().getClassLoader();
    }

    /**
     * Register a WorkFlowActionlet service
     *
     * @param context
     * @param actionlet
     */
    @SuppressWarnings ("unchecked")
    public void registerActionlet ( BundleContext context, WorkFlowActionlet actionlet ) {

        ServiceReference serviceRefSelected = context.getServiceReference( WorkflowAPIOsgiService.class.getName() );
        if ( serviceRefSelected == null ) {
            return;
        }

        if ( actionlets == null ) {
            actionlets = new ArrayList<WorkFlowActionlet>();
        }

        this.workflowOsgiService = (WorkflowAPIOsgiService) context.getService( serviceRefSelected );
        this.workflowOsgiService.addActionlet( actionlet.getClass() );
        actionlets.add( actionlet );

        Logger.info( this, "Added actionlet: " + actionlet.getName() );
    }

    /**
     * Unregister the registered WorkFlowActionlet services
     */
    public void unregisterActionlets () {

        if ( this.workflowOsgiService != null ) {
            for ( WorkFlowActionlet actionlet : actionlets ) {

                this.workflowOsgiService.removeActionlet( actionlet.getClass().getCanonicalName() );
                Logger.info( this, "Removed actionlet: " + actionlet.getName() );
            }
        }
    }

    /**
     * Register a ViewTool service using a ToolInfo object
     *
     * @param context
     * @param info
     */
    @SuppressWarnings ("unchecked")
    public void registerViewToolService ( BundleContext context, ToolInfo info ) {

        ServiceReference serviceRefSelected = context.getServiceReference( PrimitiveToolboxManager.class.getName() );
        if ( serviceRefSelected == null ) {

            /*//Forcing the loading of the ToolboxManager??
            //VelocityUtil.getEngine();
            VelocityUtil.getToolboxManager();
            serviceRefSelected = context.getServiceReference( PrimitiveToolboxManager.class.getName() );
            if ( serviceRefSelected == null ) {
                return;
            }*/

            return;
        }

        if ( viewTools == null ) {
            viewTools = new ArrayList<ToolInfo>();
        }

        this.toolboxManager = (PrimitiveToolboxManager) context.getService( serviceRefSelected );
        this.toolboxManager.addTool( info );
        viewTools.add( info );

        Logger.info( this, "Added View Tool: " + info.getKey() );
    }

    /**
     * Unregister the registered ViewTool services
     */
    public void unregisterViewToolServices () {

        if ( this.toolboxManager != null ) {
            for ( ToolInfo toolInfo : viewTools ) {

                this.toolboxManager.removeTool( toolInfo );
                Logger.info( this, "Removed View Tool: " + toolInfo.getKey() );
            }
        }
    }

    /**
     * Adds a hook to the end of the chain
     *
     * @param preHook
     * @throws Exception
     */
    public void addPreHook ( Object preHook ) throws Exception {

        Interceptor interceptor = (Interceptor) APILocator.getContentletAPIntercepter();
        interceptor.addPreHook( preHook );
    }

    /**
     * Adds a hook to the end of the chain
     *
     * @param postHook
     * @throws Exception
     */
    public void addPostHook ( Object postHook ) throws Exception {

        Interceptor interceptor = (Interceptor) APILocator.getContentletAPIntercepter();
        interceptor.addPostHook( postHook );
    }

}