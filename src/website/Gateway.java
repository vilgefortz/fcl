package website;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.application.enviroment.Enviroment;
import main.application.parser.Parser;

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
		// TODO Auto-generated constructor stub
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
		String action = request.getParameter("action");
		if (action.equalsIgnoreCase("generateJson")) {
			HttpSession session = request.getSession();
			PrintWriter pw = response.getWriter();
			String body = request.getParameter("data");
			Parser p = new Parser(body);
			
			Enviroment env = (Enviroment) session.getAttribute("env");
			if (env == null) {
				env=new Enviroment();
			}
			p.setEnviroment(env);
			p.parse();
			session.setAttribute("app", p.app);
			pw.write(p.app.toJson());
			return;
		}
	}

//	protected String getBodyContent(HttpServletRequest request)
//			throws IOException {
//		String body = null;
//		StringBuilder stringBuilder = new StringBuilder();
//		BufferedReader bufferedReader = null;
//
//		try {
//			InputStream inputStream = request.getInputStream();
//			if (inputStream != null) {
//				bufferedReader = new BufferedReader(new InputStreamReader(
//						inputStream));
//				char[] charBuffer = new char[128];
//				int bytesRead = -1;
//				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//					stringBuilder.append(charBuffer, 0, bytesRead);
//				}
//			} else {
//				stringBuilder.append("");
//			}
//		} catch (IOException ex) {
//			throw ex;
//		} finally {
//			if (bufferedReader != null) {
//				try {
//					bufferedReader.close();
//				} catch (IOException ex) {
//					throw ex;
//				}
//			}
//		}
//
//		return stringBuilder.toString();
//
//	}

}
