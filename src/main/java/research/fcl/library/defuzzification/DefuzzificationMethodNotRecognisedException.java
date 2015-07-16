package research.fcl.library.defuzzification;

public class DefuzzificationMethodNotRecognisedException extends Exception {

	String method;
	public DefuzzificationMethodNotRecognisedException(String method) {
		this.method = method;
	}
	@Override
	public String getMessage() {
		return "Deffuzification method '" + method + "' not recognised";
	}
}
