package main.application.variable.term;

import java.util.Scanner;

public class TermDefinitionNotRecognisedException extends Exception {
	private String definition;

	public TermDefinitionNotRecognisedException(String definition) {
		Scanner scanner = new Scanner(definition);
		this.definition = scanner.nextLine();
	}

	public String getMessage () {
		return ("Term definition : '"+ definition +"' not regonised.");
	}
}
