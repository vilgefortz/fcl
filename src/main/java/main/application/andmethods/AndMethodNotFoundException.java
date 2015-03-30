package main.application.andmethods;

public class AndMethodNotFoundException extends Exception {

	private String name;

	public AndMethodNotFoundException(String name) {
		this.name=name;
	}
	@Override
	public String getMessage() {
		return "And method not found : '"+name+"'";
	}

}
