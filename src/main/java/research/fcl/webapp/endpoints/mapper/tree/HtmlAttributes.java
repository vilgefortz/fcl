package research.fcl.webapp.endpoints.mapper.tree;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HtmlAttributes {
	@SerializedName("class")
	@Expose
	public String classAttr;
	@Expose
	public String value;
	@Expose
	public String name;
	@SerializedName("data-variable")
	@Expose
	public String variable;
	@SerializedName("data-function_block")
	@Expose
	public String functionBlock;
}
