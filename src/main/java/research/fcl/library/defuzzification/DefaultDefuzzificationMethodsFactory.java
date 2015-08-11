package research.fcl.library.defuzzification;

import java.util.ArrayList;
import java.util.List;

public class DefaultDefuzzificationMethodsFactory extends DefuzzificationMethodsFactory {
	public DefaultDefuzzificationMethodsFactory() {
		this.add (new CogMethod("cog"));
		this.add (new CosMethod("cos"));
	}
}
