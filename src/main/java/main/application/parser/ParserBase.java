package main.application.parser;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.application.Application;
import main.application.enviroment.Enviroment;
import main.application.enviroment.Variable;

public class ParserBase {

	public String document;
	public char[] doc;
	protected int pointer = 0;
	public int getPointer() {
		return pointer;
	}

	public void setPointer(int pointer) {
		this.pointer = pointer;
	}

	public Application app = new Application();
	public boolean done = false;
	public String word;
	public JsonLogger logger = new JsonLogger();
	public boolean fatalState;
	private int lastPointer;

	{
		this.app.setParser (this);
	}
	public ParserBase() {
		
	}

	public void logInfo(String exp, String found) {
		int line = countLines(pointer);
		int linepos = this.countLinepos(pointer);
		String info = "Info: Expected '" + exp + "', found '" + found
				+ "'";
		logger.info.add(new LogEntry(info, line, pointer, linepos));
		log(info);
	}

	protected int countLinepos(int pointer) {
		int i = 0;
		if (pointer == -1)
			return 0;
		if (pointer >= doc.length)
			pointer--;
		if (doc[pointer]=='\n' || doc[pointer]=='\r') return 0;
		while ((pointer--) != 0 && doc[pointer] != '\n' && doc[pointer] != '\r')
			i++;
		return i+1;
	}

	public void logFatal(String exp, String found) {
		this.fatalState = true;
		int line = countLines(pointer);
		int linepos = this.countLinepos(pointer);
		String info = "Fatal: Expected '" + exp + "', found '" + found
				+ "'";
		logger.fatal.add(new LogEntry(info, line, pointer, linepos));
		log(info);
	}

	public void log(String msg) {
		Logger.getGlobal().log(Level.INFO, msg);
	}

	public ParserAction expectReg(String regex, String logname) {
		lastPointer=pointer;
		int temp = pointer;
		regex = "(?is)^" + regex;
		moveOnTrailing();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(document.substring(pointer));
		String result = "";
		if (matcher.find())
			result = matcher.group();
		this.word = result;
		ParserAction action = new ParserAction(this, pointer + result.length(),
				!result.equals(""));
		logInfo(logname, result.equals("") ? this.getFirstText() : result);
		if (result.equals(""))
			pointer = temp;
		return action;
	}

	public ParserAction expect(String S) {
		lastPointer=pointer;
		int temp = pointer;
		moveOnTrailing();
		this.word = S;
		boolean found = false;
		if (pointer + S.length() <= doc.length)
			found = (S.equalsIgnoreCase(document.substring(pointer,
					pointer + S.length())));
		ParserAction action = new ParserAction(this, pointer + S.length(),
				found);
		logInfo(S, found ? S : this.getFirstText());
		if (!found)
			pointer = temp;
		return action;
	}

	protected ParserAction expectRegForce(String reg, String logname) {
		ParserAction pc = this.expectReg(reg, logname);
		if (!pc.isFound())
			logFatal(logname, this.word.equals("") ? this.getFirstText()
					: this.word);
		return pc;

	}

	private String getFirstText() {
		lastPointer=pointer;
		StringBuilder b = new StringBuilder();
		this.moveOnTrailing();
		for (int i = pointer; i < doc.length; i++) {
			if (Character.isWhitespace(doc[i]))
				break;
			b.append(doc[i]);
		}
		return b.toString();
	}

	protected ParserAction expectForce(String S) {
		ParserAction pc = this.expect(S);
		if (!pc.isFound()) {
			String firstWord = this.getFirstText();
			logFatal(S, firstWord);
		}
		return pc;
	}

	protected int countLines(int ptr) {
		int count = 0;
		if (doc.length <= ptr)
			ptr = doc.length - 1;
		for (int i = 0; i <= ptr; i++) {
			if (doc[i] == '\n')
				count++;
		}
		return count+1;
	}

	protected void eraseComments() {
		boolean flag = false;
		int debugLengthInitial = doc.length;
		for (int i = 0; i < doc.length; i++) {
			if (doc[i] == '/')
				if (i < doc.length - 1 && doc[i + 1] == '*') {
					flag = true;
				}
			if (doc[i] == '*')
				if (i < doc.length - 1 && doc[i + 1] == '/') {
					flag = false;
					doc[i] = ' ';
					doc[i + 1] = ' ';
				}
			if (flag) {
				if (doc[i] != '\n' && doc[i] != '\n') {
					doc[i] = ' ';
				}
			}
		}
		this.document = new String(doc);
		log("Erasing comments, initial l=" + debugLengthInitial + ", after l="
				+ doc.length + ", content after erasing : \n" + this.document);
	}

	protected void moveOnTrailing() {
		while (pointer < doc.length && Character.isWhitespace(doc[pointer]))
			pointer++;
	}

	public ParserAction expectWordForce(String name) {
		ParserAction pa = expectWord(name);
		if (!pa.isFound()) {
			logFatal(name, this.getFirstText());
		}
		return pa;
	}

	public ParserAction expectWordOrNumberForce(String name) {
		ParserAction pa = expectWordOrNumber(name);
		if (!pa.isFound()) {
			logFatal(name, this.getFirstText());
		}
		return pa;
	}
	public ParserAction expectWord(String name) {
		lastPointer=pointer;
		return this.expectReg("[a-z][a-z0-9_]*", name);
	}
	public ParserAction expectWordOrNumber(String name) {
		lastPointer=pointer;
		return this.expectReg("[a-z0-9_]+", name);
	}

	private String implode(String connector, String[] list) {
		if (list.length==0) return "";
		
		StringBuilder sb = new StringBuilder();
		for (String l : list) {
			sb.append(l).append(connector);
		}
		String result = sb.toString();
		Logger.getGlobal().info("IN IMPLODE : " + result);
		return result.substring(0,
				result.length() - connector.length());
	}

	protected ParserAction expectOneOfForce(String[] words, String name) {
		ParserAction pa = this.expectOneOf(words, name);
		if (!pa.isFound()) {
			logFatal(
					(new StringBuilder()).append(name).append(" : ")
							.append(this.implode(", ", words)).toString(),
					this.getFirstText());
		}
		return pa;
	}

	private ParserAction expectOneOf(String[] words, String name) {
		lastPointer=pointer;
		int temp = pointer;
		boolean found = false;
		moveOnTrailing();
		for (String S : words) {
			if (pointer + S.length() <= doc.length)
				found = (S.equalsIgnoreCase(document.substring(pointer, pointer
						+ S.length())));
			if (found) {
				this.word = S;
				break;
			}
		}
		ParserAction action = new ParserAction(this, pointer
				+ this.word.length(), found);
		logInfo((new StringBuilder()).append(name).append(" : ")
				.append(this.implode(", ", words)).toString(),
				found ? this.word : this.getFirstText());
		if (!found)
			pointer = temp;
		return action;
	}

	protected void logFatal(String string) {
		this.fatalState=true;
		this.logger.fatal.add(new LogEntry (string, this.countLines(pointer), pointer, this.countLinepos(pointer)));
		
	}

	protected ParserAction expectEof() {
		lastPointer=pointer;
		this.moveOnTrailing();
		ParserAction pa = new ParserAction (this, pointer,pointer>=doc.length-1);
		return pa;
	}

	public String[] getKeywords() {
		String [] keywords = new String [] {
				"function_block", "end_function_block", "var_input", "var_output", "end_var",
				"fuzzify", "end_fuzzify", "defuzzify", "end_defuzzify"
			};
		return keywords;
	}

	public boolean isKeyword(String word) {		
		String [] keywords = this.getKeywords();
		for (String k : keywords) {
			if (k.equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}

	public void setEnviroment(Enviroment env) {
		for (Variable v : env) {
			this.app.getEnv().getVariable(v.getName()).setValue(v.getValue());
		}
		for (Variable v : this.app.getEnv()) {
			v.forceCalc();
		}
 	}

	public String hasKeyword(String word) {
		String w = null;
		int pos = word.length()+1;
		for (String k : this.getKeywords()) {
			if (word.toLowerCase().contains(k)){
				int newpos = pos;
				if ((newpos = word.toLowerCase().indexOf(k))< pos) {
					pos=newpos;
					w=k;
				}
			};
		}
		return w;
	}
	public void rollbackPointer() {
		Logger.getGlobal().info("Rolling back pointer : old=" + pointer+ ", new=" + lastPointer + ".");
		this.pointer = this.lastPointer;
	}

}