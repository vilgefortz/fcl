package research.fcl.library.enviroment;

public class Enviroment extends VariableSet {
	public void updateRanges() {
		this.forEach(v -> v.calculateRange());
	}
}
