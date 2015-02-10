package main.application.variables;

public class InputVariableNotFoundException extends Exception {
	public InputVariableNotFoundException(String name) {
		super ("Input variable \"" + name + "\" not found");
	}	
}
