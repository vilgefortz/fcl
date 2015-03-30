package main.application;

import java.io.File;
import java.io.FileNotFoundException;

import main.application.accumulation.AccumulationMethod;
import main.application.accumulation.AccumulationMethodNotRecognisedException;
import main.application.accumulation.DefaultAccumulationMethods;
import main.application.andmethods.AndMethod;
import main.application.andmethods.AndMethodNotFoundException;
import main.application.andmethods.DefaultAndMethods;
import main.application.deffuzification.DefaultDeffuzificationMethods;
import main.application.deffuzification.DefuzzificationMethod;
import main.application.deffuzification.DefuzzificationMethodNotRecognisedException;
import main.application.enviroment.Enviroment;
import main.application.parser.JsonLogger;
import main.application.parser.Parser;
import main.application.parser.ParserBase;
import main.application.rules.DefaultRuleFactory;
import main.application.variable.term.DefaultTermFactory;
import main.application.variable.term.TermFactory;

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

