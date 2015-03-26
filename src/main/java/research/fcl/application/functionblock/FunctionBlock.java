package research.fcl.application.functionblock;

import research.fcl.application.Application;
import research.fcl.application.enviroment.Enviroment;
import research.fcl.application.variables.BaseFunctionVariable;
import research.fcl.application.variables.InlineVariableNotFoundException;
import research.fcl.application.variables.InlineVariables;
import research.fcl.application.variables.InputVariableNotFoundException;
import research.fcl.application.variables.InputVariables;
import research.fcl.application.variables.OutputVariableNotFoundException;
import research.fcl.application.variables.OutputVariables;

import com.google.gson.annotations.Expose;

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
