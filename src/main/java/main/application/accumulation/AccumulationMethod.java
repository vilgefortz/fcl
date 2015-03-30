package main.application.accumulation;

import main.application.variable.term.Term;

import com.google.gson.annotations.Expose;

public abstract class AccumulationMethod {
	@Expose
	private String name;
	
	public AccumulationMethod(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}
	@Override
	public boolean equals(Object obj) {
		return this.name.equalsIgnoreCase(((AccumulationMethod)obj).name);
	}
	public abstract Term accumulate (Term a, Term b);

	public static Object getDummy(String method) {
		return new AccumulationMethod (method) {
			@Override
			public Term accumulate(Term a, Term b) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
