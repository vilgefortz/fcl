package research.fcl.library.variable.term.types;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import research.fcl.library.variable.term.Term;

public class TraingleSingleton extends Term {
	protected Term term;
	protected double rangeMin,rangeMax;
	protected double size;
	private static Logger log = Logger.getGlobal();
	public TraingleSingleton(Term term, double min, double max, double size) {
		super ("triangle_singleton" + term.getName());
		this.term = term;
		this.rangeMax=max;
		this.rangeMin=min;
		this.size=size;
		SingletonTerm t = (SingletonTerm) term.getBaseTerm();
		double range = rangeMax-rangeMin;
		double delta = range/size;
		List<Point> points = new ArrayList<Point> ();
		points.add(new Point (t.point-delta,0));
		points.add(new Point (t.point,term.fun(t.point)));
		points.add(new Point (t.point+delta,0));
		log.info("CREATED POINTS TERM");
		pt = new PointsTerm("triangluar_singleton" + t.getName(), points);
		this.min = pt.getMin();
		this.max = pt.getMax();
	}

	protected PointsTerm pt;
	
	@Override
	public double getMax() {
		return pt.getMax();
	}

	@Override
	public double getMin() {
		return pt.getMin();
	}

	@Override
	public double fun(double val) {
		return pt.fun(val);
	}

}
