package research.fcl.webapp.endpoints;

import com.google.gson.annotations.Expose;

import research.fcl.library.enviroment.Enviroment;
import research.fcl.library.enviroment.Variable;
import research.fcl.library.variables.InputVariable;
import research.fcl.library.variables.OutputVariable;
import research.fcl.webapp.endpoints.dto.DefaultGsonMapper;
import research.fcl.webapp.endpoints.dto.VarPointsDto;

public class VarPoints3DDTO {
	@Expose
	double [][] x,y,z;
	
	public String map(Enviroment env, OutputVariable ovar, InputVariable[] ivars,
			int resolution) {
		try {
		Enviroment ecopy = new Enviroment();
		// create copy of each variable to store all variables values
		env.forEach(v -> ecopy.add((new Variable(v.getName(), v.getValue()))));
		// calculating resolution step
		// calculating pairs of values
		double hmin = Math.min (ivars[0].getMin(), ivars[0].getValue());
		double wmin = Math.min (ivars[1].getMin(), ivars[1].getValue());
		double hmax = Math.max (ivars[0].getMax(), ivars[0].getValue());
		double wmax = Math.max (ivars[1].getMax(), ivars[1].getValue());
		double hival = ((hmax-hmin) / resolution );//interval height
		double wival = ((wmax-wmin) / resolution );//interval height
		x = new double [resolution][resolution];
		y = new double [resolution][resolution];
		z = new double [resolution][resolution];
		for (int i=0; i<resolution; i++) {
			for (int j=0; j<resolution; j++) {
				x[i][j] = hmin + j*hival;
			}
		}
		for (int i=0; i<resolution; i++) {
			double val = wmin + i*wival;
			for (int j=0; j<resolution; j++) {
				y[i][j] = val;
			}
		}
		for (int i=0; i<resolution; i++) {
			for (int j=0; j<resolution; j++) {
				ivars[0].setValue(x[i][j]);
				ivars[1].setValue(y[i][j]);
				z[i][j] = ovar.getValue();
			}
		}
		String result = new DefaultGsonMapper(this).toJson();
		// might be unnecessary due to not saving in session. Left for
		// further possible modification of app/env into session bean
		ecopy.forEach(v -> env.getVariable(v.getName()).setValue(
				v.getValue()));
		return "{\"error\":\"\",\"vars\":[" + result + "]}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"" + e.getMessage() + "\"}";
		}
	}

}
