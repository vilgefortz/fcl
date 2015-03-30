package test.application.enviroment;

import static org.junit.Assert.fail;
import main.application.enviroment.Variable;

import org.junit.Test;

public class Enviroment {

	@Test
	public void testAddGetVariable() {
		main.application.enviroment.Enviroment env = new main.application.enviroment.Enviroment();
		Variable var = new Variable("var", 4);
		Variable var2 = new Variable("var2", 3);
		env.add(var);
		env.add(var2);
		if (env.getVariable("var").getValue() != 4) {
			fail ("Retrieving variable from enviroment failed");
		}
		if (env.getValueOf("var2") != 3) {
			fail ("Retrieving variable value from enviroment failed");
		}
		if (0!=env.getValueOf("var4") )
		fail ("Nan variable retrieveal failure");
	}
}
