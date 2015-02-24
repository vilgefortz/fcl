package main.application.functionblock;

import java.util.ArrayList;

public class Ruleblocks extends ArrayList<Ruleblock>{

	private static final long serialVersionUID = 8416380899866757285L;

	public void execute() {
		for (Ruleblock r: this) { 
			r.execute ();
		}
	}

}
