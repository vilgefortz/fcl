package research.fcl.library.accumulation;

import org.apache.commons.lang.ArrayUtils;

import com.google.gson.annotations.Expose;

import research.fcl.library.variable.term.Term;

public class MaxMethod extends AccumulationMethod {

	public class MaxTerm extends Term {

		private Term a;
		private Term b;

		public MaxTerm(String name, Term a, Term b) {
			super(name);
			this.a = a;
			this.b = b;
			this.max=this.getMax();
			this.min = this.getMin();
			this.type = "accumulation";
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

		@Override
		public double[] getImportantPoints() {
			return ArrayUtils.addAll (a.getImportantPoints(),b.getImportantPoints());
		}
		

		
	}
	public MaxMethod(String name) {
		super(name);
	}

	@Override
	public Term accumulate(Term aa, Term bb) {
		return new MaxTerm ("@accu", aa,bb);
		}
}
