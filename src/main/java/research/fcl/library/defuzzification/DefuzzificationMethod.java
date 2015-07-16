package research.fcl.library.defuzzification;

import java.util.List;

import research.fcl.library.terms.Term;
import research.fcl.library.variables.OutputVariable;
import research.fcl.library.accumulation.AccumulationMethod;

import com.google.gson.annotations.Expose;

public abstract class DefuzzificationMethod {
	@Expose
	private String name;
	public DefuzzificationMethod(String method) {
		this.name=method;
	}
	public String getName() {
		return this.name;
	}
	@Override
	public boolean equals(Object obj) {
		return this.name.equalsIgnoreCase(((DefuzzificationMethod)obj).name);
	}
	
	public static DefuzzificationMethod getDummy(String method) {
		return new DefuzzificationMethod(method) {
			@Override
			public double calculate(Term first, List<Term> acculist, OutputVariable outputVariable, AccumulationMethod accuMethod) {
				return Double.NaN;
			}
		};
	}
	public abstract double calculate(Term first, List<Term> acculist,
			OutputVariable outputVariable, AccumulationMethod accuMethod);
}
