package research.fcl.library.variables;

import java.util.ArrayList;

import research.fcl.library.variables.exceptions.OutputVariableNotFoundException;

public class OutputVariables extends ArrayList<OutputVariable> {

	public OutputVariable getOutputVariable (String name) throws OutputVariableNotFoundException {
		int index = -1;
		if ((index = this.indexOf(new OutputVariable(name))) >= 0 ) {
			return this.get(index);
		}
		throw new OutputVariableNotFoundException (name);
	}
	
	public double getValueOf (String name) throws OutputVariableNotFoundException {
		int index = -1;
		if ((index = this.indexOf(new OutputVariable(name))) >= 0 ) {
			return this.get(index).getValue();
		}
		throw new OutputVariableNotFoundException (name);
	}
}
