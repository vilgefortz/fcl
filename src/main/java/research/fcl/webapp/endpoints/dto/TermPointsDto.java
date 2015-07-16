package research.fcl.webapp.endpoints.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;

import research.fcl.library.terms.Term;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.library.variables.OutputVariable;
import research.fcl.library.variables.InputVariable;

import com.google.gson.annotations.Expose;	

public class TermPointsDto {
	@Expose
	public String error = "";
	
	public double[] points;
	@Expose 
	public List<TermPointsData> terms= new ArrayList<TermPointsData> ();
	//getAccumulationTerm ()
	private BaseFunctionVariable var;
	private int res;
	private static final Logger logger = Logger.getLogger("TermPointsDto");
	public TermPointsDto(BaseFunctionVariable var, List<Term> terms, int res) {
		points = terms.get(0).getImportantPoints();
		for (int i = 1; i < terms.size(); i++) {
			points = ArrayUtils.addAll(points, terms.get(i)
					.getImportantPoints());
		}
		this.var = var;
		this.res =res;
		points = addResPoints (points);
		for (Term t : terms) {
			this.terms.add(new TermPointsData (t, points, var));
		}
	}	

	private double[] addResPoints(double[] points2) {
		double min = Math.min (var.getMin(), var.getValue());
		double max = Math.max (var.getMax(), var.getValue());
		double [] respoints = new double [(int) res];
		respoints[0]=min;
		for (int i=1; i<res ; i++) {
			double delta = (max-min)/res;
			respoints[i]=respoints[i-1]+delta;
		}
		return removeDuplicates (ArrayUtils.addAll(respoints, points2));
	}

	public double[] removeDuplicates(double[] list) {
		double[] result = new double[list.length];
		Arrays.sort(list);
		int j = 1;
		result [0]=list[0];
		for (int i = 1; i < list.length; i++) {
			if (list[i] == list[i - 1]) {
				continue;
			}
			result[j++] = list[i];
		}
		return Arrays.copyOf(result, j);
	}
}

