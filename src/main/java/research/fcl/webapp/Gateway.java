package research.fcl.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import research.fcl.library.Application;
import research.fcl.library.enviroment.Enviroment;
import research.fcl.library.parser.Parser;

/**
 * Servlet implementation class Gateway
 */
@WebServlet("/Gateway")
public class Gateway extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Gateway() {
		super();
		//
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		writer.write("Working");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter pw = response.getWriter();
		String action = request.getParameter("action");
		if (action.equalsIgnoreCase("generateJson")) {
			String body = request.getParameter("data");
			Parser p = new Parser(body);
			
			Enviroment env = (Enviroment) session.getAttribute("env");
			p.parse();
			if (env == null) env = p.app.getEnv();
			p.setEnviroment(env);
			session.setAttribute("app", p.app);
			pw.write(p.app.toJson());
			return;
		}
		if (action.equalsIgnoreCase("setVariable")) {
			Application app = (Application) session.getAttribute("app");
			if (app==null) {
				pw.write("null");
				return;
			}
			String varName = request.getParameter("name");
			String value = request.getParameter("value");
			double val = Double.parseDouble(value);
			app.getEnv().getVariable(varName).setValue(val);
			session.setAttribute("app", app);
			session.setAttribute("env", app.getEnv());
			pw.write(app.toJson());
			return;
		}
	}

}
