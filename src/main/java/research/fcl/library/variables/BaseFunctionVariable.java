package research.fcl.library.variables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import research.fcl.library.enviroment.Observer;
import research.fcl.library.enviroment.Variable;
import research.fcl.library.functionblock.FunctionBlock;
import research.fcl.library.rules.Rule;
import research.fcl.library.variable.term.Term;
import research.fcl.library.variable.term.TermDeclarationErrorException;

import com.google.gson.annotations.Expose;

public class BaseFunctionVariable implements Observer,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6809124754964817287L;
	protected FunctionBlock fb;
	protected String name;
	@Expose
	private String type;
	@Expose
	protected Variable var;
	@Expose
	private List<Term> terms = new ArrayList<Term>();
	@Expose
	private double min = Double.MAX_VALUE;
	@Expose
	private double max = -Double.MAX_VALUE;
	private List<Rule> dependingRules = new ArrayList<Rule>();
	private List<Rule> affectingRules = new ArrayList<Rule>();

	public BaseFunctionVariable(String name2, FunctionBlock fb2) {
		this.name = name2;
		this.fb = fb2;
		if (fb != null) {
			this.var = this.fb.getEnv().getVariable(name);
			var.addObserver(this);
		}
	}

	public BaseFunctionVariable(String name2) {
		this.name = name2;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (name != null && fb != null)
			this.var = this.fb.getEnv().getVariable(name);
		this.name = name;
	}

	public double getValue() {
		return fb.getEnv().getValueOf(name);
	}

	public void setValue(double value) {
		fb.getEnv().getVariable(name).setValue(value);
	}

	@Override
	public boolean equals(Object obj) {
		return this.name.equalsIgnoreCase(((BaseFunctionVariable) obj).name);
	}

	public void setType(String type) {
		this.type = type;
	}

	public void addTerm(Term term) throws TermDeclarationErrorException {
		if (this.hasTerm(term.getName()))
			throw new TermDeclarationErrorException("Term alerady exists");
		this.terms.add(term);
		this.min = Math.min(this.min, term.getMin());
		this.max = Math.max(this.max, term.getMax());

	}

	public void setDefault(double val) {
		this.setValue(val);
	}

	public boolean hasTerm(String word) {
		return this.terms.contains(Term.getDummy(word));
	}

	public Term getTerm(String word) {
		return this.terms.get(this.terms.indexOf(Term.getDummy(word)));
	}

	@Override
	public void notify(Variable v) {
		this.calculateDependend();
	}

	private static Logger l = Logger.getGlobal();

	private void calculateDependend() {
		if (dependingRules.size() == 0) {
			l.info("No rules for variable " + this.name);
			return;
		}
		Set<BaseFunctionVariable> dependend = new HashSet<BaseFunctionVariable>();
		l.info("Found rules for this variable, count: " + dependingRules.size());
		dependingRules.forEach(r -> {
			dependend.addAll(r.getAffected());
		});
		l.info("All variables that depends on " + this.name + " count : "
				+ dependend.size());
		Set<Rule> rules = new HashSet<Rule>();
		dependend.forEach(v -> {
			rules.addAll(v.affectingRules);
		});
		l.info("All rules to calculate : " + rules.size());

		rules.forEach(r -> {
			double activation = r.getCause().getAction().getValue();
			if (activation > 0)
				r.getEffects().forEach(e -> {
					e.execute(activation);
				});
		});
		dependend.forEach(v -> {
			try {
				((OutputVariable) v).calculateValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public void addAffectingRule(Rule r) {
		this.affectingRules.add(r);
	}

	public void addDependendRule(Rule r) {
		this.dependingRules.add(r);
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

}