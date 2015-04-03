package research.fcl.webapp.endpoints.dto;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;

import research.fcl.library.variable.term.Term;
import research.fcl.library.variables.BaseFunctionVariable;

import com.google.gson.annotations.Expose;

public class TermPointsDto {
	@Expose
	public String error = "";
	@Expose
	public double[] points;
	private static final Logger logger = Logger.getLogger("TermPointsDto");
	public TermPointsDto(BaseFunctionVariable var, List<Term> terms, long res) {
		points = terms.get(0).getImportantPoints();
		for (int i = 1; i < terms.size(); i++) {
			points = ArrayUtils.addAll(points, terms.get(i)
					.getImportantPoints());
		}
		points[1] = points[1];
		points = removeDuplicates(points);
		logger.info("Punkty : " + points.length);
	}

	public static double[] removeDuplicates(double[] list) {
		double[] result = new double[list.length];
		Arrays.sort(list);
		int j = 0;
		for (int i = 0; i < list.length - 1; i++) {
			if (list[i] == list[i + 1]) {
				continue;
			}
			result[j++] = list[i];
		}
		return Arrays.copyOf(result, j);
	}
}