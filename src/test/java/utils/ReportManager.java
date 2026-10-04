package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;

public class ReportManager implements ITestListener {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            createInstance();
        }
        return extentReports;
    }

    public static Path getReportPath() {
        return Path.of(System.getProperty("user.dir"), "reports", "ExtentReport.html");
    }

    private static void createInstance() {
        try {
            Path reportPath = getReportPath();
            Files.createDirectories(reportPath.getParent());

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath.toString());
            sparkReporter.config().setDocumentTitle("Extent Report - HugeDomains Automation");
            sparkReporter.config().setReportName("HugeDomains Automation");
            sparkReporter.config().setTheme(Theme.STANDARD);

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Application", "HugeDomains");
            extentReports.setSystemInfo("URL", "https://www.hugedomains.com/index.cfm");
            extentReports.setSystemInfo("Framework", "Selenium + TestNG + POM");
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to initialize ExtentReports", exception);
        }
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    public static ExtentTest getTest() {
        return TEST.get();
    }

    @Override
    public void onStart(ITestContext context) {
        getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = getInstance().createTest(result.getMethod().getDescription(),
                result.getMethod().getMethodName());
        TEST.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (TEST.get() != null) {
            TEST.get().log(Status.PASS, "Test Case PASS");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (TEST.get() != null) {
            TEST.get().log(Status.FAIL, "Test Case FAIL");
            if (result.getThrowable() != null) {
                TEST.get().fail(result.getThrowable());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (TEST.get() != null) {
            TEST.get().log(Status.SKIP, "Test Case SKIPPED");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        flush();
    }
}
