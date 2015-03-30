package research.fcl.library.rules;

import research.fcl.library.andmethods.AndMethod;

import com.google.gson.annotations.Expose;

public class OrAction extends Action {

	@Expose
	String type = "or";
	@Expose
	Action left;
	@Expose
	Action right;
	private AndMethod andMethod;

	public OrAction(Action l, Action r, AndMethod andMethod) {
		this.left = l;
		this.right = r;
		this.andMethod = andMethod;
	}

	@Override
	public double getValue() {
		return andMethod.or(left.getValue(), right.getValue());
	}

}
