package research.fcl.application.rules;

import com.google.gson.annotations.Expose;

import research.fcl.application.variable.term.Term;
import research.fcl.application.variables.BaseFunctionVariable;

public class TermAction extends Action {

	private Term term;
	private BaseFunctionVariable var;
	@Expose private String type = "term";
	@Expose private String name;
	@Expose private String varName;
	public TermAction(Term t, BaseFunctionVariable v) {
		this.term = t;
		this.var =v;
		this.name=t.getName();
		this.varName=var.getName();
	}

	@Override
	public double getValue() {
		return term.fun(var.getValue());
	}

}
