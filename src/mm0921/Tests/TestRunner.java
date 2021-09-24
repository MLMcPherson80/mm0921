package mm0921.Tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(CheckoutTests.class);
		
		for (Failure failure: result.getFailures()) {
			System.out.print(failure.toString());
			failure.getException().printStackTrace();
		}
		
		System.out.print("Test successful: " + result.wasSuccessful());
	}
}
