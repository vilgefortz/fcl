package research.fcl.library.rules;

import research.fcl.library.variable.term.Term;

public class CutTerm extends Term {

		private double level;
		private Term term;

		public CutTerm (String name, double level,Term term) {
			super("@rule-" + name);
			this.level = level;
			this.term = term;
			this.min=getMin();
			this.max=getMax();
		}
		
		@Override
		public Term getBaseTerm() {
			return this.term;
		}
		
		@Override
		public double fun(double val) {
			return Math.min(term.fun(val),level);
		}

		@Override
		public double getMax() {
			return term.getMax();
		}

		@Override
		public double getMin() {
			
			return term.getMin();
		}

		@Override
		public double[] getImportantPoints() {
			return term.getImportantPoints();
		};
		
		

}
