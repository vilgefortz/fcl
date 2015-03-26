package research.fcl.library.andmethods;

public abstract class AndMethod {
	public static AndMethod createDummy (final String varName) {
		return new AndMethod () {
			{
				this.name=varName;
			}
			@Override
			public double and(double a, double b) {
				return 0;
			}

			@Override
			public double or(double a, double b) {
				return 0;
			}
			
		};
	}
	protected String name;

	public String getName() {
		return this.name;
	}
	
	public abstract double and (double a, double b);
	public abstract double or (double a, double b);
    @Override
    public boolean equals(Object obj) {
    	return this.name.equalsIgnoreCase(((AndMethod)obj).name);
    }
    
}
