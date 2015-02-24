package main.application.rules;

import java.util.logging.Logger;

import main.application.variable.term.Term;

public class CutTerm extends Term {

		private double level;
		private Term term;

		public CutTerm (double level,Term term) {
			super(term.getName() + "_with_" + level);
			this.level = level;
			this.term = term;
			this.min=getMin();
			this.max=getMax();
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
		};

}
