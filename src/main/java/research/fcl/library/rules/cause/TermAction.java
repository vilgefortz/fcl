package research.fcl.library.rules.cause;

import com.google.gson.annotations.Expose;

import research.fcl.library.terms.Term;
import research.fcl.library.variables.BaseFunctionVariable;

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
