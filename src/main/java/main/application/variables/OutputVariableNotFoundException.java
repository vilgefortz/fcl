package main.application.variables;

public class OutputVariableNotFoundException extends Exception {
	public OutputVariableNotFoundException(String name) {
		super ("Output variable \"" + name + "\" not found");
	}	
}
