package main.application.variables;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.application.accumulation.AccumulationMethod;
import main.application.accumulation.AccumulationMethodNotRecognisedException;
import main.application.deffuzification.DeffuzzificationMethodNotRecognisedException;
import main.application.deffuzification.DefuzzificationMethod;
import main.application.deffuzification.DefuzzificationMethodNotRecognisedException;
import main.application.functionblock.FunctionBlock;
import main.application.variable.term.Term;

import com.google.gson.annotations.Expose;

public class OutputVariable extends BaseFunctionVariable  {
	@Expose
	private AccumulationMethod accuMethod;
	@Expose
	private DefuzzificationMethod deffMethod;
	@Expose
	private List<Term> acculist = new ArrayList<Term> ();
	@Expose
	private List<Term> oldAccuList;
	@Expose
	private Term oldAccuTerm;
	
	
	public OutputVariable(String name, FunctionBlock fb) {
		super (name,fb);
	}

	public OutputVariable(String name) {
		super (name,null);
	}

	public void accumulateTerm(Term term) {
		this.acculist .add(term);
		
	}

	public void setAccuMethod(String method)
			throws AccumulationMethodNotRecognisedException {
		this.accuMethod = this.fb.getApp().getAccuMethod(method);
	}

	public void setDefuzzificationMethod(String method) throws DefuzzificationMethodNotRecognisedException {
		this.deffMethod = this.fb.getApp().getDefuzzificationMethod(method);
		
	}
	private static Logger l = Logger.getGlobal();
	public void calculateValue() {
		if (this.acculist.size()==0) {
			l.info ("accu list is empty");
			return;
		}
		Term first = acculist.get(0);
		for (int i=1; i<acculist.size(); i++) {
			first = this.accuMethod.accumulate(first, acculist.get(i));
		}
		l.info ("accu list size : " + acculist.size());
		this.setValue(this.deffMethod.calculate(first,acculist,this));
		this.oldAccuList = new ArrayList<Term> (acculist);
		this.oldAccuTerm = first;
		acculist.clear();
	}
//	
}
