package research.fcl.webapp.endpoints.dto;

import java.io.FileNotFoundException;

import research.fcl.library.enviroment.Enviroment;
import research.fcl.library.enviroment.Variable;
import research.fcl.library.variables.InputVariable;
import research.fcl.library.variables.OutputVariable;

public class VarPointsMapper {
	public static String createErrorJson (String msg) {
		return"{\"error\":\"" + msg + "\"}";
	}
	
	public String map(Enviroment env, OutputVariable ovar, InputVariable ivar,
			int resolution) {
		try {
		Enviroment ecopy = new Enviroment ();
		//create copy of each variable to store all variables values
		env.forEach (v -> ecopy.add((new Variable (v.getName(), v.getValue())))); 
		//calculating resolution step
		//calculating pairs of values
		String result = new DefaultGsonMapper(new VarPointsDto(resolution, ivar, ovar)).toJson();
		//might be unnecessary due to not saving in session. Left for further possible modification of app/env into session bean
		ecopy.forEach(v -> env.getVariable(v.getName()).setValue(v.getValue()));
		return result;
		
		} catch (Exception e) {
			return createErrorJson(e.getMessage());
		}
	}
	
}
