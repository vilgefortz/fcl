package main.application.functionblock;

import java.util.ArrayList;
import java.util.List;

import main.application.andmethods.AndMethod;
import main.application.andmethods.DefaultAndMethods;
import main.application.rules.DefaultRuleFactory;
import main.application.rules.Rule;

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
	public void execute() {
		
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
	public main.application.enviroment.Enviroment getEnviroment() {
		return this.functionBlock.env;
	}
	public FunctionBlock getFunctionBlock() {
		return this.functionBlock;
	}
	
}
