package research.fcl.library.rules;

import com.google.gson.annotations.Expose;

import research.fcl.library.variable.term.Term;
import research.fcl.library.variable.term.types.CutTerm;
import research.fcl.library.variables.BaseFunctionVariable;

public class WithEffect extends Effect {
	private Term term;
	@Expose
	private double level;
	@Expose
	private String type = "with";
	public WithEffect(Rule r, double level, Term t, BaseFunctionVariable v) throws RuleParsingException {
		super (r,v);
		this.level = level;
		this.term = t;
	}

	public Term getTerm(double level) {
		return new CutTerm (this.rule.getName(), this.level,this.term);
	}
}
