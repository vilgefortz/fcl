package research.fcl.application.parser;

import java.util.logging.Logger;

public class Console {
	public static void log (String message) {
		Logger.getGlobal().fine(message);
	}

	public static void log(String message, int line, int pointer) {
		log (message + ", at line " + line + ", pos : " + pointer + ".");
	}

	public static void printLog() {
		// TODO Auto-generated method stub
		
	}
}
