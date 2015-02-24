package main.application.deffuzification;

import java.util.List;

import main.application.variable.term.Term;
import main.application.variables.OutputVariable;

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
	public abstract double calculate(Term first, List<Term> acculist, OutputVariable outputVariable);
	public static DefuzzificationMethod getDummy(String method) {
		return new DefuzzificationMethod(method) {
			@Override
			public double calculate(Term first, List<Term> acculist, OutputVariable outputVariable) {
				return Double.NaN;
			}
		};
	}
}
