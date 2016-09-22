package com.dotcms;

import com.dotmarketing.util.ConfigTestHelper;

import org.junit.jupiter.api.BeforeAll;


/**
 * Created by nollymar on 9/22/16.
 */
public class UnitTestBase {

    @BeforeAll
    public static void setUp() throws Exception {
        ConfigTestHelper._setupFakeTestingContext();
    }
}
