package com.axismaxlife.tests.base;

import com.axismaxlife.automation.config.FrameworkConstants;
import com.axismaxlife.automation.utils.DriverManager;
import com.axismaxlife.automation.utils.WebDriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.nio.file.Files;

public abstract class BaseTest {

    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void createOutputDirectories() throws IOException {
        Files.createDirectories(FrameworkConstants.REPORT_DIR);
        Files.createDirectories(FrameworkConstants.SCREENSHOT_DIR);
        Files.createDirectories(FrameworkConstants.LOG_DIR);
        Files.createDirectories(FrameworkConstants.RESULTS_DIR);
        LOGGER.info("Framework output directories initialized");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriver driver = WebDriverFactory.createDriver();
        DriverManager.setDriver(driver);
        LOGGER.info("WebDriver session started");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
        LOGGER.info("WebDriver session closed");
    }
}
