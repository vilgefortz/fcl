package research.fcl.application.variables;

public class OutputVariableNotFoundException extends Exception {
	
	public OutputVariableNotFoundException(String name) {
		this.name = name;
	}	
	protected String name;
	@Override
	public String getMessage() {
	
		return "Output variable not found : '" + name + "'";
	}
}
