package com.dotmarketing.portlets.contentlet.business;

import com.dotcms.LicenseTestUtil;
import com.dotcms.enterprise.HostAssetsJobProxy;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.db.HibernateUtil;
import com.dotmarketing.quartz.QuartzUtils;
import com.dotmarketing.quartz.SimpleScheduledTask;
import com.dotmarketing.quartz.job.HostCopyOptions;
import com.dotmarketing.util.Logger;
import com.liferay.portal.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.quartz.SimpleTrigger;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class will test operations related with interacting with hosts: Deleting
 * a host, marking a host as default, etc.
 * 
 * @author Jorge Urdaneta
 * @since Sep 5, 2013
 *
 */
public class HostAPITest {
	
	@BeforeEach
    public void prepare() throws Exception {
        LicenseTestUtil.getLicense();
    }

	@Disabled("Temporarily ignore this test method")
    @Test
    public void testDeleteHost() throws Exception {
        User user=APILocator.getUserAPI().getSystemUser();
        
        Host source=APILocator.getHostAPI().findByName("demo.dotcms.com", user, false);
        
        Host host=new Host();
        host.setHostname("copy"+System.currentTimeMillis()+".demo.dotcms.com");
        host.setDefault(false);
        try{
        	HibernateUtil.startTransaction();
        	host=APILocator.getHostAPI().save(host, user, false);
        	HibernateUtil.commitTransaction();
        }catch(Exception e){
        	HibernateUtil.rollbackTransaction();
        	Logger.error(HostAPITest.class, e.getMessage());
        }
        APILocator.getContentletAPI().isInodeIndexed(host.getInode());
        Thread.sleep(5000);
        String hostIdent=host.getIdentifier();
        String hostName=host.getHostname();
        
        HostCopyOptions options=new HostCopyOptions(true);
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("sourceHostId", source.getIdentifier());
        parameters.put("destinationHostId", host.getIdentifier());
        parameters.put("copyOptions", options);
        
        Calendar startTime = Calendar.getInstance();
        SimpleScheduledTask task = new SimpleScheduledTask("setup-host-" + host.getIdentifier(), "setup-host-group", "Setups host "
                + host.getIdentifier() + " from host " + source.getIdentifier(), HostAssetsJobProxy.class.getCanonicalName(), false,
                "setup-host-" + source.getIdentifier() + "-trigger", "setup-host-trigger-group", startTime.getTime(), null,
                SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT, 5, true, parameters, 0, 0);
        
        QuartzUtils.scheduleTask(task);
        
        // wait for the copy to be done. 
        //#6084: If the license is not Enterprise it should NOT get stuck.
        //It will wait for 15 minutes only. 
        int milliseconds = 0;
        int maxMilliseconds = 900000; //15 Minutes
        
        while(QuartzUtils.getTaskProgress(task.getJobName(), task.getJobGroup())<100 && milliseconds < maxMilliseconds) {
            Thread.sleep(500);
            milliseconds += 500;
        }
        
        if (QuartzUtils.getTaskProgress(task.getJobName(), task.getJobGroup()) < 0) {
        	fail("testDeleteHost The host copy task did not start");
        }
        
        if(milliseconds >= maxMilliseconds){
        	fail("testDeleteHost is stuck waiting for QuartzUtils.scheduleTask");
        }
        
        Thread.sleep(600); // wait a bit for the index
        
        try{
        	HibernateUtil.startTransaction();
        	APILocator.getHostAPI().archive(host, user, false);
        	APILocator.getHostAPI().delete(host, user, false);
        	HibernateUtil.commitTransaction();
        }catch(Exception e){
        	HibernateUtil.rollbackTransaction();
        	Logger.error(HostAPITest.class, e.getMessage());
        }

        Thread.sleep(600); // wait a bit for the index
        
        host = APILocator.getHostAPI().find(hostIdent, user, false);
        
        if(host!=null){
        	APILocator.getHostAPI().delete(host, user, false);
        	Thread.sleep(10000);
        }
        
        host = APILocator.getHostAPI().find(hostIdent, user, false);
        assertNull(host);
        
        host = APILocator.getHostAPI().findByName(hostName, user, false);
        
        assertNull(host);
    }
    
    @Test
    public void makeDefault() throws Exception {
    	User user=APILocator.getUserAPI().getSystemUser();
    	/*
    	 * Get the current Default host
    	 */
    	Host hdef = APILocator.getHostAPI().findDefaultHost(user, false);
    	
    	/*
    	 * Create a new Host and make it default
    	 */
    	Host host=new Host();
        host.setHostname("test"+System.currentTimeMillis()+".demo.dotcms.com");
        host.setDefault(false);
        try{
        	HibernateUtil.startTransaction();
        	host=APILocator.getHostAPI().save(host, user, false);
        	HibernateUtil.commitTransaction();
        }catch(Exception e){
        	HibernateUtil.rollbackTransaction();
        	Logger.error(HostAPITest.class, e.getMessage());
        }
        APILocator.getHostAPI().publish(host, user, false);
        APILocator.getHostAPI().makeDefault(host, user, false);
        
        host = APILocator.getHostAPI().find(host.getIdentifier(), user, false);
        APILocator.getContentletAPI().isInodeIndexed(host.getInode());
        APILocator.getContentletAPI().isInodeIndexed(host.getInode(),true);
        hdef = APILocator.getHostAPI().find(hdef.getIdentifier(), user, false);
        APILocator.getContentletAPI().isInodeIndexed(hdef.getInode());
        APILocator.getContentletAPI().isInodeIndexed(hdef.getInode(),true);
        
        /*
         * Validate if the previous default host. Is live and not default
         */
        assertTrue(hdef.isLive());
        assertFalse(hdef.isDefault());
        
        /*
         * get Back to default the previous host
         */
        APILocator.getHostAPI().makeDefault(hdef, user, false);
        
        host = APILocator.getHostAPI().find(host.getIdentifier(), user, false);
        APILocator.getContentletAPI().isInodeIndexed(host.getInode());
        APILocator.getContentletAPI().isInodeIndexed(host.getInode(),true);
        hdef = APILocator.getHostAPI().find(hdef.getIdentifier(), user, false);
        APILocator.getContentletAPI().isInodeIndexed(hdef.getInode());
        APILocator.getContentletAPI().isInodeIndexed(hdef.getInode(),true);
        
        /*
         * Validate if the new host is not default anymore and if its live
         */
        assertFalse(host.isDefault());
        assertTrue(host.isLive());
        
        assertTrue(hdef.isLive());
        assertTrue(hdef.isDefault());
        
        /*
         * Delete the new test host
         */
        Thread.sleep(600); // wait a bit for the index
        try{
        	HibernateUtil.startTransaction();
        	APILocator.getHostAPI().archive(host, user, false);
        	APILocator.getHostAPI().delete(host, user, false);
        	HibernateUtil.commitTransaction();
        }catch(Exception e){
        	HibernateUtil.rollbackTransaction();
        	Logger.error(HostAPITest.class, e.getMessage());
        }
        Thread.sleep(600); // wait a bit for the index
        /*
         * Validate if the current Original default host is the current default one
         */
        host = APILocator.getHostAPI().findDefaultHost(user, false);
        assertEquals(hdef.getIdentifier(), host.getIdentifier());
    }
}
