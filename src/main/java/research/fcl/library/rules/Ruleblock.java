package research.fcl.library.rules;

import java.util.ArrayList;
import java.util.List;

import research.fcl.library.andmethods.AndMethod;
import research.fcl.library.andmethods.DefaultAndMethods;
import research.fcl.library.functionblock.FunctionBlock;

import com.google.gson.annotations.Expose;

public class Ruleblock {
	FunctionBlock functionBlock;
	@Expose
	List<Rule> rules = new ArrayList<Rule> ();
	@Expose
	private String name;
	public DefaultRuleFactory ruleFactory; 
	private AndMethod andMethod = DefaultAndMethods.MIN;
	
	public Ruleblock (FunctionBlock fb) {
		this.functionBlock = fb;
		ruleFactory = new DefaultRuleFactory(this);
	}
	
	public void setName(String ruleBlockName) {
		this.name=ruleBlockName;
		
	}
	public void setAndMethod(AndMethod andMethod) {
		this.andMethod = andMethod;
		
	}
	public void addRule(Rule rule) {
		this.rules.add(rule);
	}
	public AndMethod getAndMethod() {
		return this.andMethod;
	}
	public research.fcl.library.enviroment.Enviroment getEnviroment() {
		return this.functionBlock.getEnviroment();
	}
	public FunctionBlock getFunctionBlock() {
		return this.functionBlock;
	}
	
}
