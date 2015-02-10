package main.application.functionblock;

import com.google.gson.annotations.Expose;

import main.application.Application;
import main.application.enviroment.Enviroment;
import main.application.variables.InputVariables;
import main.application.variables.OutputVariables;

public class FunctionBlock {
	@Expose
	public InputVariables input = new InputVariables();
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
	public Enviroment getEnv() {
		return env;
	}
	public void setEnv(Enviroment env) {
		this.env = env;
	}
	public void execute () {
		ruleblocks.execute();
	}
}
