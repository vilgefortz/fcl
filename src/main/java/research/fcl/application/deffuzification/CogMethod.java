package research.fcl.application.deffuzification;

import java.util.List;
import java.util.logging.Logger;

import research.fcl.application.accumulation.AccumulationMethod;
import research.fcl.application.variable.term.Term;
import research.fcl.application.variable.term.types.SingletonTerm;
import research.fcl.application.variable.term.types.TraingleSingleton;
import research.fcl.application.variables.OutputVariable;

import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;

public class CogMethod extends DefuzzificationMethod {
	public static Logger log = Logger.getGlobal() ;
	public CogMethod(String method) {
		super(method);
	}
	Term temp;
	/* TODO 
	 * externalize magic numbers
	 */
	@Override
	public double calculate(Term first, List<Term> acculist,
			OutputVariable var, AccumulationMethod accuMethod) {
		//recalculating main term - converting singletons to triangular terms
		Term main = convertSingleton (acculist.get(0),var);
		for (int i=1; i< acculist.size(); i++) {
			main = accuMethod.accumulate(main, convertSingleton (acculist.get(i),var));
		}
		temp = main;
		TrapezoidIntegrator u = new TrapezoidIntegrator();
		TrapezoidIntegrator l = new TrapezoidIntegrator();
		double min = /*temp.getMin()==temp.getMax()? var.getMin() :*/ temp.getMin();
		double max = /*temp.getMin()==temp.getMax()? var.getMax() :*/ temp.getMax();
		log.info ("Counting simpson for " + var.getName() + " min : " + temp.getMin() + " max : " + temp.getMax() );
		double a = u.integrate(Integer.MAX_VALUE, v->{return v*temp.fun(v)*1000;}, min , max);
		log.info ("a : " + a );
		double b = l.integrate(Integer.MAX_VALUE, v->{return temp.fun(v)*1000;}, min , max);
		log.info ("b : " + b );
		log.info ("Calculated value : " + a/b);
		return b!=0?a/b:var.getValue(); 	
	}

	private Term convertSingleton(Term term, OutputVariable var) {
		
		Term base = term.getBaseTerm();
		if (base instanceof SingletonTerm) {
			log.info("CONVERTING SINGLETON !!!");
			return new TraingleSingleton (term, var.getMin(), var.getMax(), 20);
		}
		return term;
	}

}
 