package research.fcl.library.accumulation;

public class AccumulationMethodNotRecognisedException extends Exception {

	private static final long serialVersionUID = 602423792146268711L;
	private String name;
	public AccumulationMethodNotRecognisedException(String method) {
		this.name= method;
	}
	
	@Override
	public String getMessage() {
		return "Accumulation method '" + name + "' not recognised";
	}

}
