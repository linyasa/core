package com.dotmarketing.portlets.rules.conditionlet;

import com.dotcms.repackage.com.google.common.collect.Lists;
import com.dotcms.repackage.com.google.common.collect.Maps;
import com.dotcms.visitor.business.VisitorAPI;
import com.dotcms.visitor.domain.Visitor;
import com.dotmarketing.business.UserAPI;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.personas.business.PersonaAPI;
import com.dotmarketing.portlets.personas.model.Persona;
import com.dotmarketing.portlets.rules.exception.ComparisonNotSupportedException;
import com.dotmarketing.portlets.rules.model.ParameterModel;
import com.dotmarketing.portlets.rules.parameter.comparison.Comparison;
import com.liferay.portal.model.User;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.dotmarketing.portlets.rules.actionlet.PersonaActionlet.PERSONA_ID_KEY;
import static com.dotmarketing.portlets.rules.conditionlet.Conditionlet.COMPARISON_KEY;
import static com.dotmarketing.portlets.rules.parameter.comparison.Comparison.EXISTS;
import static com.dotmarketing.portlets.rules.parameter.comparison.Comparison.IS;
import static com.dotmarketing.portlets.rules.parameter.comparison.Comparison.IS_NOT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonaConditionletTest {

    private List<TestCase> getCases() throws Exception {
        try {
            List<TestCase> data = Lists.newArrayList();

            Persona investor = new Persona(new Contentlet());
            investor.setIdentifier("investor");
            investor.setInode("investor");

            Persona student = new Persona(new Contentlet());
            student.setIdentifier("student");
            student.setInode("student");

            /* IS */

            data.add(new TestCase("When current persona is 'Student' and given persona is 'Investor' and using 'IS' comparator Should return False")
                    .withComparison(IS)
                    .withInputPersona(investor)
                    .withCurrentPersona(student)
                    .shouldBeFalse()
            );

            data.add(new TestCase("When current persona is 'Student' and given persona is 'Student' and using 'IS' comparator Should return True")
                    .withComparison(IS)
                    .withInputPersona(student)
                    .withCurrentPersona(student)
                    .shouldBeTrue()
            );

            /* IS NOT */

            data.add(new TestCase("When current persona is 'Student' and given persona is 'Investor' and using 'IS NOT' comparator Should return True")
                    .withComparison(IS_NOT)
                    .withInputPersona(investor)
                    .withCurrentPersona(student)
                    .shouldBeTrue()
            );

            data.add(new TestCase("When current persona is 'Investor' and given persona is 'Investor' and using 'IS NOT' comparator Should return False")
                    .withComparison(IS_NOT)
                    .withInputPersona(investor)
                    .withCurrentPersona(investor)
                    .shouldBeFalse()
            );

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
        PersonaConditionlet conditionlet = new PersonaConditionlet(aCase.personaAPI, aCase.userAPI, aCase.visitorAPI);
        return conditionlet.evaluate(aCase.request, aCase.response, conditionlet.instanceFrom(aCase.params));
    }

    @Test
    public void testEvaluatesToFalseWhenArgumentsAreEmptyOrMissing() throws Exception {
        TestCase test = new TestCase();
        PersonaConditionlet conditionlet = new PersonaConditionlet(test.personaAPI, test.userAPI, test.visitorAPI);
        assertThrows(IllegalStateException.class, () -> conditionlet.instanceFrom(null));
    }

    @Test
    public void testCannotValidateWhenComparisonIsNull() throws Exception {
        TestCase aCase = new TestCase().withComparison(null);
        PersonaConditionlet conditionlet = new PersonaConditionlet(aCase.personaAPI, aCase.userAPI, aCase.visitorAPI);
        assertThrows(IllegalStateException.class, () -> conditionlet.instanceFrom(aCase.params));
    }

    @Test
    public void testCannotValidateWhenComparisonNotSet() throws Exception {
        TestCase test = new TestCase();
        PersonaConditionlet conditionlet = new PersonaConditionlet(test.personaAPI, test.userAPI, test.visitorAPI);
        assertThrows(IllegalStateException.class, () -> conditionlet.instanceFrom(Maps.newHashMap()));
    }

    @Test
    public void testUnsupportedComparisonThrowsException() throws Exception {
        TestCase aCase = new TestCase();
        Persona any = new Persona(new Contentlet());
        aCase.withComparison(EXISTS).withCurrentPersona(any).withInputPersona(any);
        assertThrows(ComparisonNotSupportedException.class, () -> runCase(aCase));
    }

    private class TestCase {

        private final User user = new User("000000");
        private final HttpServletRequest request = mock(HttpServletRequest.class);
        private final HttpServletResponse response= mock(HttpServletResponse.class);
        private PersonaAPI personaAPI = mock(PersonaAPI.class);
        private UserAPI userAPI = mock(UserAPI.class);
        private VisitorAPI visitorAPI = mock(VisitorAPI.class);
        private Map<String, ParameterModel> params = Maps.newHashMap();
        private final String testDescription;

        private boolean expect;

        public TestCase() {
            this("");
        }

        public TestCase(String testDescription) {
            this.testDescription = testDescription;
        }

        TestCase shouldBeTrue() {
            this.expect = true;
            return this;
        }

        TestCase shouldBeFalse() {
            this.expect = false;
            return this;
        }

        TestCase withComparison(Comparison c) {
            params.put(COMPARISON_KEY, new ParameterModel(COMPARISON_KEY, c != null ? c.getId() : null));
            return this;
        }

        public TestCase withInputPersona(Persona persona){
            params.put(PERSONA_ID_KEY, new ParameterModel(PERSONA_ID_KEY, persona.getIdentifier()));
            try {
                when(userAPI.getSystemUser()).thenReturn(user);
                when(personaAPI.find(persona.getIdentifier(), user, false)).thenReturn(persona);
            } catch (DotDataException | DotSecurityException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public TestCase withCurrentPersona(Persona persona){
            Visitor visitor = mock(Visitor.class);
            when(visitor.getPersona()).thenReturn(persona);
            when(visitorAPI.getVisitor(request)).thenReturn(Optional.of(visitor));
            return this;
        }
    }
}
