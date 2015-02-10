package main.application.parser;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.application.Application;
import main.application.enviroment.Enviroment;
import main.application.functionblock.FunctionBlock;
import main.application.variables.InputVariable;
import main.application.variables.OutputVariable;

public class Parser extends ParserBase {
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
					app.functionBlocks.add(fb);
					expectWordForce("function block name").execute(p2 -> {
						if (isKeyword(p2.word)) {
							logFatal("function block name", "keyword " + p2.word);
						}
						fb.name = p2.word;
					});
					expectForce("var_input").execute(
							p2-> {
								while (!expect("end_var").isFound()) {
									InputVariable var = new InputVariable(fb);
									expectWordForce("variable name or 'end_var'").execute(
											p3 -> {
												if (isKeyword(p3.word)) {
													logFatal("variable name", "keyword " + p3.word);
												}
												var.setName(p3.word);
												expectForce(":").execute(
														p4 -> {
															expectOneOfForce(
																	ApplcationConfig
																			.getVariableTypes(),
																	"variable type").execute(
																	p5 -> {
																		var.setType(p5.word);
																		expectForce(";").execute(
																				p6 -> {
																					fb.input.add(var);
																				});
																	});
														});
											});
								}
							});
					expectForce("var_output").execute(
							p2-> {
								while (!expect("end_var").isFound()) {
									OutputVariable var = new OutputVariable(fb);
									expectWordForce("variable name or 'end_var'").execute(
											p3 -> {
												if (isKeyword(p3.word)) {
													logFatal("variable name", "keyword " + p3.word);
												}
												var.setName(p3.word);
												expectForce(":").execute(
														p4 -> {
															expectOneOfForce(
																	ApplcationConfig
																			.getVariableTypes(),
																	"variable type").execute(
																	p5 -> {
																		var.setType(p5.word);
																		expectForce(";").execute(
																				p6 -> {
																					fb.output.add(var);
																				});
																	});
														});
											});
								}
							});
					expectForce("end_function_block");
				});
		// app.saveJSON();
		}
		catch (Exception e) {
			this.logger.fatal.add(new LogEntry (e.toString(), this.countLines(pointer), pointer, this.countLinepos(pointer)));
			e.printStackTrace();
		}
		this.app.logger=this.logger;
	}

	private ParserAction expectEof() {
		this.moveOnTrailing();
		ParserAction pa = new ParserAction (this, pointer,pointer>=doc.length-1);
		return pa;
	}

	private boolean isKeyword(String word) {		
		String [] keywords = new String [] {
			"function_block", "end_function_block", "var_input", "var_output", "end_var"
		};
		
		for (String k : keywords) {
			if (k.equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}

	public void setEnviroment(Enviroment env) {
		this.app.setEnviroment(env);
		
	}
}
