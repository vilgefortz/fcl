package main.application.enviroment;

import java.util.ArrayList;
import java.util.logging.Logger;

public class VariableSet extends ArrayList<Variable>{
	
	private Logger log = Logger.getLogger(this.getClass().toGenericString());
	
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
