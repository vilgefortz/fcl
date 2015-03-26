package research.fcl.library.variable.term;

public class TermDefinitionHasKeywordException extends Exception {

	private String w;

	public TermDefinitionHasKeywordException(String w) {
		this.w = w;
	}
	
	@Override
	public String getMessage() {
		return "Term definition cannot contain keyword : '" + w +"', are you missing semicolon?";
	}

}
