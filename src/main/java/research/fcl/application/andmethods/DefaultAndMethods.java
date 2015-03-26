package research.fcl.application.andmethods;

import java.util.ArrayList;
import java.util.List;

public class DefaultAndMethods extends ArrayList<AndMethod> {

	public String[] getNames() {
		List<String> names = new ArrayList<String>();
		this.forEach(a -> {
			names.add(a.getName());
		});
		String [] l = new String [names.size()];
		return names.toArray(l);
	}

	public AndMethod getMethod(String name) throws AndMethodNotFoundException {
		try {
			AndMethod m = this.get(this.indexOf((AndMethod.createDummy(name))));
			return m;
		} catch (Exception e) {
			throw new AndMethodNotFoundException(name);
		}

	}
	public static final AndMethod MIN = new AndMethod () {
		{
			this.name="min";
		}
		@Override
		public double and(double a, double b) {
			return Math.min(a, b);
		}
		@Override
		public double or(double a, double b) {
			return Math.max(a, b);
		}
		
	};
	public DefaultAndMethods () {
		this.add(MIN);
	}
}
