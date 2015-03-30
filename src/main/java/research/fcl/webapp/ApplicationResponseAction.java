package research.fcl.webapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import research.fcl.library.Application;

public interface ApplicationResponseAction {
	public String action (HttpServletRequest request, HttpServletResponse response, Application app);
}
