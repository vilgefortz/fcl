package research.fcl.library.rules.modifiers;

import research.fcl.library.terms.Term;

public class FairlyModifier extends Modifier {
	public class FairlyTerm extends Term {

		private Term t;

		public FairlyTerm(String name,Term t) {
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
			return Math.sqrt(t.fun(val));
		}
		
	}
	@Override
	public Term modify(Term t) {
		return new FairlyTerm(t.getName(), t);
	}

}
