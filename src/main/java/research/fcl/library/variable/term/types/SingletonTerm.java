package research.fcl.library.variable.term.types;

import com.google.gson.annotations.Expose;

import research.fcl.library.variable.term.Term;
import research.fcl.library.variable.term.TermDeclarationErrorException;

public class SingletonTerm extends Term {

	@Expose public double point;

	public SingletonTerm(String termName, String definition) throws TermDeclarationErrorException {
		super(termName);
		this.type = "singleton";
		try {
		this.point = Double.parseDouble(definition);
		}
		catch (NumberFormatException e) {
			throw new TermDeclarationErrorException("Expected numeric value for singleton, found '" + definition +"'");
					
		}
		this.max = point;
		this.min = point;

	}

	@Override
	public double fun(double val) {
		return val==this.point? 1: 0;
	}

	@Override
	public double getMax() {
		return this.point;
	}

	@Override
	public double getMin() {
		return this.point;
	}

	@Override
	public double[] getImportantPoints() {
		return new double [] {this.point};
	}

}
