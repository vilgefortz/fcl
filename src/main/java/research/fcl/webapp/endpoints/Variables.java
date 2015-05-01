package research.fcl.webapp.endpoints;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import research.fcl.library.Application;
import research.fcl.library.enviroment.Enviroment;
import research.fcl.webapp.endpoints.dto.DefaultGsonMapper;

public class Variables {
	public static String setVariable(HttpServletRequest request,
			HttpServletResponse response, Application app) {
		try {
			String varName = request.getParameter("name");
			String value = request.getParameter("value");
			HttpSession session = request.getSession();
			double val = Double.parseDouble(value);
			app.getEnv().getVariable(varName).setValue(val);
			session.setAttribute("app", app);
			session.setAttribute("env", app.getEnv());
			DefaultGsonMapper mapper = new DefaultGsonMapper(app.getEnv());
			return mapper.toJson();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "false";
		} catch (IOException e) {
			e.printStackTrace();
			return "false";
		} catch (Exception e) {
			e.printStackTrace();
			return "false";	
		}
	}
	public static String getEnviroment(HttpServletRequest request,
			HttpServletResponse response, Application app) {
		try {
			Enviroment env = app.getEnv();
			env.updateRanges();
			DefaultGsonMapper mapper = new DefaultGsonMapper(env);
			return mapper.toJson();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "false";
		} catch (IOException e) {
			e.printStackTrace();
			return "false";
		}
	}
}
