package research.fcl.library.andmethods;

public class AndMethodNotFoundException extends Exception {

	private static final long serialVersionUID = 8269654302803332161L;
	private String name;

	public AndMethodNotFoundException(String name) {
		this.name=name;
	}
	@Override
	public String getMessage() {
		return "And method not found : '"+name+"'";
	}

}
