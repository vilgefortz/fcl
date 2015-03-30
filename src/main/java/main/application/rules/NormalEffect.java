package main.application.rules;

import com.google.gson.annotations.Expose;

import main.application.variable.term.Term;
import main.application.variables.BaseFunctionVariable;

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