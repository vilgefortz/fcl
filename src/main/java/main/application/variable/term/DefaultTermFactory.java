package main.application.variable.term;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.application.Application;
import main.application.parser.utils.ParsingUtils;
import main.application.variable.term.types.Point;
import main.application.variable.term.types.PointsTerm;
import main.application.variable.term.types.SingletonTerm;

public class DefaultTermFactory extends TermFactory {
	public static final TermGenerator POINTS = new TermGenerator() {

		@Override
		protected Term generate(String termName, String definition)
				throws TermDeclarationErrorException {
			List<Point> p = this.generatePoints(definition);
			return new PointsTerm(termName, p);
		}
		private double errorPoint=Double.NaN;
		private List<Point> generatePoints(String definition)
				throws TermDeclarationErrorException {
			List<Point> points = new ArrayList<Point>();
			while (!definition.trim().equals("")) {
				Logger.getGlobal()
						.info("PARSING Points : '" + definition + "'");
				definition = definition.trim();
				if (!definition.startsWith("("))
					throw new TermDeclarationErrorException(
							"Point definition should start with '('");
				definition = definition.substring(1);
				int closingPos = definition.indexOf(")");
				if (closingPos < 0)
					throw new TermDeclarationErrorException(
							"Point definition is missing closing bracket");
				String pointDef = definition.substring(0, closingPos);
				Point p = generatePoint(pointDef);
				points.add(p);
				definition = definition.substring(pointDef.length() + 1);
			}
			points.sort((p1, p2)->{
				int result = Double.compare(p1.x, p2.x);
				if (result==0) errorPoint=p1.x;
				return result;
			});
			if (!Double.isNaN(errorPoint)) {
				double ep = errorPoint;
				errorPoint = Double.NaN;
				throw new TermDeclarationErrorException("Cannot assign two values to same point '"+ ep + "'");
			}
			return points;
		}

		private Point generatePoint(String pointDef)
				throws TermDeclarationErrorException {
			double x, y;
			Point p = new Point();
			String val = ParsingUtils.getFirstWord(pointDef = pointDef.trim(),
					"[^,]*");
			try {
				x = Double.parseDouble(val);
			} catch (Exception e) {
				throw new TermDeclarationErrorException("Point value '" + val
						+ "' is not valid");
			}
			String comma = ParsingUtils.getFirstWord(pointDef = pointDef
					.substring(val.length()).trim(), ".");
			if (!comma.equals(","))
				throw new TermDeclarationErrorException("Points should be seperated by comma, '" + comma + "' found");
			val = ParsingUtils.getFirstWord(
					pointDef = pointDef.substring(comma.length()).trim(),
					".*");
			try {
				y = Double.parseDouble(val);
			} catch (Exception e) {
				throw new TermDeclarationErrorException("Point value '" + val
						+ "' is not valid");
			}
			if (y<0 || y>1) throw new TermDeclarationErrorException("Point value part must be in range <0,1>");
			if (!pointDef.substring(val.length()).equals(""))
				throw new TermDeclarationErrorException("Unexpected character sequence '" + pointDef + "'");
			p.x = x;
			p.y = y;
			return p;
		}

		@Override
		protected boolean isResponsible(String definition) {
			if (definition.trim().startsWith("("))
				return true;
			return false;
		}
	};
	public static final TermGenerator SINGLETON = new TermGenerator() {

		@Override
		protected Term generate(String termName, String definition)
				throws TermDeclarationErrorException {
			return new SingletonTerm(termName, definition);
		}

		@Override
		protected boolean isResponsible(String definition)
				throws TermDeclarationErrorException {
			char first = definition.trim().toCharArray()[0];
			return Character.isDigit(first) || first == '-' || first == '.';
		}
	};

	public DefaultTermFactory(Application application) {
		this.app = application;
		this.addTermGenerator(SINGLETON);
		this.addTermGenerator(POINTS);
	}
}
