package com.axismaxlife.automation.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.axismaxlife.automation.config.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;

public final class ExtentManager {

    private static final Logger LOGGER = LogManager.getLogger(ExtentManager.class);
    private static ExtentReports extentReports;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            createInstance();
        }
        return extentReports;
    }

    private static void createInstance() {
        try {
            Files.createDirectories(FrameworkConstants.REPORT_DIR);
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(FrameworkConstants.EXTENT_REPORT_PATH);
            sparkReporter.config().setDocumentTitle("Axis Max Life Automation Report");
            sparkReporter.config().setReportName("Insurance Application Test Results");
            sparkReporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.DARK);

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Application", "Axis Max Life UAT");
            extentReports.setSystemInfo("Framework", "Selenium + TestNG + POM");
            extentReports.setSystemInfo("Browser", "Chrome");
            LOGGER.info("Extent report initialized at {}", FrameworkConstants.EXTENT_REPORT_PATH);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to initialize Extent report", exception);
        }
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
            LOGGER.info("Extent report flushed to {}", FrameworkConstants.EXTENT_REPORT_PATH);
        }
    }
}
