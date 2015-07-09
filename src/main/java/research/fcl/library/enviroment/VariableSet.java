package research.fcl.library.enviroment;

import java.util.ArrayList;
import java.util.logging.Logger;

public class VariableSet extends ArrayList<Variable>{
	
	protected Logger log = Logger.getLogger(this.getClass().toGenericString());
	
	public Variable getVariable (String name) {
		int index = -1;
		if ((index = this.indexOf(new Variable(name))) >= 0 ) {
			return this.get(index);
		}
		log.info ("Variable " + name + " does not exist, creating new.");
		Variable var = new Variable(name);
		this.add(var);
		return var;
		
	}
	public void removeVariable (String name) {
		int index = this.indexOf (new Variable (name));
		if (index < 0) return;
		this.remove (index);
	}
	public VariableSet setValue (String name, double value) {
		this.getVariable(name).setValue(value);
		return this;
	}
	public double getValueOf (String name) {
		int index = -1;
		if ((index = this.indexOf(new Variable(name))) >= 0 ) {
			return this.get(index).getValue();
		}
		log.info ("Variable " + name + " does not exist, creating new.");
		Variable var = new Variable(name);
		this.add(var);
		return var.getValue();
	}	
}
