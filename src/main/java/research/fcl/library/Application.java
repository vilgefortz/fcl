package research.fcl.library;

import java.io.File;
import java.io.FileNotFoundException;

import research.fcl.library.accumulation.AccumulationMethod;
import research.fcl.library.accumulation.AccumulationMethodNotRecognisedException;
import research.fcl.library.accumulation.AccumulationMethodsFactory;
import research.fcl.library.accumulation.DefaultAccumulationMethodsFactory;
import research.fcl.library.andmethods.AndMethodsFactory;
import research.fcl.library.andmethods.DefaultAndMethods;
import research.fcl.library.defuzzification.DefaultDefuzzificationMethodsFactory;
import research.fcl.library.defuzzification.DefuzzificationMethod;
import research.fcl.library.defuzzification.DefuzzificationMethodNotRecognisedException;
import research.fcl.library.defuzzification.DefuzzificationMethodsFactory;
import research.fcl.library.enviroment.Enviroment;
import research.fcl.library.enviroment.VariableSet;
import research.fcl.library.functionblock.FunctionBlock;
import research.fcl.library.functionblock.FunctionBlocks;
import research.fcl.library.parser.Parser;
import research.fcl.library.parser.ParserBase;
import research.fcl.library.parser.ParserLogger;
import research.fcl.library.rules.DefaultActionFactory;
import research.fcl.library.rules.DefaultEffectFactory;
import research.fcl.library.rules.DefaultRuleFactory;
import research.fcl.library.rules.RuleFactory;
import research.fcl.library.rules.Ruleblock;
import research.fcl.library.rules.modifiers.DefaultModifierFactory;
import research.fcl.library.rules.modifiers.ModifierFactory;
import research.fcl.library.terms.DefaultTermFactory;
import research.fcl.library.terms.TermFactory;

import com.google.gson.annotations.Expose;

public class Application {
	public static void main (String [] args) throws FileNotFoundException {
		Parser p = new Parser(new File ("test.fcl"));
		p.parse();
		
	}
	private AccumulationMethodsFactory accumulationMethodsFactory = new DefaultAccumulationMethodsFactory();
	private AndMethodsFactory andMethods = new DefaultAndMethods ();
	public AccumulationMethodsFactory getAccumulationMethodsFactory() {
		return accumulationMethodsFactory;
	}
	public void setAccumulationMethodsFactory(
			AccumulationMethodsFactory accumulationMethodsFactory) {
		this.accumulationMethodsFactory = accumulationMethodsFactory;
	}
	public AndMethodsFactory getAndMethodsFactory() {
		return andMethods;
	}
	public void setAndMethodsFactory(AndMethodsFactory andMethods) {
		this.andMethods = andMethods;
	}
	public DefuzzificationMethodsFactory getDefuzzificationMethodsFactory() {
		return defuzzificationMethodsFactory;
	}
	public void setDefuzzificationMethodsFactory(
			DefuzzificationMethodsFactory defuzzificationMethodsFactory) {
		this.defuzzificationMethodsFactory = defuzzificationMethodsFactory;
	}
	public void setTermFactory(TermFactory termFactory) {
		this.termFactory = termFactory;
	}
	private DefuzzificationMethodsFactory defuzzificationMethodsFactory = new DefaultDefuzzificationMethodsFactory();
	@Expose
	private Enviroment enviroment = new Enviroment ();
	@Expose
	public FunctionBlocks functionBlocks =new FunctionBlocks();
	@Expose
	public ParserLogger logger;
	private ParserBase parser; 
	private TermFactory termFactory = new DefaultTermFactory (this);
	private ModifierFactory modifierFactory = new DefaultModifierFactory();
	public void setModifiersFactory(DefaultModifierFactory modifiersFactory) {
		this.modifierFactory = modifiersFactory;
	}
	public void addFunctionBlock(FunctionBlock fb) {
		this.functionBlocks.add(fb);
	}
	public Enviroment getEnv() {
		return enviroment;
	}
	public FunctionBlock getFunctionBlock(String fbName) {
		try {
			return this.functionBlocks.get(functionBlocks.indexOf(FunctionBlock.getDummy(fbName)));
		}
		catch (Exception e) {
			//not found
			return null;
		}
	}
	public ParserBase getParser() {
		return parser;
	}
	public TermFactory getTermFactory() {
		return this.termFactory;
	}
	public double getValue (String name) {
		return this.getEnv().getValueOf(name);
	}
	
	public void setEnviroment(Enviroment env) {
		this.enviroment= env;
		
	}
	public void setParser(ParserBase parser) {
		this.parser=parser;
		
	}
	public VariableSet setValue (String name, double value) {
		this.getEnv().setValue(name, value);
		return this.getEnv();
	}
	public AccumulationMethod getAccuMethod(String method) throws AccumulationMethodNotRecognisedException {
		return this.getAccumulationMethodsFactory().get(method);
	}
	public DefuzzificationMethod getDefuzzificationMethod(String method) throws DefuzzificationMethodNotRecognisedException {
		return this.getDefuzzificationMethodsFactory().get(method);
	}
	public ModifierFactory getModifierFactory() {
		return modifierFactory;
	}
	public DefaultActionFactory getActionFactory(Ruleblock rb) {
		return new DefaultActionFactory(rb,this.getModifierFactory());
	}
	public DefaultEffectFactory getEffectFactory(Ruleblock rb) {
		return new DefaultEffectFactory(rb);
	}
	public RuleFactory getRuleFactory(Ruleblock ruleblock) {
		return new DefaultRuleFactory(ruleblock);
	}
}

