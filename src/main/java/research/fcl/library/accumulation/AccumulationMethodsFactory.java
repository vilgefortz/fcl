package research.fcl.library.accumulation;

import java.util.ArrayList;
import java.util.List;

public class AccumulationMethodsFactory extends ArrayList<AccumulationMethod> {
	public String[] getNames() {
		List<String> l = new ArrayList<String>();
		for (AccumulationMethod m : this) {
			l.add(m.getName());
		}
		return l.toArray(new String[l.size()]);
	}

	public AccumulationMethod get(String method)
			throws AccumulationMethodNotRecognisedException {
		try {
			return this.get(this.indexOf(AccumulationMethod.getDummy(method)));
		} catch (Exception e) {
			throw new AccumulationMethodNotRecognisedException(method);
		}
	}

}
