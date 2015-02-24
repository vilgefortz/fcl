package main.application.parser;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.application.functionblock.FunctionBlock;
import main.application.functionblock.Ruleblock;
import main.application.variables.BaseFunctionVariable;
import main.application.variables.InlineVariable;
import main.application.variables.InputVariable;
import main.application.variables.OutputVariable;

public class Parser extends ParserBase {
	public BaseFunctionVariable var;
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
									expectWordForce("variable name or 'end_var'").execute(
											p3 -> {
												if (isKeyword(p3.word)) {
													logFatal("variable name", "keyword " + p3.word);
												}
												InputVariable var = new InputVariable(p3.word,fb);
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
					expect("var").execute(
							p2-> {
								if (expect ("_output").isFound()) {
									rollbackPointer();
									pointer -=3;
								}
								else 
								while (!expect("end_var").isFound()) {
									expectWordForce("variable name or 'end_var'").execute(
											p3 -> {
												if (isKeyword(p3.word)) {
													logFatal("variable name", "keyword " + p3.word);
												}
												InlineVariable var = new InlineVariable(p3.word,fb);
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
																					fb.inline.add(var);
																				});
																	});
														});
											});
								}
							});
					expectForce("var_output").execute(
							p2-> {
								while (!expect("end_var").isFound()) {
									
									expectWordForce("variable name or 'end_var'").execute(
											p3 -> {
												if (isKeyword(p3.word)) {
													logFatal("variable name", "keyword " + p3.word);
												}
												OutputVariable var = new OutputVariable(p3.word,fb);
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
					expectForce("fuzzify").execute(
							p2-> {
								do {
									 expectWordForce("variable name'").execute( p3 -> {
										 if (isKeyword(p3.word)) {
												logFatal("variable name", "keyword " + p3.word);
										 }
										 String varName = p3.word;
										try { 
											var = fb.input.getInputVariable(varName);
										} catch (Exception e) {
											this.rollbackPointer();
											logFatal (e.getMessage());
											e.printStackTrace();
										}
										 while (!p2.expect("end_fuzzify").isFound()) {
											 expectForce("term").execute(p4-> {
												 expectWordForce("term name'").execute( p5 -> {
													 if (isKeyword(p5.word)) {
															logFatal("variable name", "keyword " + p3.word);
													 }
													 String termName = p5.word;
													 expectForce (":=").execute(p6->{
														 expectRegForce("(?s).*?;", "term definition").execute(p7-> {
																try {
																	var.addTerm (this.app.termFactory.generateTerm (termName,p7.word));
																} catch (Exception e) {
																	this.rollbackPointer();
																	logFatal (e.getMessage());
																	e.printStackTrace();					}
																
														 });
													 });
												 });
											 });
										 }
									 });
								} while (expect("fuzzify").isFound() && !this.fatalState);
							});
					expectForce("defuzzify").execute(
							p2-> {
								do {
									 expectWordForce("variable name'").execute( p3 -> {
										 if (isKeyword(p3.word)) {
												logFatal("variable name", "keyword " + p3.word);
										 }
										 String varName = p3.word;
										 try {
											var = fb.getRightVariable(varName);
										} catch (Exception e) {
											this.rollbackPointer();
											logFatal (e.getMessage());
											e.printStackTrace();
					}
										 while (!p2.expect("end_defuzzify").isFound()) {
											 expectOneOfForce(new String [] {"term", "accu", "method", "default"},
													 "term or defizzify details").execute(p4-> {
												 if (p4.word.equalsIgnoreCase("term")) {
												 expectWordForce("term name'").execute( p5 -> {
													 if (isKeyword(p5.word)) {
															logFatal("variable name", "keyword " + p3.word);
													 }
													 String termName = p5.word;
													 expectForce (":=").execute(p6->{
														 expectRegForce("(?s).*?;", "term definition").execute(p7-> {
															 try {
																var.addTerm (this.app.termFactory.generateTerm (termName,p7.word));
															} catch (Exception e) {
																this.rollbackPointer();
																logFatal (e.getMessage());
																e.printStackTrace();
															}
														 });
													 });
												 });
												 }
												 if (p4.word.equalsIgnoreCase("accu")) {
													 expectForce (":").execute(p6->{ 
														 expectOneOfForce(app.getAccuMethodsNames(), "Accumulation method").execute(p7 -> {
															 String method = p7.word;
															 expectForce(";").execute(
																		p8 -> {
																			try {
																				((OutputVariable)var).setAccuMethod(method);
																			} catch (Exception e) {
																				this.rollbackPointer();
																				logFatal (e.getMessage());
																				e.printStackTrace();;
																			}
																		});
														 });
													 });
												 }
												 if (p4.word.equalsIgnoreCase("method")) {
													 expectForce (":").execute(p6->{ 
														 expectOneOfForce(app.getDeffuMethodsNames(), "Defuzzification method").execute(p7 -> {
															 String method = p7.word;
															 expectForce(";").execute(
																		p8 -> {
																			try {
																				((OutputVariable)var).setDefuzzificationMethod(method);
																			} catch (Exception e) {
																				this.rollbackPointer();
																				logFatal (e.getMessage());
																				e.printStackTrace();;
																			}
																		});
														 });
													 });
												 }
												 if (p4.word.equalsIgnoreCase("default")) {
													 expectForce(":=").execute(p5-> {
														 expectRegForce("-?[0-9]*\\.?[0-9]*", "Numeric value").execute(p6 -> {
															 double val = Double.parseDouble(p6.word);
															 	expectForce(";").execute(p7-> { 
															 		var.setDefault (val);
															 });
														 });
													 });
																 
												 }
											 });
										 }
									 });
								} while (expect("defuzzify").isFound() && !this.fatalState);
							});
					expectForce("ruleblock").execute(
							p2-> {
								do {
									 expectWordForce("ruleblock name'").execute( p3 -> {
										 if (isKeyword(p3.word)) {
												logFatal("ruleblock name", "keyword " + p3.word);
										 }
										 String ruleBlockName = p3.word;
										 Ruleblock rb = new Ruleblock(fb);
										 rb.setName (ruleBlockName);
										 fb.ruleblocks.add(rb);
										 while (!p2.expect("end_ruleblock").isFound()) {
											 expectOneOfForce(new String [] {"rule", "and"},
													 "rule or settings").execute(p4-> {
												if (p4.word.equalsIgnoreCase("and")) {
													expectForce(":").execute(p5->{
														expectOneOfForce(app.getAndMethodsNames(), "and methods").execute(p6->{
															String methodName = p6.word;
															if (isKeyword(methodName)) {
																logFatal("and method name", "keyword " + methodName);
															}
															expectForce(";").execute(p7->{
																try {
																	rb.setAndMethod (app.getAndMethod(methodName));
																} catch (Exception e) {
																	this.rollbackPointer();
																	logFatal (e.getMessage());
																	e.printStackTrace();;
																}
															});
														});
													});
												}
												if (p4.word.equalsIgnoreCase("rule")) {
													expectWordOrNumberForce("Rule name or number").execute(p5->{
														String name = p5.word;
														expectForce(":").execute(p6->{
															expectRegForce("(?s).*?;", "rule definition").execute(p7-> {
																try {
																rb.addRule(rb.ruleFactory.fromString(name,p7.word));
															} catch (Exception e) {
																this.rollbackPointer();
																logFatal (e.getMessage());
																e.printStackTrace();;
															}
													});
													});
													});
												}
										 });
										 }
									 });
								} while (expect("ruleblock").isFound() && !this.fatalState);
							});
		
					expectForce("end_function_block");
				});
		// app.saveJSON();
		}
		catch (Exception e) {
			this.rollbackPointer();
			this.logger.fatal.add(new LogEntry (e.getMessage(), this.countLines(getPointer()), getPointer(), this.countLinepos(getPointer())));
			e.printStackTrace();
		}
		this.app.logger=this.logger;
	}
}
