package research.fcl.library.variables;

public class TermNotFoundException extends Exception {

	private String name;
	private String varName;

	public TermNotFoundException(String word, String vname) {
		this.name=word;
		this.varName = vname;
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Term not found : " + name + ", at variable : " + varName + ".";
	}

}
