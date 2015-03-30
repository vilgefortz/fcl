package research.fcl.library.rules;

import com.google.gson.annotations.Expose;

import research.fcl.library.variable.term.Term;
import research.fcl.library.variables.BaseFunctionVariable;

public class NormalEffect extends Effect {
	private Term term;
	@Expose
	private String type = "normal";
	public NormalEffect(Term t, BaseFunctionVariable v) throws RuleParsingException {
		super (v);
		this.term = t;
	}
	
	public Term getTerm(double level) {
		return new CutTerm (level,this.term);
	}
}