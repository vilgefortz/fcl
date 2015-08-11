package research.fcl.library.andmethods;

import java.util.ArrayList;
import java.util.List;

public class DefaultAndMethods extends AndMethodsFactory {
	
	public DefaultAndMethods () {
		this.add(new MinMethod());
		this.add(new ProdMethod());
	}
}
