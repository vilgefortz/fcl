package research.fcl.application.deffuzification;

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
