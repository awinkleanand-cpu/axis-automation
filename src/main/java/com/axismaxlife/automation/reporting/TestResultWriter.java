package com.axismaxlife.automation.reporting;

import com.axismaxlife.automation.config.FrameworkConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TestResultWriter {

    private static final Logger LOGGER = LogManager.getLogger(TestResultWriter.class);
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    private static final List<Map<String, Object>> RESULTS = new ArrayList<>();

    private TestResultWriter() {
    }

    public static synchronized void addResult(String testName, String status, long durationMs, String errorMessage) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("testName", testName);
        result.put("status", status);
        result.put("durationMs", durationMs);
        result.put("timestamp", LocalDateTime.now().toString());
        if (errorMessage != null && !errorMessage.isBlank()) {
            result.put("error", errorMessage);
        }
        RESULTS.add(result);
    }

    public static synchronized List<Map<String, Object>> getResults() {
        return List.copyOf(RESULTS);
    }

    public static synchronized void writeSummary(int passed, int failed, int skipped) {
        try {
            Files.createDirectories(FrameworkConstants.RESULTS_DIR);
            Map<String, Object> summary = new LinkedHashMap<>();
            summary.put("suite", "Axis Max Life Automation Suite");
            summary.put("generatedAt", LocalDateTime.now().toString());
            summary.put("passed", passed);
            summary.put("failed", failed);
            summary.put("skipped", skipped);
            summary.put("total", passed + failed + skipped);
            summary.put("tests", RESULTS);

            MAPPER.writeValue(FrameworkConstants.RESULTS_DIR.resolve("test-results.json").toFile(), summary);
            LOGGER.info("JSON test results written to {}", FrameworkConstants.JSON_RESULTS_PATH);

            ExcelResultWriter.writeSummary(passed, failed, skipped, getResults());
            EmailReporter.sendReportIfRequired(passed, failed, skipped, getResults());
        } catch (IOException exception) {
            LOGGER.error("Unable to write JSON test results", exception);
        }
    }
}
