package research.fcl.application.parser;

public class ParserAction {
	private ParserBase parser;
	private boolean found;
	public boolean isFound() {
		return this.parser.fatalState||found;
	}
	public ParserAction (ParserBase parser, int pos, boolean found) {
		this.found = found;
		this.parser = parser;
		if (found) this.parser.setPointer(pos);
	}
	public void execute (ParserFunction func) {
		if (!this.parser.fatalState && found) func.commit(this.parser);
	}
}
