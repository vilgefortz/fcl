package research.fcl.library.accumulation;

import java.util.ArrayList;
import java.util.List;

public class DefaultAccumulationMethodsFactory extends AccumulationMethodsFactory{

	private static final long serialVersionUID = 1144167258494948535L;

	public DefaultAccumulationMethodsFactory () {
		this.add (new MaxMethod("max"));
		this.add (new ProdMethod("prod"));
	}	
}
