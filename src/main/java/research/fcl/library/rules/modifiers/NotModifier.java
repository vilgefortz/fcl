package research.fcl.library.rules.modifiers;

import research.fcl.library.terms.Term;

public class NotModifier extends Modifier {
	public class NotTerm extends Term {
		Term t;
		public NotTerm(String name,Term t) {
			super("not_" +name);
			this.t=t;
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
			return 1-t.fun(val);
		}
		
	}
	@Override
	public Term modify(Term t) {
		return new NotTerm (t.getName(), t);
	}

}
