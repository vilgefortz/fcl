package main.application.accumulation;

import main.application.variable.term.Term;

public class MaxMethod extends AccumulationMethod {

	public MaxMethod(String name) {
		super(name);
	}

	@Override
	public Term accumulate(Term a, Term b) {
		return new Term (a.getName()+ "_" + b.getName()) {

			@Override
			public double getMax() {
				return Math.max(a.getMax(), b.getMax());
			}

			@Override
			public double getMin() {
				return Math.min(a.getMin(), b.getMin());
			}

			@Override
			public double fun(double val) {
				return Math.max(a.fun(val), b.fun(val));
			}
			
		};
	}
	
	
}
