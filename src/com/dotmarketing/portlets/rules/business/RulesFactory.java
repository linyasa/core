package com.dotmarketing.portlets.rules.business;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dotmarketing.business.Treeable;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.portlets.rules.model.Condition;
import com.dotmarketing.portlets.rules.model.ConditionGroup;
import com.dotmarketing.portlets.rules.model.ParameterModel;
import com.dotmarketing.portlets.rules.model.Rule;
import com.dotmarketing.portlets.rules.model.RuleAction;

public interface RulesFactory {

    List<Rule> getEnabledRulesByParent(Treeable host) throws DotDataException;

    List<Rule> getAllRulesByParent(Treeable host) throws DotDataException;

    Set<Rule> getRulesByParent(String host, Rule.FireOn fireOn) throws DotDataException;

    List<Rule> getRulesByNameFilter(String nameFilter);

    Rule getRuleById(String id) throws DotDataException;

    List<RuleAction> getRuleActionsByRule(String ruleId) throws DotDataException;

    RuleAction getRuleActionById(String ruleActionId) throws DotDataException;

    ParameterModel getRuleActionParameterById(String id) throws DotDataException;

    List<ConditionGroup> getConditionGroupsByRule(String ruleId) throws DotDataException;

    ConditionGroup getConditionGroupById(String conditionGroupId) throws DotDataException;

    List<Condition> getConditionsByGroup(String groupId) throws DotDataException;

    Condition getConditionById(String id) throws DotDataException ;

    ParameterModel getConditionValueById(String id) throws DotDataException;

    void saveRule(Rule rule) throws DotDataException;

    void saveConditionGroup(ConditionGroup condition) throws DotDataException;

    void saveCondition(Condition condition) throws DotDataException;

    void saveConditionValue(ParameterModel parameterModel) throws DotDataException;

    void saveRuleAction(RuleAction ruleAction) throws DotDataException;

    void deleteRule(Rule rule) throws DotDataException;

    void deleteConditionGroup(ConditionGroup conditionGroup) throws DotDataException;

    void deleteConditionsByGroup(ConditionGroup conditionGroup) throws DotDataException;

    void deleteCondition(Condition condition) throws DotDataException;

    void deleteConditionValue(ParameterModel parameterModel) throws DotDataException;

    void deleteRuleAction(RuleAction ruleAction) throws DotDataException;

    void deleteRuleActionsByRule(Rule rule) throws DotDataException;

    void deleteRuleActionsParameters(RuleAction action) throws DotDataException;

    void deleteConditionValues(Condition condition) throws DotDataException;

    Map<String, ParameterModel> getRuleActionParameters(RuleAction action) throws DotDataException;

	List<Rule> getAllRules() throws DotDataException;

}
