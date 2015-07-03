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
import research.fcl.library.enviroment.Variable;
import research.fcl.library.variable.term.Term;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.library.functionblock.FunctionBlock;
import research.fcl.webapp.endpoints.dto.DefaultGsonMapper;
import research.fcl.webapp.endpoints.dto.VarPointsMapper;
import research.fcl.library.variables.OutputVariable;
import research.fcl.library.variables.InputVariable;

public class Variables {
	public static int resolution = 200;
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
			System.out.println (termsList);
			termsList.forEach (e -> System.out.println ("RETRIEVED TERM FOR '" + varName + "' : '" + (e!=null?e.getName():"null?") + "'"));
			DefaultGsonMapper mapper = new DefaultGsonMapper(termsList);
			return mapper.toJson();
		} catch (Exception e) {
			e.printStackTrace();
			return "false";	
		}
	}
	public static String getVariable3DFunction (HttpServletRequest request,
			HttpServletResponse response, Application app) {
		try {
			String ivar0Name = request.getParameter("ivar0"); //input variable 0
			String ivar1Name = request.getParameter("ivar1"); //input variable 1
			String ovarName = request.getParameter("ovar"); //output / inline variable
			System.out.println (ovarName);
			String fbName = request.getParameter("fb"); //output / inline variable
			String resString = request.getParameter("res"); //output / inline variable
			int res = resolution;
			try {
				res = Integer.parseInt(resString);
			} catch (Exception e) {
				//nothing, using default
			}
			FunctionBlock fb = app.getFunctionBlock(fbName);
			if (fb == null) return "{\"error\":\"no fb " + fbName + " found\"}";
			OutputVariable ovar = (OutputVariable)fb.getRightVariable(ovarName);
			InputVariable ivar0 = (InputVariable)fb.getLeftVariable(ivar0Name);
			InputVariable ivar1 = (InputVariable)fb.getLeftVariable(ivar1Name);
			return new VarPoints3DDTO ().map(app.getEnv(),ovar,new InputVariable [] {ivar0, ivar1}, res		);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"" + e.getMessage() + "\"}";
		}
	}
	public static String getVariableFunction (HttpServletRequest request,
			HttpServletResponse response, Application app) {		
		try {
			String ivarName = request.getParameter("ivar"); //input variable
			String ovarName = request.getParameter("ovar"); //output / inline variable
			String fbName = request.getParameter("fb"); //output / inline variable
			FunctionBlock fb = app.getFunctionBlock(fbName);
			if (fb == null) return "{\"error\":\"no fb " + fbName + " found\"}";
			OutputVariable ovar = (OutputVariable)fb.getRightVariable(ovarName);
			InputVariable ivar = (InputVariable)fb.getLeftVariable(ivarName);
			return new VarPointsMapper ().map(app.getEnv(),ovar,ivar,resolution);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"" + e.getMessage() + "\"}";
		}
	}
	public static String getVariables (HttpServletRequest request,
			HttpServletResponse response, Application app) {
		try {
			String type = request.getParameter("type");
			String fbName = request.getParameter("fb");
			FunctionBlock fb = app.getFunctionBlock(fbName);
			List<String> vars = new  ArrayList<String> ();	
			if (type == null) { //collecting all variable names
				fb.input.forEach (in -> vars.add(in.getName()));
				fb.output.forEach (out -> vars.add(out.getName()));
				fb.inline.forEach (line -> vars.add(line.getName()));
			} else {
				if (type.equals("input")) {
					fb.input.forEach (in -> vars.add(in.getName()));
					}
				if (type.equals("output")) {
						fb.output.forEach (out -> vars.add(out.getName()));
					}
				if (type.equals("inline")) {
						fb.inline.forEach (line -> vars.add(line.getName()));
					}
				}
			if (vars != null) {
				DefaultGsonMapper mapper = new DefaultGsonMapper(vars);
				return mapper.toJson();
			}
			return "false";	
		}
		catch (Exception e) {
			e.printStackTrace();
			return "false";	
		}
	}
}
