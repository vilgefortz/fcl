package research.fcl.webapp.endpoints.mapper.tree;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

/*{
	  id          : "string" // will be autogenerated if omitted
	  text        : "string" // node text
	  icon        : "string" // string for custom
	  state       : {
	    opened    : boolean  // is the node open
	    disabled  : boolean  // is the node disabled
	    selected  : boolean  // is the node selected
	  },
	  children    : []  // array of strings or objects
	  li_attr     : {}  // attributes for the generated LI node
	  a_attr      : {}  // attributes for the generated A node
	}
*/

public class JsTreeNode {
	@Expose
	private String id;
	@Expose
	private String text;
	@Expose
	private String icon;
	@Expose
	private State state = new State ();
	@Expose
	private List<JsTreeNode> children= new ArrayList<JsTreeNode> ();
	@Expose
	private HtmlAttributes li_attr = new HtmlAttributes ();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public List<JsTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<JsTreeNode> children) {
		this.children = children;
	}
	public HtmlAttributes getLi_attr() {
		return li_attr;
	}
	public void setLi_attr(HtmlAttributes li_attr) {
		this.li_attr = li_attr;
	}
	public HtmlAttributes getA_attr() {
		return a_attr;
	}
	public void setA_attr(HtmlAttributes a_attr) {
		this.a_attr = a_attr;
	}
	@Expose
	private HtmlAttributes a_attr = new HtmlAttributes ();
	public class State {
		@Expose
		boolean opened;
		@Expose
		boolean disabled;
		@Expose
		boolean selected;
		public boolean isOpened() {
			return opened;
		}
		public void setOpened(boolean opened) {
			this.opened = opened;
		}
		public boolean isDisabled() {
			return disabled;
		}
		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}
}
