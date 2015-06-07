package research.fcl.webapp.endpoints;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import research.fcl.library.Application;
import research.fcl.library.enviroment.Enviroment;
import research.fcl.library.variable.term.Term;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.library.functionblock.FunctionBlock;
import research.fcl.webapp.endpoints.dto.DefaultGsonMapper;

import research.fcl.library.variables.OutputVariable;
import research.fcl.library.variables.InputVariable;

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
	public static String removeVariable(HttpServletRequest request,
			HttpServletResponse response, Application app) {
		try {
			String varName = request.getParameter("name");
			HttpSession session = request.getSession();
			app.getEnv().removeVariable(varName);
			session.setAttribute("app", app);
			session.setAttribute("env", app.getEnv());
		} catch (Exception e) {
			e.printStackTrace();
			return "false";	
		}
		return "ok";
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
	public static String getTerms (HttpServletRequest request,
			HttpServletResponse response, Application app) {
		try {
			String varName = request.getParameter("variable");
			String fbName = request.getParameter("fb");
			HttpSession session = request.getSession();
			FunctionBlock fb = app.getFunctionBlock(fbName);
			BaseFunctionVariable variable = fb.getVariable(varName);
			List<Term> termsList = variable.getAllTerms();
			DefaultGsonMapper mapper = new DefaultGsonMapper(termsList);
			return mapper.toJson();
		} catch (Exception e) {
			e.printStackTrace();
			return "false";	
		}
		
	}
}
