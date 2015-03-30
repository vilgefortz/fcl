package main.application.rules;

import java.util.List;
import java.util.logging.Logger;

import main.application.functionblock.Ruleblock;
import main.application.variables.InlineVariableNotFoundException;
import main.application.variables.InputVariableNotFoundException;
import main.application.variables.OutputVariableNotFoundException;


public class DefaultRuleFactory {

	private Ruleblock ruleblock;
	private DefaultActionFactory actionFactory;
	private DefaultEffectFactory effectFactory;
	public DefaultRuleFactory(Ruleblock rb) {		
		this.ruleblock=rb;
		this.actionFactory = new DefaultActionFactory(this.ruleblock);
		this.effectFactory = new DefaultEffectFactory(this.ruleblock);
	}
	public Rule fromString(String name, String rule) throws RuleParsingException, InlineVariableNotFoundException, InputVariableNotFoundException, OutputVariableNotFoundException {
		Rule r = new Rule (name,rule); 
		
		//remove semicolon from end;
		rule = rule.toLowerCase().trim();
		rule = rule.replaceAll(";", "");
		int start = rule.indexOf("if");
		int thenpos = rule.indexOf("then");
		if (start<0) throw new RuleParsingException("You are missing keyword 'if' in your rule declaration");
		if (start != 0) throw new RuleParsingException("Rule definition must start with keyword 'if'");
		if (thenpos<0) throw new RuleParsingException("You are missing keyword 'then' in your rule declaration");
		r.cause = this.parseCause (rule.substring(start+2, thenpos),r);
		r.effect.add (this.parseEffect (rule.substring(thenpos+4),r,r.effect));
		return r;
	}

	private Effect parseEffect(String text, Rule r, List<Effect> effect) throws OutputVariableNotFoundException, RuleParsingException {

		Effect e = this.effectFactory.createEffect(text,r,effect);
		return e;
	}

	private Cause parseCause(String text, Rule r) throws RuleParsingException, InlineVariableNotFoundException, InputVariableNotFoundException {
		
		Cause c = new Cause ();
		c.action = this.actionFactory.createAction(text,r);
		Logger.getGlobal().info("ACTION : " + c.action.toString());
		return c;
	}

	


}
