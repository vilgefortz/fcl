package research.fcl.library.rules.modifiers;

import research.fcl.library.terms.Term;

public class VeryModifier extends Modifier {
	public class VeryTerm extends Term {

		private Term t;

		public VeryTerm(String name, Term t) {
			super(name);
			this.t = t;
		}

		@Override
		public double getMax() {
			return t.getMax();
		}

		@Override
		public double getMin() {
			return t.getMin();
		}

		@Override
		public double[] getImportantPoints() {
			return t.getImportantPoints();
		}

		@Override
		public double fun(double val) {
			double z=t.fun(val);
			return z*z;
		}
	}

	@Override
	public Term modify(Term t) {
		return new VeryTerm(t.getName(), t);
	}
}
