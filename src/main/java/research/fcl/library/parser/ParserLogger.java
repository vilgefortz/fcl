package research.fcl.library.parser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class ParserLogger {
	//@Expose
	public List<LogEntry> info = new ArrayList<LogEntry>();
	@Expose
	public List<LogEntry> fatal = new ArrayList<LogEntry>();
	
	public String toJson() throws FileNotFoundException {

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.setPrettyPrinting().create();
		return gson.toJson(this);

	}
}
