package research.fcl.application.rules;

public class RuleParsingException extends Exception {

	
	private String msg;

	public RuleParsingException(String string) {
		this.msg = string;
		}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return msg;
	}
}
