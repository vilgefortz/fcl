package research.fcl.webapp.endpoints.dto;

import research.fcl.library.variable.term.Term;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.library.variables.OutputVariable;
import research.fcl.library.variables.InputVariable;

import com.google.gson.annotations.Expose;

public class VarPointsDto {
	public VarPointsDto(int resolution, InputVariable ivar, OutputVariable ovar) {
		double min = Math.min(ivar.getMin(), ivar.getValue());
		double max = Math.max(ivar.getMax(), ivar.getValue());
		double step = (max-min)/resolution;
		label = ovar.getName() + " (" + ivar.getName() + ")";
		int i = 0;
		this.data = new double [(int) ((max - min)/step)][2];
		for (double val = min; val <= max; val+=step) {
			ivar.setValue(val);
			data[i][0] = val;
			data[i][1] = ovar.getValue();
		}
	}

	@Expose
	double [][] data;
	@Expose 
	String label;
}
