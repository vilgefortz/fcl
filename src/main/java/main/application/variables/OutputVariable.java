package main.application.variables;


import main.application.functionblock.FunctionBlock;

public class OutputVariable extends BaseFunctionVariable{
	public OutputVariable(FunctionBlock fb) {
		this.fb = fb;
	}

	public OutputVariable(String name) {
		this.name = name;
	}

	public OutputVariable(String name, FunctionBlock fb) {
		this.name = name;
		this.fb = fb;
		this.var = this.fb.getEnv().getVariable(name);
	}
	}
