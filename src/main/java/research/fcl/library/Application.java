package research.fcl.library;

import java.io.File;
import java.io.FileNotFoundException;

import research.fcl.library.andmethods.AndMethod;
import research.fcl.library.andmethods.AndMethodNotFoundException;
import research.fcl.library.andmethods.DefaultAndMethods;
import research.fcl.library.defuzzification.DefaultDefuzzificationMethods;
import research.fcl.library.defuzzification.DefuzzificationMethod;
import research.fcl.library.defuzzification.DefuzzificationMethodNotRecognisedException;
import research.fcl.library.enviroment.Enviroment;
import research.fcl.library.enviroment.VariableSet;
import research.fcl.library.functionblock.FunctionBlock;
import research.fcl.library.functionblock.FunctionBlocks;
import research.fcl.library.parser.ParserLogger;
import research.fcl.library.parser.Parser;
import research.fcl.library.parser.ParserBase;
import research.fcl.library.terms.DefaultTermFactory;
import research.fcl.library.terms.TermFactory;
import research.fcl.library.accumulation.AccumulationMethod;
import research.fcl.library.accumulation.AccumulationMethodNotRecognisedException;
import research.fcl.library.accumulation.DefaultAccumulationMethods;






import com.google.gson.annotations.Expose;

public class Application {
	private ParserBase parser;
	public ParserBase getParser() {
		return parser;
	}
	@Expose
	private Enviroment enviroment = new Enviroment ();
	@Expose
	public FunctionBlocks functionBlocks =new FunctionBlocks();
	@Expose
	public ParserLogger logger;
	public TermFactory termFactory = new DefaultTermFactory (this);
	private DefaultAccumulationMethods accuMethods = new DefaultAccumulationMethods();
	private DefaultDefuzzificationMethods deffuMethods = new DefaultDefuzzificationMethods(); 
	private DefaultAndMethods andMethods = new DefaultAndMethods ();
	public double getValue (String name) {
		return this.getEnv().getValueOf(name);
	}
	public VariableSet setValue (String name, double value) {
		this.getEnv().setValue(name, value);
		return this.getEnv();
	}
	public static void main (String [] args) throws FileNotFoundException {
		Parser p = new Parser(new File ("test.fcl"));
		p.parse();
		
	}
	public Enviroment getEnv() {
		return enviroment;
	}
	public void setEnviroment(Enviroment env) {
		this.enviroment= env;
		
	}
	public void setParser(ParserBase parser) {
		this.parser=parser;
		
	}
	public String[] getAccuMethodsNames() {
		return this.accuMethods.getNames();
	}
	public AccumulationMethod getAccuMethod(String method) throws AccumulationMethodNotRecognisedException {
		return this.accuMethods.get(method);
	}
	public String[] getDeffuMethodsNames() {
		return this.deffuMethods.getNames ();
	}
	public DefuzzificationMethod getDefuzzificationMethod(String method) throws DefuzzificationMethodNotRecognisedException {
		return this.deffuMethods.get(method);
	}
	public AndMethod getAndMethod(String name) throws AndMethodNotFoundException {
		return this.andMethods.getMethod(name);
	}
	public String[] getAndMethodsNames() {
		return this.andMethods.getNames();
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
	public void addFunctionBlock(FunctionBlock fb) {
		this.functionBlocks.add(fb);
	}
	public TermFactory getTermFactory() {
		return this.termFactory;
	}
}

