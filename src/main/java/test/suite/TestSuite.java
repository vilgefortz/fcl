package test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.application.Application;
import test.application.enviroment.Enviroment;

@RunWith(Suite.class)
@SuiteClasses({
	Application.class,
	Enviroment.class
})
public class TestSuite {

}
