package research.fcl.library.rules;

import java.util.logging.Logger;

import research.fcl.library.variable.term.Term;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.library.variables.OutputVariable;

public abstract class Effect {
	public OutputVariable var;

	public Effect(BaseFunctionVariable v) throws RuleParsingException {
		try {
			this.var = (OutputVariable) v;

		} catch (Exception e) {
			throw new RuleParsingException("Variable '" + v
					+ "' cannot be defuzzified");
		}
	}

	public abstract Term getTerm(double level);
	private static Logger l = Logger.getGlobal();
	public void execute(double level) {
		l.info("accumulating term " + this.getTerm(level).getName() + " with level " + level);
		this.var.accumulateTerm(this.getTerm(level));
	}
}
