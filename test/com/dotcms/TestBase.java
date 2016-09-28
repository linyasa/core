package com.dotcms;

import com.dotcms.repackage.net.sf.hibernate.HibernateException;
import com.dotmarketing.db.HibernateUtil;
import com.dotmarketing.exception.DotHibernateException;

import org.junit.jupiter.api.AfterEach;

import java.sql.SQLException;

/**
 * Created by Jonathan Gamba.
 * Date: 3/6/12
 * Time: 4:36 PM
 * <p/>
 * Annotations that can be use: {@link org.junit.jupiter.api.BeforeAll @BeforeAll}, {@link org.junit.jupiter.api.BeforeEach @BeforeEach},
 * {@link org.junit.jupiter.api.Test @Test}, {@link org.junit.jupiter.api.AfterAll @AfterAll},
 * {@link org.junit.jupiter.api.AfterEach @AfterEach}, {@link org.junit.jupiter.api.Disabled @Disabled}
 * <br>For managing the assertions use the static class {@link org.junit.jupiter.api.Assertions @Assertions}
 */
public abstract class TestBase {

    @AfterEach
    public void after () throws SQLException, DotHibernateException, HibernateException {

        //Closing the session
        HibernateUtil.getSession().connection().close();
        HibernateUtil.getSession().close();
    }

}