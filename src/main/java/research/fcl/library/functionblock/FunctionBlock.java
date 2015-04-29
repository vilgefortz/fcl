package research.fcl.library.functionblock;

import research.fcl.library.Application;
import research.fcl.library.enviroment.Enviroment;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.library.variables.InlineVariableNotFoundException;
import research.fcl.library.variables.InlineVariables;
import research.fcl.library.variables.InputVariableNotFoundException;
import research.fcl.library.variables.InputVariables;
import research.fcl.library.variables.OutputVariableNotFoundException;
import research.fcl.library.variables.OutputVariables;

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
	public FunctionBlock(String fbName) {
		this.name=fbName;
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
	@Override
	public boolean equals(Object obj) {
		return this.name.equals(((FunctionBlock)obj).name);
	}
	public static FunctionBlock getDummy(String fbName) {
		return new FunctionBlock(fbName);
	}
	public BaseFunctionVariable getVariable(String name) throws VariableNotFoundException {
		if (name == null) {
			throw new VariableNotFoundException ("null value given");
		}
		try {
			return getLeftVariable(name);
		} catch (InputVariableNotFoundException e) {
			try {
				return getRightVariable(name);
			} catch (OutputVariableNotFoundException e1) {
				throw new VariableNotFoundException (name);
			}
		}
	}
	public String getName() {
		return this.name;
	}
}
