package main.application.parser;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.application.Application;

public class ParserBase {

	public String document;
	public char[] doc;
	public int pointer = 0;
	public Application app = new Application();
	public boolean done = false;
	public String word;
	public JsonLogger logger = new JsonLogger();
	public boolean fatalState;

	public ParserBase() {
		super();
	}

	public void logInfo(String exp, String found) {
		int line = countLines(pointer);
		int linepos = this.countLinepos(pointer);
		String info = "Info: Expected '" + exp + "', found '" + found
				+ "' at line : " + line + ", pos : " + linepos;
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
				+ "' at line : " + line + ", pos : " + linepos;
		logger.fatal.add(new LogEntry(info, line, pointer, linepos));
		log(info);
	}

	public void log(String msg) {
		Logger.getGlobal().log(Level.INFO, msg);
	}

	public ParserAction expectReg(String regex, String logname) {
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

	private ParserAction expectRegForce(String reg, String logname) {
		ParserAction pc = this.expectReg(reg, logname);
		int line = this.countLines(pointer);
		if (!pc.isFound())
			logFatal(logname, this.word.equals("") ? this.getFirstText()
					: this.word);
		return pc;

	}

	private String getFirstText() {
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

	public ParserAction expectWord(String name) {
		return this.expectReg("[a-z][a-z0-9_]*", name);
	}

	private String implode(String connector, String[] list) {
		StringBuilder sb = new StringBuilder();
		for (String l : list) {
			sb.append(l).append(connector);
		}
		String result = sb.toString();
		return result.substring(result.length() - connector.length(),
				result.length());
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

}