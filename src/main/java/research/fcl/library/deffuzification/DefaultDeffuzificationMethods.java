package research.fcl.library.deffuzification;

import java.util.ArrayList;
import java.util.List;

public class DefaultDeffuzificationMethods extends ArrayList<DefuzzificationMethod>{
	public DefaultDeffuzificationMethods() {
		this.add (new CogMethod("cog"));
		this.add (new CosMethod("cos"));
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
