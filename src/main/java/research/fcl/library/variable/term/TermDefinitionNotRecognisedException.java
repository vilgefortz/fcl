package research.fcl.library.variable.term;

import java.util.Scanner;

public class TermDefinitionNotRecognisedException extends Exception {
	private String definition;

	public TermDefinitionNotRecognisedException(String definition) {
		Scanner scanner = new Scanner(definition);
		this.definition = scanner.nextLine();
		scanner.close();
	}

	public String getMessage () {
		return ("Term definition : '"+ definition +"' not regonised.");
	}
}
