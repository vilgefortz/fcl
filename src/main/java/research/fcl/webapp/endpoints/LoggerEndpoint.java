package research.fcl.webapp.endpoints;

import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import research.fcl.library.Application;
import research.fcl.webapp.endpoints.dto.DefaultGsonMapper;

public class LoggerEndpoint {
	public static String getErrorLog(HttpServletRequest request,
			HttpServletResponse response, Application app) {
		if (app.logger != null && app.logger.fatal != null) {
			DefaultGsonMapper mapper = new DefaultGsonMapper(app.logger.fatal);
			try {
				return mapper.toJson();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "false";

			}
		}
		return "false";
	}
}
