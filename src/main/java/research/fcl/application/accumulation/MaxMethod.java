package research.fcl.application.accumulation;

import research.fcl.application.variable.term.Term;

public class MaxMethod extends AccumulationMethod {

	public MaxMethod(String name) {
		super(name);
	}

	@Override
	public Term accumulate(Term aa, Term bb) {
		return new Term (aa.getName()+ "_" + bb.getName()) {
			Term a=aa, b=bb;
			{
			this.max=this.getMax();
			this.min = this.getMin();
			}
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
