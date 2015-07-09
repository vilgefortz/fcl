package research.fcl.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		String body = request.getParameter("data");
		Parser p = new Parser(body);
		Enviroment env = (Enviroment) session.getAttribute("env");
		p.parse();
		if (env == null)
			env = p.getApplication().getEnv();
		p.setEnviroment(env);
		session.setAttribute("app", p.getApplication());
		pw.write("1");
		return;
	}

}
