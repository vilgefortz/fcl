package research.fcl.application.variables;

import java.io.Serializable;

import research.fcl.application.functionblock.FunctionBlock;

public class InputVariable extends BaseFunctionVariable implements Serializable{

	private static final long serialVersionUID = -4390349344965618303L;

	public InputVariable(String name) {
		super (name,null);
	}

	public InputVariable(String name, FunctionBlock fb) {
		super (name,fb);
	}
}
