
package research.fcl.library.andmethods;

public class MinMethod extends AndMethod  {
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
