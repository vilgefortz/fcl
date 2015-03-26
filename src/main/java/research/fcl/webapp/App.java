 package research.fcl.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import research.fcl.library.Application;
import research.fcl.webapp.endpoints.LoggerEndpoint;
import research.fcl.webapp.endpoints.Variables;

/**
 * Servlet implementation class Gateway
 */
@WebServlet("/App")
public class App extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public App() {
		super();
		//
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost (request,response);
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String actionString = request.getParameter("action");
		PrintWriter pw = response.getWriter(); 
		Application app = (Application) session.getAttribute("app");
		if (app==null) {
			pw.print("false");
			return;
		}
		Map<String,ApplicationResponseAction> dispatchMap = new HashMap<String,ApplicationResponseAction> ();
		dispatchMap.put ("setVariable", Variables::setVariable);
		dispatchMap.put ("errorLog", LoggerEndpoint::getErrorLog);
		dispatchMap.put ("getEnviroment", Variables::getEnviroment);
		
		ApplicationResponseAction action = dispatchMap.get(actionString);
		if (action != null) pw.write(action.action(request, response, app));
		else pw.write("404");
		
	}

}
