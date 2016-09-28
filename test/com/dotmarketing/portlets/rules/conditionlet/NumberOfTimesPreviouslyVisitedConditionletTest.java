package com.dotmarketing.portlets.rules.conditionlet;

import com.dotcms.LicenseTestUtil;
import com.dotmarketing.portlets.rules.model.ParameterModel;
import com.dotmarketing.util.CookieUtil;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static com.dotmarketing.portlets.rules.parameter.comparison.Comparison.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by freddyrodriguez on 10/3/16.
 */
public class NumberOfTimesPreviouslyVisitedConditionletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession httpSessionMock;
    private NumberOfTimesPreviouslyVisitedConditionlet conditionlet = new NumberOfTimesPreviouslyVisitedConditionlet();

    @BeforeAll
    public static void prepare () throws Exception {
        LicenseTestUtil.getLicense();
    }

    @BeforeEach
    public void before () {
        // Mock the request
        request = Mockito.mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[1];
        cookies[ 0 ] = CookieUtil.createSiteVisitsCookie();
        cookies[ 0 ].setValue("3");
        Mockito.when(request.getCookies()).thenReturn(cookies);

        // Mock the response
        response = Mockito.mock(HttpServletResponse.class);

        //Mock the session
        httpSessionMock = Mockito.mock(HttpSession.class);
        Mockito.when(request.getSession()).thenReturn(httpSessionMock);

    }

    @Test
    public void testEvaluateEquals() {

        Map<String, ParameterModel> parameters = new HashMap<>();
        parameters.put(Conditionlet.COMPARISON_KEY, new ParameterModel(Conditionlet.COMPARISON_KEY, EQUAL.getId()));
        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "2"));

        NumberOfTimesPreviouslyVisitedConditionlet.Instance instance = conditionlet.instanceFrom(parameters);

        assertTrue(conditionlet.evaluate(request, response, instance));

        //---------------------------------------------------------------------------------------------------------

        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "3"));

        instance = conditionlet.instanceFrom(parameters);

        assertFalse(conditionlet.evaluate(request, response, instance));
    }

    @Test
    public void testEvaluateLessThan() {

        Map<String, ParameterModel> parameters = new HashMap<>();
        parameters.put(Conditionlet.COMPARISON_KEY, new ParameterModel(Conditionlet.COMPARISON_KEY, LESS_THAN.getId()));
        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "3"));

        NumberOfTimesPreviouslyVisitedConditionlet.Instance instance = conditionlet.instanceFrom(parameters);

        assertTrue(conditionlet.evaluate(request, response, instance));

        //---------------------------------------------------------------------------------------------------------

        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "1"));

        instance = conditionlet.instanceFrom(parameters);

        assertFalse(conditionlet.evaluate(request, response, instance));

        //---------------------------------------------------------------------------------------------------------

        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "2"));

        instance = conditionlet.instanceFrom(parameters);

        assertFalse(conditionlet.evaluate(request, response, instance));
    }

    @Test
    public void testEvaluateGreaterThan() {

        Map<String, ParameterModel> parameters = new HashMap<>();
        parameters.put(Conditionlet.COMPARISON_KEY, new ParameterModel(Conditionlet.COMPARISON_KEY, GREATER_THAN.getId()));
        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "1"));

        NumberOfTimesPreviouslyVisitedConditionlet.Instance instance = conditionlet.instanceFrom(parameters);

        assertTrue(conditionlet.evaluate(request, response, instance));

        //---------------------------------------------------------------------------------------------------------

        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "3"));

        instance = conditionlet.instanceFrom(parameters);

        assertFalse(conditionlet.evaluate(request, response, instance));

        //---------------------------------------------------------------------------------------------------------

        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "2"));

        instance = conditionlet.instanceFrom(parameters);

        assertFalse(conditionlet.evaluate(request, response, instance));
    }


    @Test
    public void testEvaluateLessOrEqualThan() {

        Map<String, ParameterModel> parameters = new HashMap<>();
        parameters.put(Conditionlet.COMPARISON_KEY, new ParameterModel(Conditionlet.COMPARISON_KEY, LESS_THAN_OR_EQUAL.getId()));
        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "3"));

        NumberOfTimesPreviouslyVisitedConditionlet.Instance instance = conditionlet.instanceFrom(parameters);

        assertTrue(conditionlet.evaluate(request, response, instance));

        //---------------------------------------------------------------------------------------------------------

        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "1"));

        instance = conditionlet.instanceFrom(parameters);

        assertFalse(conditionlet.evaluate(request, response, instance));

        //---------------------------------------------------------------------------------------------------------

        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "2"));

        instance = conditionlet.instanceFrom(parameters);

        assertTrue(conditionlet.evaluate(request, response, instance));
    }

    @Test
    public void testEvaluateGreaterOrEqualThan() {

        Map<String, ParameterModel> parameters = new HashMap<>();
        parameters.put(Conditionlet.COMPARISON_KEY, new ParameterModel(Conditionlet.COMPARISON_KEY, GREATER_THAN_OR_EQUAL.getId()));
        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "1"));

        NumberOfTimesPreviouslyVisitedConditionlet.Instance instance = conditionlet.instanceFrom(parameters);

        assertTrue(conditionlet.evaluate(request, response, instance));

        //---------------------------------------------------------------------------------------------------------

        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "3"));

        instance = conditionlet.instanceFrom(parameters);

        assertFalse(conditionlet.evaluate(request, response, instance));

        //---------------------------------------------------------------------------------------------------------

        parameters.put(UsersSiteVisitsConditionlet.SITE_VISITS_KEY,
                new ParameterModel(UsersSiteVisitsConditionlet.SITE_VISITS_KEY, "2"));

        instance = conditionlet.instanceFrom(parameters);

        assertTrue(conditionlet.evaluate(request, response, instance));
    }
}
