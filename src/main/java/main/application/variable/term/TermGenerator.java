package main.application.variable.term;

import main.application.Application;

//used with chain responsibility
public abstract class TermGenerator {
	private Application app;
	
	public Term generateTerm (String termName, String definition) throws TermDefinitionNotRecognisedException, TermDeclarationErrorException {
		if (this.isResponsible(definition)) {
			return this.generate(termName, definition);
		}
		else {
			return null;
		}
	}
	protected abstract Term generate(String termName, String definition) throws TermDeclarationErrorException;
	protected abstract boolean isResponsible(String definition) throws TermDeclarationErrorException;
	
	public void setApplication(Application app) {
		this.app=app;
	}	
	
}