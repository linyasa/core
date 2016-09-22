package com.dotmarketing.portlets.rules.conditionlet;

import com.dotcms.repackage.com.google.common.collect.Lists;
import com.dotcms.repackage.com.google.common.collect.Maps;
import com.dotmarketing.portlets.rules.model.ParameterModel;
import com.dotmarketing.portlets.rules.parameter.comparison.Comparison;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.dotmarketing.portlets.rules.conditionlet.Conditionlet.COMPARISON_KEY;
import static com.dotmarketing.portlets.rules.conditionlet.SessionAttributeConditionlet.SESSION_KEY;
import static com.dotmarketing.portlets.rules.conditionlet.SessionAttributeConditionlet.SESSION_VALUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionAttributeConditionletTest {

    private SessionAttributeConditionlet conditionlet;

    @BeforeEach
    public void setUp() throws Exception {
        conditionlet = new SessionAttributeConditionlet();
    }

    private List<TestCase> getCases() {
        List<TestCase> data = Lists.newArrayList();

        data.add(new TestCase("Comparison 'Exists' should eval true for session attribute value == '' and user value == ''.")
                 .withComparison(Comparison.EXISTS)
                 .withMockedActualValue("key", "")
                 .withAttribute("key", "")
                 .shouldBeTrue()
        );
        data.add(new TestCase("Comparison 'Exists' should eval false for session attribute value == null and user value == ''.")
                     .withComparison(Comparison.EXISTS)
                     .withMockedActualValue("key", null)
                     .withAttribute("key", "")
                     .shouldBeFalse()

        );
        /* Is */
        data.add(new TestCase("Comparison 'Is' should eval true for session attribute value == '' and user value == ''.")
                     .withComparison(Comparison.IS)
                     .withMockedActualValue("key", "")
                     .withAttribute("key", "")
                     .shouldBeTrue()
        );

        data.add(new TestCase("Comparison 'Is' should eval true for session attribute value == '1' and user value == '1'.")
                     .withComparison(Comparison.IS)
                     .withMockedActualValue("key", "")
                     .withAttribute("key", "")
                     .shouldBeTrue()
        );

        data.add(new TestCase("Comparison 'Is' should eval false for session attribute value == '1' and user value == ''.")
                     .withComparison(Comparison.IS)
                     .withMockedActualValue("key", "1")
                     .withAttribute("key", "")
                     .shouldBeFalse()
        );

        data.add(new TestCase("Comparison 'Is' should not be case sensitive - should eval true for session attribute value == 'One' and user value == 'one'.")
                     .withComparison(Comparison.IS)
                     .withMockedActualValue("key", "One")
                     .withAttribute("key", "one")
                     .shouldBeTrue()
        );
        /* Is Not */
        data.add(new TestCase("Comparison 'Is Not' should eval false for session attribute value == '' and user value == ''.")
                     .withComparison(Comparison.IS_NOT)
                     .withMockedActualValue("key", "")
                     .withAttribute("key", "")
                     .shouldBeFalse()
        );

        data.add(new TestCase("Comparison 'Is Not' should eval false for session attribute value == '1' and user value == '1'.")
                     .withComparison(Comparison.IS_NOT)
                     .withMockedActualValue("key", "")
                     .withAttribute("key", "")
                     .shouldBeFalse()
        );

        data.add(new TestCase("Comparison 'Is Not' should eval true for session attribute value == '1' and user value == ''.")
                     .withComparison(Comparison.IS_NOT)
                     .withMockedActualValue("key", "1")
                     .withAttribute("key", "")
                     .shouldBeTrue()
        );

        data.add(new TestCase("Comparison 'Is Not' should eval true for session attribute value == null and user value == 'any'.")
                     .withComparison(Comparison.IS_NOT)
                     .withMockedActualValue("key", null)
                     .withAttribute("key", "any")
                     .shouldBeFalse()
        );

        data.add(new TestCase("Comparison 'Is Not' should not be case sensitive - should eval false for session attribute value == 'One' and user value == 'one'.")
                     .withComparison(Comparison.IS_NOT)
                     .withMockedActualValue("key", "One")
                     .withAttribute("key", "one")
                     .shouldBeFalse()
        );
        /* Starts With  */
        String mockedActual;
        String userValue;
        String description;
        mockedActual = "The Quick Brown Fox";
        userValue = "the quick";
        description = String.format("Comparison 'Starts With' should eval true for session attribute value == '%s' and user value == '%s'.", mockedActual, userValue);
        data.add(new TestCase(description)
                     .withComparison(Comparison.STARTS_WITH)
                     .withMockedActualValue("key", mockedActual)
                     .withAttribute("key", userValue)
                     .shouldBeTrue()
        );
        mockedActual = "The Quick Brown Fox";
        userValue = "A quick";
        description = String.format("Comparison 'Starts With' should eval false for session attribute value == '%s' and user value == '%s'.", mockedActual, userValue);
        data.add(new TestCase(description)
                     .withComparison(Comparison.STARTS_WITH)
                     .withMockedActualValue("key", mockedActual)
                     .withAttribute("key", userValue)
                     .shouldBeFalse()
        );
        /* Ends With */
        mockedActual = "The Quick Brown Fox";
        userValue = "Brown fox";
        description = String.format("Comparison 'Ends With' should eval true for session attribute value == '%s' and user value == '%s'.", mockedActual, userValue);
        data.add(new TestCase(description)
                     .withComparison(Comparison.ENDS_WITH)
                     .withMockedActualValue("key", mockedActual)
                     .withAttribute("key", userValue)
                     .shouldBeTrue()
        );
        mockedActual = "The Quick Brown Fox";
        userValue = "foxy";
        description = String.format("Comparison 'Ends With' should eval false for session attribute value == '%s' and user value == '%s'.", mockedActual, userValue);
        data.add(new TestCase(description)
                     .withComparison(Comparison.ENDS_WITH)
                     .withMockedActualValue("key", mockedActual)
                     .withAttribute("key", userValue)
                     .shouldBeFalse()
        );
        /* Contains */
        mockedActual = "The Quick Brown Fox";
        userValue = "quick brown";
        description = String.format("Comparison 'Contains' should eval true for session attribute value == '%s' and user value == '%s'.", mockedActual, userValue);
        data.add(new TestCase(description)
                     .withComparison(Comparison.CONTAINS)
                     .withMockedActualValue("key", mockedActual)
                     .withAttribute("key", userValue)
                     .shouldBeTrue()
        );
        mockedActual = "The Quick Brown Fox";
        userValue = "bob";
        description = String.format("Comparison 'Ends With' should eval false for session attribute value == '%s' and user value == '%s'.", mockedActual, userValue);
        data.add(new TestCase(description)
                     .withComparison(Comparison.CONTAINS)
                     .withMockedActualValue("key", mockedActual)
                     .withAttribute("key", userValue)
                     .shouldBeFalse()
        );
        /* Matches Regex */
        mockedActual = "The Quick Brown Fox";
        userValue = ".*Quick.*Fox";
        description = String.format("Comparison 'Regex' should eval true for session attribute value == '%s' and user value == '%s'.", mockedActual, userValue);
        data.add(new TestCase(description)
                     .withComparison(Comparison.REGEX)
                     .withMockedActualValue("key", mockedActual)
                     .withAttribute("key", userValue)
                     .shouldBeTrue()
        );

        return data;
    }

    @TestFactory
    public Stream<DynamicTest> testComparisonsFactory() throws Exception {
        List<TestCase> testData = getCases();
        return testData.stream()
            .map(datum -> DynamicTest.dynamicTest(
                "Testing " + datum,
                () -> testComparisons(datum)));
    }

    private void testComparisons(TestCase aCase) throws Exception {
        assertThat(aCase.testDescription, runCase(aCase), is(aCase.expect));
    }

    private boolean runCase(TestCase aCase) {
        return conditionlet.evaluate(aCase.request, aCase.response, conditionlet.instanceFrom(aCase.params));
    }

    private class TestCase {

        private final HttpServletRequest request;
        private final HttpServletResponse response;
        private final HttpSession stubSession;
        private final Map<String, ParameterModel> params = Maps.newLinkedHashMap();
        private final String testDescription;
        private boolean expect;


        public TestCase(String testDescription) {
            this.testDescription = testDescription;
            this.request = mock(HttpServletRequest.class);
            this.response = mock(HttpServletResponse.class);
            this.stubSession = mock(HttpSession.class);
        }

        TestCase shouldBeTrue(){
            this.expect = true;
            return this;
        }

        TestCase shouldBeFalse() {
            this.expect = false;
            return this;
        }

        TestCase withComparison(Comparison c){
            params.put(COMPARISON_KEY, new ParameterModel(COMPARISON_KEY, c != null ? c.getId() : null));
            return this;
        }

        TestCase withAttribute(String key, String value) {
            params.put(SESSION_KEY, new ParameterModel(SESSION_KEY, key));
            params.put(SESSION_VALUE, new ParameterModel(SESSION_VALUE, value));
            return this;
        }

        TestCase withMockedActualValue(String key, String value){
            when(this.request.getSession()).thenReturn(stubSession);
            when(this.request.getSession().getAttribute(key)).thenReturn(value);
            return this;
        }

        @Override
        public String toString() {
            return testDescription;
        }
    }

}