package research.fcl.webapp.endpoints.dto;

import research.fcl.library.terms.Term;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.library.variables.OutputVariable;
import research.fcl.library.variables.InputVariable;

import com.google.gson.annotations.Expose;

public class TermPointsData {
	public TermPointsData(Term t, double[] points, BaseFunctionVariable var) {
		data = new double [points.length][2];
		for (int i=0; i<points.length; i++) {
			data[i][0]=points[i];
			data[i][1]=t.fun(points[i]);
		}
		if (var instanceof InputVariable) {
			label = t.getName() + " : " + t.fun  (var.getValue());
		}
		else {
			label = t.getName();
		}
	}

	@Expose
	double [][] data;
	@Expose 
	String label
	;
}
