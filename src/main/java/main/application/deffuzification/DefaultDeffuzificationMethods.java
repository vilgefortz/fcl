package main.application.deffuzification;

import java.util.ArrayList;
import java.util.List;

import main.application.accumulation.AccumulationMethod;
import main.application.accumulation.AccumulationMethodNotRecognisedException;
import main.application.accumulation.MaxMethod;

public class DefaultDeffuzificationMethods extends ArrayList<DefuzzificationMethod>{
	public DefaultDeffuzificationMethods() {
		this.add (new CogMethod("cog"));
	}
	public String[] getNames() {
		List<String> l = new ArrayList<String> ();
		for (DefuzzificationMethod m : this) {
			l.add(m.getName());
		}
		return l.toArray(new String [l.size()]);
	}

	public DefuzzificationMethod get(String method) throws DefuzzificationMethodNotRecognisedException  {
		try {
			return this.get(this.indexOf(DefuzzificationMethod.getDummy(method)));
		} catch (Exception e) {
			throw new DefuzzificationMethodNotRecognisedException (method);
		}
	}
}
