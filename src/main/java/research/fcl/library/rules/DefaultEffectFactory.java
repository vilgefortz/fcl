package research.fcl.library.rules;

import java.util.List;

import research.fcl.library.parser.utils.ParsingUtils;
import research.fcl.library.rules.effect.Effect;
import research.fcl.library.rules.effect.NormalEffect;
import research.fcl.library.rules.effect.WithEffect;
import research.fcl.library.terms.Term;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.library.variables.exceptions.OutputVariableNotFoundException;
import research.fcl.library.variables.exceptions.TermNotFoundException;

public class DefaultEffectFactory {

	protected String WITH = "with";
	protected Ruleblock ruleblock;
	protected String IS = "is";
	private String AND = "and";
	public DefaultEffectFactory (Ruleblock rb) {
		this.ruleblock = rb;
	}
	public Effect createEffect(String text, Rule r, List<Effect> effect) throws OutputVariableNotFoundException, RuleParsingException, TermNotFoundException {
		text = text.trim();
		String varName = ParsingUtils.getFirstWord(text);
		BaseFunctionVariable var = ruleblock.getFunctionBlock().getRightVariable(varName);
		if (r.dependsOn(var)) throw new RuleParsingException("Recurrency with variable '" + varName + "' is forbidden");
		r.addAffected(var);
		var.addAffectingRule(r);
		String is = ParsingUtils.getFirstWord(text=text.substring(varName.length()).trim());
		if (!is.equalsIgnoreCase(IS)) throw new RuleParsingException("Expected keyword 'is' after variable '" + varName + "'");
		String term = ParsingUtils.getFirstWord(text=text.substring(is.length()).trim());
		if (!var.hasTerm(term)) 
			throw new RuleParsingException("Term '" + term + "' is not defined for variable '" + var.getName() + "'");
		Term t = var.getTerm(term);
		String with = ParsingUtils.getFirstWord(text=text.substring(term.length()).trim());
		Effect eff = null;
		if (with.equalsIgnoreCase(WITH)) {
			String number = ParsingUtils.getFirstWord(text=text.substring(with.length()).trim(),"[\\.a-z0-9_]*");
			try {
				double level = Double.parseDouble(number);
				eff = new WithEffect (r, level, t, var);
				with = ParsingUtils.getFirstWord(text=text.substring(number.length()).trim());
			}
			catch (Exception e) {
				throw new RuleParsingException("Please provide REAL value for 'with' keyword, '" + number +"' found");
			}
		}
		else {
			eff = new NormalEffect(r, t, var);
		}
		if (with.equalsIgnoreCase(AND)) {
			text = text.substring(with.length());
			effect.add(this.createEffect(text, r, effect));
		}
		else if (!with.equals("")) {
			throw new RuleParsingException("Unexpected character sequence '" + with + "' in rule definition");
		}
		return eff;
	}

	

}
