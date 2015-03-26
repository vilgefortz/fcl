package research.fcl.application.rules;

import research.fcl.application.andmethods.AndMethod;

import com.google.gson.annotations.Expose;

public class AndAction extends Action {

	@Expose
	String type = "and";
	@Expose
	Action left;
	@Expose
	Action right;
	private AndMethod andMethod;

	public AndAction(Action l, Action r, AndMethod andMethod) {
		this.left = l;
		this.right = r;
		this.andMethod = andMethod;
	}

	@Override
	public double getValue() {
		return andMethod.and(left.getValue(), right.getValue());
	}

}
