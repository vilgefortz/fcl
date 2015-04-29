package research.fcl.webapp.endpoints.dto;

import research.fcl.library.variable.term.Term;

import com.google.gson.annotations.Expose;

public class TermPointsData {
	public TermPointsData(Term t, double[] points) {
		data = new double [points.length][2];
		for (int i=0; i<points.length; i++) {
			data[i][0]=points[i];
			data[i][1]=t.fun(points[i]);
		}
		label = t.getName();
	}

	@Expose
	double [][] data;
	@Expose 
	String label
	;
}
