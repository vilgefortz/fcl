package research.fcl.webapp.endpoints.mapper.tree;

import java.util.ArrayList;
import java.util.List;

import research.fcl.library.Application;
import research.fcl.library.functionblock.FunctionBlock;
import research.fcl.library.variables.BaseFunctionVariable;
import research.fcl.library.variables.InlineVariable;
import research.fcl.library.variables.InlineVariables;
import research.fcl.library.variables.InputVariable;
import research.fcl.library.variables.InputVariables;
import research.fcl.library.variables.OutputVariable;
import research.fcl.library.variables.OutputVariables;

public class JsTreeNodeMapper {
	public static final String INPUT_VARIABLES_NAME = "input variables";
	public static final String OUTPUT_VARIABLES_NAME = "output variables";
	public static final String INLINE_VARIABLES_NAME = "inline variables";
	public static final String NORMAL_TERMS_NAME = "terms";
	public Object map(Application app) {
		List<JsTreeNode> root = new ArrayList<JsTreeNode>();
		app.functionBlocks.forEach(fb -> {
			root.add(map(fb));
		});
		return root;
	}

	private JsTreeNode map(FunctionBlock fb) {
		JsTreeNode node = new JsTreeNode();
		node.setText(fb.name);
		if (fb.input.size()!=0) node.getChildren().add(map(fb.input));
		if (fb.inline.size()!=0) node.getChildren().add(map(fb.inline));
		if (fb.output.size()!=0) node.getChildren().add(map(fb.output));
		node.getLi_attr().classAttr="function-block";
		return node;
	}

	private JsTreeNode map(InlineVariables inline) {
		JsTreeNode node = new JsTreeNode ();
		node.setText(INLINE_VARIABLES_NAME);
		inline.forEach(var -> {node.getChildren().add(map (var));});	
		return node;
	}

	private JsTreeNode map(OutputVariables output) {
		JsTreeNode node = new JsTreeNode ();
		node.setText(OUTPUT_VARIABLES_NAME);
		output.forEach(var -> {node.getChildren().add(map (var));});
		return node;
	}

	private JsTreeNode map(InputVariables input) {
		JsTreeNode node = new JsTreeNode ();
		node.setText(INPUT_VARIABLES_NAME);
		input.forEach(var -> {node.getChildren().add(map (var));});
		return node;
	}

	private JsTreeNode map(InputVariable var) {
		JsTreeNode node = new JsTreeNode ();
		node.setText(var.toString());
		node.getChildren().add(mapNormalTerms (var,NORMAL_TERMS_NAME));
		node.getLi_attr().classAttr="input-var";
		node.getLi_attr().value=var.getName();
		return node;
	}

	private JsTreeNode mapNormalTerms(BaseFunctionVariable var, String name) {
		JsTreeNode node = new JsTreeNode ();
		node.setText(name);
		var.getTerms().forEach(term -> {
			JsTreeNode termNode = new JsTreeNode ();
			termNode.setText(term.toString());
			node.getChildren().add(termNode);
		});	
		return node;
	}

	private JsTreeNode map(OutputVariable var) {
		JsTreeNode node = new JsTreeNode ();
		node.setText(var.toString());
		node.getChildren().add(mapNormalTerms (var,NORMAL_TERMS_NAME));
		if (var instanceof InlineVariable) node.getLi_attr().classAttr="inline-var";
		else node.getLi_attr().classAttr="output-var";
		node.getLi_attr().value=var.getName();
		return node;
	}
}

