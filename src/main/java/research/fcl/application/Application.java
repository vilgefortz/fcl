package research.fcl.application;

import java.io.File;
import java.io.FileNotFoundException;

import research.fcl.application.accumulation.AccumulationMethod;
import research.fcl.application.accumulation.AccumulationMethodNotRecognisedException;
import research.fcl.application.accumulation.DefaultAccumulationMethods;
import research.fcl.application.andmethods.AndMethod;
import research.fcl.application.andmethods.AndMethodNotFoundException;
import research.fcl.application.andmethods.DefaultAndMethods;
import research.fcl.application.deffuzification.DefaultDeffuzificationMethods;
import research.fcl.application.deffuzification.DefuzzificationMethod;
import research.fcl.application.deffuzification.DefuzzificationMethodNotRecognisedException;
import research.fcl.application.enviroment.Enviroment;
import research.fcl.application.parser.JsonLogger;
import research.fcl.application.parser.Parser;
import research.fcl.application.parser.ParserBase;
import research.fcl.application.variable.term.DefaultTermFactory;
import research.fcl.application.variable.term.TermFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
	public JsonLogger logger;
	public TermFactory termFactory = new DefaultTermFactory (this);
	private DefaultAccumulationMethods accuMethods = new DefaultAccumulationMethods();
	private DefaultDeffuzificationMethods deffuMethods = new DefaultDeffuzificationMethods(); 
	private DefaultAndMethods andMethods = new DefaultAndMethods ();
	
	public static void main (String [] args) throws FileNotFoundException {
		Parser p = new Parser(new File ("test.fcl"));
		p.parse();
		System.out.println(p.app.toJson());
		
	}
	public String toJson() throws FileNotFoundException {
		
			Gson gson = new GsonBuilder().
					excludeFieldsWithoutExposeAnnotation().
					setPrettyPrinting().
					create();
		 return gson.toJson(this);
		
		
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
}

