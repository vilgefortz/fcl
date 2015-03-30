package main.application.functionblock;

import com.google.gson.annotations.Expose;

import main.application.Application;
import main.application.enviroment.Enviroment;
import main.application.enviroment.Variable;
import main.application.variables.BaseFunctionVariable;
import main.application.variables.InlineVariableNotFoundException;
import main.application.variables.InlineVariables;
import main.application.variables.InputVariableNotFoundException;
import main.application.variables.InputVariables;
import main.application.variables.OutputVariableNotFoundException;
import main.application.variables.OutputVariables;

public class FunctionBlock {
	@Expose
	public InputVariables input = new InputVariables();
	@Expose
	public InlineVariables inline = new InlineVariables();
	@Expose
	public OutputVariables output = new OutputVariables();
	@Expose
	public Ruleblocks ruleblocks = new Ruleblocks();
	@Expose
	public String name;
	protected Enviroment env;
	private Application app;
	public FunctionBlock(Application app) {
		this.app = app;
		this.env = app.getEnv();
	}
	public Application getApp() {
		return app;
	}
	public void setApp(Application app) {
		this.app = app;
	}
	public Enviroment getEnv() {
		return env;
	}
	public void setEnv(Enviroment env) {
		this.env = env;
	}
	public void execute () {
		ruleblocks.execute();
	}
	public BaseFunctionVariable getLeftVariable(String varName) throws  InputVariableNotFoundException {
		try {
			return this.inline.getInlineVariable(varName);
		} catch (InlineVariableNotFoundException e) {
			return this.input.getInputVariable(varName);		
		}
	}
	public BaseFunctionVariable getRightVariable(String varName) throws OutputVariableNotFoundException {
		try {
			return this.inline.getInlineVariable(varName);
		} catch (InlineVariableNotFoundException e) {
			return this.output.getOutputVariable(varName);		
		}
	}
	
}
