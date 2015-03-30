package main.application.accumulation;

public class AccumulationMethodNotRecognisedException extends Exception {

	private String name;
	public AccumulationMethodNotRecognisedException(String method) {
		this.name= method;
	}
	
	@Override
	public String getMessage() {
		return "Accumulation method '" + name + "' not recognised";
	}

}
