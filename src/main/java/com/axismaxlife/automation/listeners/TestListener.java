package com.axismaxlife.automation.listeners;

import com.axismaxlife.automation.reporting.ExtentManager;
import com.axismaxlife.automation.reporting.ReportManager;
import com.axismaxlife.automation.reporting.TestResultWriter;
import com.axismaxlife.automation.utils.ScreenshotUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private int passed;
    private int failed;
    private int skipped;

    @Override
    public void onStart(ITestContext context) {
        passed = 0;
        failed = 0;
        skipped = 0;
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isBlank()) {
            description = testName;
        }
        ReportManager.startTest(testName, description);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passed++;
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();
        ReportManager.logPass("Test passed in " + duration + " ms");
        ReportManager.attachScreenshot(testName + "_PASS");
        TestResultWriter.addResult(testName, "PASSED", duration, null);
        ReportManager.endTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failed++;
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();
        Throwable throwable = result.getThrowable();
        String errorMessage = throwable != null ? throwable.getMessage() : "Unknown failure";

        ReportManager.logFail("Test failed: " + errorMessage);
        if (throwable != null) {
            ReportManager.getExtentTest().fail(throwable);
        }
        ScreenshotUtils.capture(testName + "_FAIL");
        ReportManager.attachScreenshot(testName + "_FAIL");
        TestResultWriter.addResult(testName, "FAILED", duration, errorMessage);
        ReportManager.endTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skipped++;
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();
        Throwable throwable = result.getThrowable();
        String errorMessage = throwable != null ? throwable.getMessage() : "Test skipped";

        ReportManager.logInfo("Test skipped: " + errorMessage);
        TestResultWriter.addResult(testName, "SKIPPED", duration, errorMessage);
        ReportManager.endTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        TestResultWriter.writeSummary(passed, failed, skipped);
        ExtentManager.flush();
    }
}
