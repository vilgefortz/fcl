package research.fcl.application.variables;

import java.util.ArrayList;

public class InlineVariables extends ArrayList<InlineVariable> {

	public BaseFunctionVariable getInlineVariable (String name) throws InlineVariableNotFoundException {
		int index = -1;
		if ((index = this.indexOf(new InlineVariable(name))) >= 0 ) {
			return this.get(index);
		}
		throw new InlineVariableNotFoundException (name);
	}
	
	public double getValueOf (String name) throws InlineVariableNotFoundException {
		int index = -1;
		if ((index = this.indexOf(new InlineVariable(name))) >= 0 ) {
			return this.get(index).getValue();
		}
		throw new InlineVariableNotFoundException (name);
	}
}
