package research.fcl.library.terms;

public class TermDeclarationErrorException extends Exception {

	private String msg;

	public TermDeclarationErrorException(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}

}
