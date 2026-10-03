package com.axismaxlife.automation.config;

import java.nio.file.Path;

public final class FrameworkConstants {

    public static final Path REPORT_DIR = Path.of("test-output", "reports");
    public static final Path SCREENSHOT_DIR = Path.of("test-output", "screenshots");
    public static final Path LOG_DIR = Path.of("test-output", "logs");
    public static final Path RESULTS_DIR = Path.of("test-output", "results");

    public static final String EXTENT_REPORT_PATH = REPORT_DIR.resolve("ExtentReport.html").toString();
    public static final String JSON_RESULTS_PATH = RESULTS_DIR.resolve("test-results.json").toString();

    private FrameworkConstants() {
    }
}
