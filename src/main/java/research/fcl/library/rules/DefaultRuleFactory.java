package research.fcl.library.rules;

import java.util.List;
import java.util.logging.Logger;

import research.fcl.library.rules.cause.Cause;
import research.fcl.library.rules.effect.Effect;
import research.fcl.library.rules.modifiers.ModifierFactory;
import research.fcl.library.variables.exceptions.InlineVariableNotFoundException;
import research.fcl.library.variables.exceptions.InputVariableNotFoundException;
import research.fcl.library.variables.exceptions.OutputVariableNotFoundException;
import research.fcl.library.variables.exceptions.TermNotFoundException;


public class DefaultRuleFactory extends RuleFactory{

	public DefaultRuleFactory(Ruleblock rb) {
		super(rb);
	}
	public Rule fromString(String name, String rule) throws RuleParsingException, InlineVariableNotFoundException, InputVariableNotFoundException, OutputVariableNotFoundException, TermNotFoundException {
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

	private Effect parseEffect(String text, Rule r, List<Effect> effect) throws OutputVariableNotFoundException, RuleParsingException, TermNotFoundException {

		Effect e = this.getEffectFactory().createEffect(text,r,effect);
		return e;
	}

	private Cause parseCause(String text, Rule r) throws RuleParsingException, InlineVariableNotFoundException, InputVariableNotFoundException, TermNotFoundException {
		
		Cause c = new Cause ();
		c.action = this.getActionFactory().createAction(text,r);
		Logger.getGlobal().info("ACTION : " + c.action.toString());
		return c;
	}
}
