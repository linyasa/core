package com.dotmarketing.util.jwt;


import com.dotmarketing.util.json.JSONException;
import com.dotmarketing.util.json.JSONObject;
import com.dotmarketing.util.marshal.MarshalFactory;
import com.dotmarketing.util.marshal.MarshalUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * JsonWebTokenService
 * Test
 * @author jsanca
 */

public class JsonWebTokenServiceTest {



    /**
     * Testing the generateToken JsonWebTokenServiceTest
     */
    @Test
    public void generateTokenTest() throws ParseException, JSONException {

        final SimpleDateFormat dateFormat =
                new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-8:00"));
        dateFormat.setLenient(true);

        final JsonWebTokenService jsonWebTokenService =
                JsonWebTokenFactory.getInstance().getJsonWebTokenService();

        assertNotNull(jsonWebTokenService);

        final MarshalFactory marshalFactory =
                MarshalFactory.getInstance();

        assertNotNull(marshalFactory);

        final MarshalUtils marshalUtils =
                marshalFactory.getMarshalUtils();

        assertNotNull(marshalUtils);

        final Date date = dateFormat.parse("04/10/1981");

        final String jsonWebTokenSubject = marshalUtils.marshal(
                new DotCMSSubjectBean(date, "jsanca", "myCompany")
        );

        System.out.println(jsonWebTokenSubject);

        assertNotNull(jsonWebTokenSubject);
        assertTrue(
                new JSONObject("{\"userId\":\"jsanca\",\"lastModified\":371030400000, \"companyId\":\"myCompany\"}").toString().equals
                        (new JSONObject(jsonWebTokenSubject).toString())
        );

        String jsonWebToken = jsonWebTokenService.generateToken(new JWTBean("jwt1",
                jsonWebTokenSubject, "jsanca", date.getTime()
                ));

        System.out.println(jsonWebToken);

        assertNotNull(jsonWebToken);
        assertTrue(jsonWebToken.startsWith("eyJhbGciOiJIUzI1NiJ9"));
    }

}
