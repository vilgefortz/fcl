package main.application.deffuzification;

import java.util.List;
import java.util.logging.Logger;

import main.application.variable.term.Term;
import main.application.variables.OutputVariable;

import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;

public class CogMethod extends DefuzzificationMethod {
	public static Logger log = Logger.getGlobal() ;
	public CogMethod(String method) {
		super(method);
	}

	@Override
	public double calculate(Term first, List<Term> acculist,
			OutputVariable var) {
		TrapezoidIntegrator u = new TrapezoidIntegrator();
		TrapezoidIntegrator l = new TrapezoidIntegrator();
		log.info ("Counting simpson for " + var.getName() + " min : " + var.getMin() + " max : " + var.getMax() );
		double a = u.integrate(10000000, v->{return v*first.fun(v);}, first.getMin() , first.getMax());
		double b = l.integrate(10000000, v->{return first.fun(v);}, first.getMin() , first.getMax());
		log.info ("Calculated value : " + a/b);
		return b!=0?a/b:var.getValue();
	}

}
 