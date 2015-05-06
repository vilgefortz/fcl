package research.fcl.library.enviroment;



public class Enviroment extends VariableSet{
	public void updateRanges () {
		this.forEach (
			v -> {
				v.calculateRange ();
			}
		);
	}
	public void removeVariable (String name) {
		int index = this.indexOf (new Variable (name));
		if (index < 0) return;
		this.remove (index);
	}
}	
