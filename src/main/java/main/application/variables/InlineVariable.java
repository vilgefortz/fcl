package main.application.variables;

import main.application.functionblock.FunctionBlock;

public class InlineVariable extends OutputVariable {

	public InlineVariable(String name, FunctionBlock fb) {
		super (name,fb);
	}

	public InlineVariable(String name) {
		super (name,null);
	}
}
