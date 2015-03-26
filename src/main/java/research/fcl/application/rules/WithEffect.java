package research.fcl.application.rules;

import com.google.gson.annotations.Expose;

import research.fcl.application.variable.term.Term;
import research.fcl.application.variables.BaseFunctionVariable;

public class WithEffect extends Effect {
	private Term term;
	@Expose
	private double level;
	@Expose
	private String type = "with";
	public WithEffect(double level, Term t, BaseFunctionVariable v) throws RuleParsingException {
		super (v);
		this.level = level;
		this.term = t;
	}

	public Term getTerm(double level) {
		return new CutTerm (this.level,this.term);
	}
}
