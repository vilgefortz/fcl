package research.fcl.webapp.endpoints.dto;

import java.io.FileNotFoundException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DefaultGsonMapper {
	private Object obj;

	public DefaultGsonMapper(Object obj) {
		this.obj = obj;
	}

	public String toJson() throws FileNotFoundException {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		return gson.toJson(obj);

	}
}
