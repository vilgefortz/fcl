package research.fcl.library.variables;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import research.fcl.library.deffuzification.DefuzzificationMethod;
import research.fcl.library.deffuzification.DefuzzificationMethodNotRecognisedException;
import research.fcl.library.functionblock.FunctionBlock;
import research.fcl.library.variable.term.Term;
import research.fcl.library.accumulation.AccumulationMethod;
import research.fcl.library.accumulation.AccumulationMethodNotRecognisedException;
import research.fcl.library.variable.term.types.SingletonTerm;

import com.google.gson.annotations.Expose;

public class OutputVariable extends BaseFunctionVariable  {
	public static final String ACCU_TERM = "@accu";	
	@Expose
	private AccumulationMethod accuMethod;
	@Expose
	private DefuzzificationMethod deffMethod;
	@Expose
	private List<Term> acculist = new ArrayList<Term> ();
	@Expose
	private List<Term> oldAccuList = new ArrayList<Term> ();
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
		int index = this.acculist.size()-1;
		term.setName("@accu" + index);
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
		this.setValue(this.deffMethod.calculate(first,acculist,this,this.accuMethod));
		this.oldAccuList = new ArrayList<Term> (acculist);
		this.oldAccuTerm = first;
		this.oldAccuTerm.setName ("@accu");
		acculist.clear();
	}
	
	public Term getAccumulationTerm () {
		return this.oldAccuTerm;
	}
	public List<Term> getAccumulationTerms () {
		return this.oldAccuList;
	}
//	

	public FunctionBlock getFunctionBlock() {
		return this.fb;
	}	
	
	public Term getTermFromAll (String name) throws TermNotFoundException {
		System.out.println ("getting term fron all '" + name + "'");
		if (name.equals(BaseFunctionVariable.VALUE_TERM)) return new SingletonTerm (BaseFunctionVariable.VALUE_TERM, var.getValue());
		if (name.equals(OutputVariable.ACCU_TERM)) return this.getAccumulationTerm();
		try {
			return this.getTerm (name);
		}
		catch (TermNotFoundException e) {
			int index = this.oldAccuList.indexOf (Term.getDummy(name));
			if (index < 0) throw new TermNotFoundException (name, this.name);
			return this.oldAccuList.get(index);
		}
	}
	public List<Term> getAllTerms () {
		List<Term> allTerms = super.getAllTerms();
		System.out.println ("RETRIEVING ACCUMULATION TERM :" + this.getAccumulationTerm());
		allTerms.add (this.getAccumulationTerm());
		//allTerms.addAll (this.getAccumulationTerms());
		//allTerms.forEach ( e -> { l.info ("Created list for output variable " + this.getName() + " added term " + e.getName()); });
		return allTerms;
	}
}
