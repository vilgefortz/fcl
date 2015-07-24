
package research.fcl.library.andmethods;

public class ProdMethod extends AndMethod  {
	{
		this.name="prod";
	}
	@Override
	public double and(double a, double b) {
		return a*b;
	}
	@Override
	public double or(double a, double b) {
		return a+b-a*b;
	}
	
};
