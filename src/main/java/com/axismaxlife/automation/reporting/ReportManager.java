package com.axismaxlife.automation.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.axismaxlife.automation.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ReportManager {

    private static final Logger LOGGER = LogManager.getLogger(ReportManager.class);
    private static final ThreadLocal<ExtentTest> EXTENT_TEST = new ThreadLocal<>();

    private ReportManager() {
    }

    public static void startTest(String testName, String description) {
        ExtentTest extentTest = ExtentManager.getInstance().createTest(testName, description);
        EXTENT_TEST.set(extentTest);
        LOGGER.info("Started test: {} - {}", testName, description);
    }

    public static void endTest() {
        EXTENT_TEST.remove();
    }

    public static void logInfo(String message) {
        LOGGER.info(message);
        ExtentTest extentTest = EXTENT_TEST.get();
        if (extentTest != null) {
            extentTest.log(Status.INFO, message);
        }
    }

    public static void logPass(String message) {
        LOGGER.info("PASS: {}", message);
        ExtentTest extentTest = EXTENT_TEST.get();
        if (extentTest != null) {
            extentTest.log(Status.PASS, message);
        }
    }

    public static void logFail(String message) {
        LOGGER.error("FAIL: {}", message);
        ExtentTest extentTest = EXTENT_TEST.get();
        if (extentTest != null) {
            extentTest.log(Status.FAIL, message);
        }
    }

    public static void logStep(String step) {
        logInfo(step);
    }

    public static void logStepWithScreenshot(String step) {
        logInfo(step);
        attachScreenshot(step);
    }

    public static void attachScreenshot(String label) {
        String screenshotPath = ScreenshotUtils.capture(label);
        ExtentTest extentTest = EXTENT_TEST.get();
        if (screenshotPath != null && extentTest != null) {
            extentTest.addScreenCaptureFromPath(screenshotPath);
        }
    }

    public static ExtentTest getExtentTest() {
        return EXTENT_TEST.get();
    }
}
