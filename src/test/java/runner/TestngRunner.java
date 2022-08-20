package runner;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

public class TestngRunner {

	public static void main(String[] args) {
		 
		TestNG objtestng = new TestNG();
		
		List<String> suites = new ArrayList<String>();
		
		suites.add("C:\\Users\\VinodPatial\\eclipse-workspace\\RestAssuredProject\\testng01.xml");
		
		objtestng.setTestSuites(suites);
		
		objtestng.run();

	}

}
