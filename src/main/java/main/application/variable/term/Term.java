package main.application.variable.term;

import com.google.gson.annotations.Expose;

public abstract class Term {
	@Expose
	protected String name="unknown";
	@Expose
	protected String type="unknown";
	@Expose
	protected double min;
	@Expose
	protected double max;
	public abstract double getMax ();
	public abstract double getMin();
	public Term (String name) {
		this.name=name;
	}
	
	public Term getBaseTerm () {
		return this;
	}
	
	@Override
	public boolean equals(Object arg0) {
		Term t=(Term)arg0;
		return t.name.equalsIgnoreCase(this.name);
	}
	public abstract double fun (double val);
	@Override
	public String toString() {
		return "TERM : "+name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public static Term getDummy(String word) {
		return new Term(word) {
			@Override
			public double fun(double val) {
				return 0;
			}
			@Override
			public double getMax() {
				return 0;
			}
			@Override
			public double getMin() {
				return 0;
			}
		};
	}
}