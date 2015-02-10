package main.application.variables;

import main.application.functionblock.FunctionBlock;

public class InputVariable extends BaseFunctionVariable {
	public InputVariable(FunctionBlock fb) {
		this.fb = fb;
	}

	public InputVariable(String name) {
		this.name = name;
	}

	public InputVariable(String name, FunctionBlock fb) {
		this.name = name;
		this.fb = fb;
		this.var = this.fb.getEnv().getVariable(name);
	}
}
