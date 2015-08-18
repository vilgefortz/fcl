package research.fcl.library.parser;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import research.fcl.library.Application;
import research.fcl.library.functionblock.FunctionBlock;

public class Parser extends BlockParser {
	public Parser() {

	}

	public Parser(String content) {
		this.document = content;
		this.doc = content.toCharArray();
	}

	public Parser(File file) {
		try {
			document = new Scanner(file).useDelimiter("\\Z").next();
		} catch (IOException e) {
			Logger.getGlobal().log(Level.SEVERE, "Cannot find file " + file);
		}
		if (document != null)
			doc = document.toCharArray();
		log("Opened file " + file.getAbsolutePath() + ", content:\n" + document);
	}
	public void parse() {
		try {
		this.eraseComments();
		if (this.doc.length==0) return;
		while (!expectEof ().isFound()) 
		expectForce("function_block").execute(	
				p1 -> {
					FunctionBlock fb = new FunctionBlock(app);
					fb.setEnv(app.getEnv());
					app.addFunctionBlock(fb);
					expectWordForce("function block name").execute(p2 -> {
						if (isKeyword(p2.word)) {
							logFatal("function block name", "keyword " + p2.word);
						}
						fb.name = p2.word;
					});
					this.parseInputVariables(fb);
					this.parseInlineVariables(fb);
					this.parseOutputVariables(fb);
					this.parseFuzzifyBlocks (fb);
					this.parseDefuzzifyBlocks (fb);
					this.parseRuleBlocks(fb);
					expectForce("end_function_block");
				});
		}
		catch (Exception e) {
			this.rollbackPointer();
			this.logger.fatal.add(new LogEntry (e.getMessage(), this.countLines(getPointer()), getPointer(), this.countLinepos(getPointer())));
			e.printStackTrace();
		}
		this.app.logger=this.logger;
	}

}
