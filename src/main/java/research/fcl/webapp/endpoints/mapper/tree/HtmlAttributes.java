package research.fcl.webapp.endpoints.mapper.tree;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HtmlAttributes {
	@SerializedName("class")
	@Expose
	public String classAttr;
	@Expose
	public String value;
}
