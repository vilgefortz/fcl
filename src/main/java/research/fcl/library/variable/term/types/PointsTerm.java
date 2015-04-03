package research.fcl.library.variable.term.types;

import java.util.List;

import research.fcl.library.variable.term.Term;

import com.google.gson.annotations.Expose;

public class PointsTerm extends Term {

	@Expose
	private List<Point> points;
	
	public PointsTerm(String termName, List<Point> p) {
		super(termName);
		this.max = p.get(p.size()-1).x;
		this.min = p.get(0).x;
		this.type = "points";
		this.points=p;
	}

	@Override
	public double fun(double val) {
		if (val <= points.get(0).x)
			return points.get(0).y;
		if (val >= points.get(points.size()-1).x)
			return points.get(points.size()-1).y;
		for (int i = 0; i < points.size() - 1; i++) {
			if (points.get(i).x <= val && points.get(i + 1).x >= val) {
				double x1 = points.get(i).x;
				double x2 = points.get(i + 1).x;
				double y1 = points.get(i).y;
				double y2 = points.get(i + 1).y;
				return (y2 - y1) * (val - x1) / (x2 - x1) + y1;
			}
		}
		return Double.NaN;
	}

	@Override
	public double getMax() {
		return this.max;
	}

	@Override
	public double getMin() {
		return this.min;
	}

	@Override
	public double[] getImportantPoints() {
		double [] list = new double [this.points.size()];
		for (int i=0; i<list.length; i++)
			list[i]=points.get(i).x;
		return list;
	}

}
