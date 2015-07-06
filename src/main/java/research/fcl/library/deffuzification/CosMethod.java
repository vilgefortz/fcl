package research.fcl.library.deffuzification;

import java.util.List;
import java.util.logging.Logger;

import research.fcl.library.variable.term.Term;
import research.fcl.library.variable.term.types.SingletonTerm;
import research.fcl.library.variable.term.types.TraingleSingleton;
import research.fcl.library.variables.OutputVariable;
import research.fcl.library.accumulation.AccumulationMethod;

import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;

public class CosMethod extends DefuzzificationMethod {
	public static Logger log = Logger.getGlobal() ;
	public CosMethod(String method) {
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
		Term main = acculist.get(0);
		for (int i=1; i< acculist.size(); i++) {
			main = accuMethod.accumulate(main, acculist.get(i));
		}
		double a=0;
		double b=0;
		for (double point : main.getImportantPoints ()) {
			a+=main.fun(point) * point;	
			b+=main.fun(point);		
			System.out.println ("Calculating COS for point : " + point + ", a:" + a + ", b:" + b);
		}
		log.info ("Calcluating cogs for singleton only variable : " + a/b);
		return b!=0?a/b:0; 	
	}
}
 