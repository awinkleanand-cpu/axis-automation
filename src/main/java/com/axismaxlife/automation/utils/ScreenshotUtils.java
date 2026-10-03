package com.axismaxlife.automation.utils;

import com.axismaxlife.automation.config.FrameworkConstants;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private static final Logger LOGGER = LogManager.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    private ScreenshotUtils() {
    }

    public static String capture(String label) {
        if (!DriverManager.isDriverInitialized()) {
            return null;
        }

        WebDriver driver = DriverManager.getDriver();
        if (!(driver instanceof TakesScreenshot takesScreenshot)) {
            LOGGER.warn("Current driver does not support screenshots");
            return null;
        }

        try {
            Files.createDirectories(FrameworkConstants.SCREENSHOT_DIR);
            String fileName = sanitize(label) + "_" + LocalDateTime.now().format(FORMATTER) + ".png";
            Path destination = FrameworkConstants.SCREENSHOT_DIR.resolve(fileName);
            FileUtils.copyFile(takesScreenshot.getScreenshotAs(OutputType.FILE), destination.toFile());
            LOGGER.info("Screenshot saved: {}", destination.toAbsolutePath());
            return destination.toAbsolutePath().toString();
        } catch (IOException exception) {
            LOGGER.error("Failed to capture screenshot for {}", label, exception);
            return null;
        }
    }

    private static String sanitize(String label) {
        return label.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}
