package research.fcl.webapp;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class Controler {
	public static final List<String> modules = new ArrayList<String> ();
	static {
		modules.add("admin");
	}
	public static final String DEFAULT = "WEB-INF/include/defautt.jsp";
	public static String getContentUrl(HttpServletRequest request) {
		return "";
	}
	public static String createUrl (String module, String action) {
		return (new StringBuilder("index.jsp?m=")).append(module).append("&a=").append(action).toString();
	};
}
