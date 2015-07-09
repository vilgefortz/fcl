package research.fcl.library.rules;

import java.util.logging.Logger;

import research.fcl.library.andmethods.AndMethod;
import research.fcl.library.functionblock.FunctionBlock;
import research.fcl.library.parser.utils.ParsingUtils;
import research.fcl.library.variable.term.Term;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.library.variables.exceptions.InlineVariableNotFoundException;
import research.fcl.library.variables.exceptions.InputVariableNotFoundException;
import research.fcl.library.variables.exceptions.TermNotFoundException;

public class DefaultActionFactory {
	protected String IS = "is";
	protected String OR = "or";
	protected String AND = "and";
	private Ruleblock ruleblock;
	public DefaultActionFactory (Ruleblock rb) {
		this.ruleblock=rb;
	}
	public Action createAction(String text, Rule r) throws RuleParsingException, InlineVariableNotFoundException, InputVariableNotFoundException, TermNotFoundException {
		text = text.trim();
		text = this.dropBrackets (text);
		int pos = findFirstNotEnclosed ("or",text);
		if (pos<0) { //no or, search for and 
			pos = findFirstNotEnclosed ("and",text);
				if (pos<0) {
					return this.parseSingleExpression(text,r);
				}
			return this.createAndAction (text,pos,r);
		}
		return this.createOrAction (text,pos,r);
	}

	private Action createOrAction(String text, int pos, Rule ru) throws RuleParsingException, InlineVariableNotFoundException, InputVariableNotFoundException, TermNotFoundException {
		String left = text.substring(0, pos);
		String right = text.substring(pos + OR.length());
		Action l = this.createAction(left,ru);
		Action r = this.createAction(right,ru);
		if (l==null || r ==null) throw new RuleParsingException("Unknown error at parsing rule expression");
		return new OrAction (l,r,this.getAndMethod());
	} 
	private Action createAndAction(String text, int pos, Rule ru) throws RuleParsingException, InlineVariableNotFoundException, InputVariableNotFoundException, TermNotFoundException {
		String left = text.substring(0, pos);
		String right = text.substring(pos+AND.length());
		Action l = this.createAction(left,ru);
		Action r = this.createAction(right,ru);
		if (l==null || r ==null) throw new RuleParsingException("Unknown error at parsing rule expression");
		return new AndAction (l,r,this.getAndMethod());
	}
	protected AndMethod getAndMethod() {
		return this.ruleblock.getAndMethod();
	}

	private Action parseSingleExpression(String text, Rule r) throws RuleParsingException, InlineVariableNotFoundException, InputVariableNotFoundException, TermNotFoundException {
		FunctionBlock fb = this.ruleblock.getFunctionBlock();
		String varName = ParsingUtils.getFirstWord (text);
		if (varName.equals("")) throw new RuleParsingException("Expected variable name");
		BaseFunctionVariable v = fb.getLeftVariable(varName);
		logger.info("Found left hand variable " + varName);
		r.addDepenedency (v);
		v.addDependendRule(r);
		String is = ParsingUtils.getFirstWord(text=text.substring(varName.length()).trim());
		if (!is.equalsIgnoreCase(IS)) throw new RuleParsingException("Expected keyword 'is' after variable '" + varName + "'");
		String rightSide = text=text.substring(IS.length()).trim();
		Term t = this.parseTerm (rightSide,v,r);
		return new TermAction (t,v);
	}
	
	private Term parseTerm(String text, BaseFunctionVariable v, Rule r) throws RuleParsingException, TermNotFoundException {
		String word = ParsingUtils.getFirstWord(text = text.trim());
		if (v.hasTerm (word)) {
			String w =text.substring(word.length()).trim();
			if (!w.equals("")) throw new RuleParsingException("Unexpected '" + w + "' after term '" + word);
			return v.getTerm(word);
		}
		throw new RuleParsingException("Term '" + word + "' is not defined for variable '" + v.getName() + "'");
	}
	private static final Logger logger = Logger.getLogger("DefaultActionFactory");
	
	private int findFirstNotEnclosed(String text,String main) throws RuleParsingException {
		Logger.getGlobal().info("Searching for '" + text + "' in '" + main+ "'.");
		char [] c = main.toCharArray();
		int count = 0;
		for (int i=0; i<c.length-text.length();i++) {
			if (c[i]=='(') count ++;
			if (c[i]==')') count --;
			if (count<0) throw new RuleParsingException("Unexpected ')', check your rule definition");
			if (count>0) continue;
			if ((new String (c,i,text.length())).equalsIgnoreCase(text)) {
				
				Logger.getGlobal().info("Found '" + text + "' in '" + main+ "' at position " + i);
				return i;
			};
			
		}
		if (count>0) throw new RuleParsingException("Unexpected '(', check your rule definition");
		return -1;
	}

	
	private String dropBrackets(String text) {
		if (text.startsWith("(") && text.endsWith(")")) {
			return text.substring(1, text.length()-1).trim();
		}
		return text;
	}

}
