package main.application.accumulation;

import java.util.ArrayList;
import java.util.List;

public class DefaultAccumulationMethods extends ArrayList<AccumulationMethod>{

	public DefaultAccumulationMethods () {
		this.add (new MaxMethod("max"));
	}
	public String[] getNames() {
		List<String> l = new ArrayList<String> ();
		for (AccumulationMethod m : this) {
			l.add(m.getName());
		}
		return l.toArray(new String [l.size()]);
	}

	public AccumulationMethod get(String method) throws AccumulationMethodNotRecognisedException {
		try {
			return this.get(this.indexOf(AccumulationMethod.getDummy(method)));
		} catch (Exception e) {
			throw new AccumulationMethodNotRecognisedException (method);
		}
	}
	
}
