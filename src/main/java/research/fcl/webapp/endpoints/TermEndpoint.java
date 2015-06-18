package research.fcl.webapp.endpoints;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import research.fcl.library.Application;
import research.fcl.library.functionblock.FunctionBlock;
import research.fcl.library.variable.term.Term;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.webapp.endpoints.dto.DefaultGsonMapper;
import research.fcl.webapp.endpoints.dto.TermPointsDto;


public class TermEndpoint {
	public static int resolution = 200;
	//responses
	public static String createErrorJson (String msg) {
		return"{\"error\":\"" + msg + "\"}";
	}
	public static String getTerms(HttpServletRequest request,
			HttpServletResponse response, Application app) {
		try{
			List<Term> terms = new ArrayList<Term>();
			String fbName = request.getParameter("fb");
			FunctionBlock fb = app.getFunctionBlock(fbName);
			if (fb == null) {
				return createErrorJson("Function block does not exists : " + fbName); 
			}
			String varName = request.getParameter("var");
			BaseFunctionVariable variable = fb.getVariable(varName);
			int res = resolution;
			try {
				res = Integer.parseInt(request.getParameter("res"));
			}
			catch (Exception e) {
				//do nothing, use default
			}
			String[] termNames = request.getParameterValues("term");
			if (termNames == null || termNames.length<1) {
				return createErrorJson ("Term names not provided");
			}
			for (String name : termNames) {
				System.out.println ("RETRIEVING TERM IN ENDPOINT : '" + name + "'");
				terms.add(variable.getTermFromAll(name));
			}
			return new DefaultGsonMapper (new TermPointsDto(variable, terms,res)).toJson();
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorJson (e.getMessage());
		}
	}
}
