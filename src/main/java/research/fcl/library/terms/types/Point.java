package research.fcl.library.terms.types;

import com.google.gson.annotations.Expose;

public class Point {
	public Point () {
		
	}
	public Point(double x, double y) {
		this.x=x;
		this.y=y;
	}

	@Expose
	public double x,y;
}
