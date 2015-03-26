package research.fcl.library.parser;

public class Keywords {
	public Keyword function_block = new Keyword ("function_block","end_function_block");
	public Keyword var_input = new Keyword ("var_input", "end_var");
	public Keyword var_output = new Keyword ("var_output", "end_var");
	public Keyword fuzzify = new Keyword ("fuzzify", "end_fuzzify");
	public Keyword defuzzify = new Keyword ("defuzzify", "end_defuzzify");
	public Keyword ruleblock = new Keyword ("ruleblock", "end_ruleblock");
}
