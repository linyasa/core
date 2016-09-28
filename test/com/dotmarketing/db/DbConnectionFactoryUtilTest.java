package com.dotmarketing.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DbConnectionFactoryUtilTest {
    
    @BeforeAll
    public static void init() throws Exception {

    }
    
    @AfterEach
    @BeforeEach
    public void prep() throws Exception {
        HibernateUtil.closeSession();
    }
    
    /**
     * This test is for the inTransaction method
     * of the DBConnectionFacotry - and returns if we are in a session or not
     */
    @Test
    public void testInTransaction() throws Exception {
        assertFalse(DbConnectionFactory.inTransaction() );

    	HibernateUtil.startTransaction();
        assertTrue( DbConnectionFactory.inTransaction());
    	HibernateUtil.commitTransaction();
        assertFalse(DbConnectionFactory.inTransaction() );

    	DbConnectionFactory.getConnection();

        assertFalse(DbConnectionFactory.inTransaction() );
        HibernateUtil.closeSession();
    }
}
