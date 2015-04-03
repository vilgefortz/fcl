package research.fcl.library.functionblock;

public class VariableNotFoundException extends RuntimeException {

	private String name;

	public VariableNotFoundException(String name) {
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Variable not found : " + name;
	}

}
