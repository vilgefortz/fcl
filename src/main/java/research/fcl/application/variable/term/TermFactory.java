package research.fcl.application.variable.term;

import java.util.ArrayList;
import java.util.List;

import research.fcl.application.Application;

public class TermFactory {

	protected List<TermGenerator> termGenerators = new ArrayList<TermGenerator> ();
	protected Application app;
	
	public TermFactory addTermGenerator (TermGenerator tg) {
			tg.setApplication(app);
			this.termGenerators.add(tg);
			return this;
	}
	public Term generateTerm(String termName, String word) throws TermDefinitionNotRecognisedException, TermDeclarationErrorException, TermDefinitionHasKeywordException {
		//remove last semicolon
		
		word=word.trim().substring(0, word.length()-1);
		if (word.trim().equals(""))
			throw new TermDeclarationErrorException(
					"Expected term definition");
		String w = null;
		if (( w = app.getParser().hasKeyword(word))!=null) throw new TermDefinitionHasKeywordException (w);
		for (TermGenerator tg : this.termGenerators) {
			if (tg.isResponsible(word))
			return tg.generate(termName, word);
		}
		throw new TermDefinitionNotRecognisedException(word);
	}

}
