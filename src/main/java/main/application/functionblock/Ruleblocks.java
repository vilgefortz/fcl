package main.application.functionblock;

import java.util.ArrayList;

public class Ruleblocks extends ArrayList<Ruleblock>{

	public void execute() {
		for (Ruleblock r: this) { 
			r.execute ();
		}
	}

}
