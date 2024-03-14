package extentReportUtilities;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class SetUp implements BeforeEachCallback, BeforeAllCallback, AfterAllCallback, TestWatcher {

	private static ExtentReports extentReports;
	public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	public void beforeAll(ExtensionContext context) {
		String fileName = ExtentReportManager.getReportNameWithTimeStamp();
		String fullReportPath = System.getProperty("user.dir") + "\\reports\\" + fileName;
		extentReports = ExtentReportManager.createInstance(fullReportPath, "Integration Test Cases Report",
				"Test Execution Report");
	}
	
	public void beforeEach(ExtensionContext context) {
		ExtentTest test = extentReports.createTest("Test Name " + context.getRequiredTestClass().getName() + " - "
				+ context.getRequiredTestMethod().getName());
		extentTest.set(test);
	}

	public void afterAll(ExtensionContext context) {
		if (extentReports != null)
			extentReports.flush();
	}
	
	public void testFailed(ExtensionContext context, Throwable cause) {
        ExtentReportManager.logFailureDetails(context.getDisplayName());
    }
}
