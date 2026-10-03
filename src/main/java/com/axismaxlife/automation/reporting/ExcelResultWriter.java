package com.axismaxlife.automation.reporting;

import com.axismaxlife.automation.config.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public final class ExcelResultWriter {

    private static final Logger LOGGER = LogManager.getLogger(ExcelResultWriter.class);

    private ExcelResultWriter() {
    }

    public static void writeSummary(int passed, int failed, int skipped, List<Map<String, Object>> results) {
        try {
            Files.createDirectories(FrameworkConstants.RESULTS_DIR);
            try (Workbook workbook = new XSSFWorkbook()) {
                writeSummarySheet(workbook, passed, failed, skipped);
                writeDetailsSheet(workbook, results);
                try (OutputStream outputStream = Files.newOutputStream(
                        FrameworkConstants.RESULTS_DIR.resolve("test-results.xlsx"))) {
                    workbook.write(outputStream);
                }
            }
            LOGGER.info("Excel test results written to {}", FrameworkConstants.EXCEL_RESULTS_PATH);
        } catch (IOException exception) {
            LOGGER.error("Unable to write Excel test results", exception);
        }
    }

    private static void writeSummarySheet(Workbook workbook, int passed, int failed, int skipped) {
        Sheet sheet = workbook.createSheet("Summary");
        CellStyle headerStyle = createHeaderStyle(workbook);

        createRow(sheet, 0, headerStyle, "Metric", "Value");
        createRow(sheet, 1, null, "Suite", "Axis Max Life Automation Suite");
        createRow(sheet, 2, null, "Generated At", LocalDateTime.now().toString());
        createRow(sheet, 3, null, "Passed", String.valueOf(passed));
        createRow(sheet, 4, null, "Failed", String.valueOf(failed));
        createRow(sheet, 5, null, "Skipped", String.valueOf(skipped));
        createRow(sheet, 6, null, "Total", String.valueOf(passed + failed + skipped));

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private static void writeDetailsSheet(Workbook workbook, List<Map<String, Object>> results) {
        Sheet sheet = workbook.createSheet("Test Details");
        CellStyle headerStyle = createHeaderStyle(workbook);

        createRow(sheet, 0, headerStyle, "Test Name", "Status", "Duration (ms)", "Timestamp", "Error");

        int rowIndex = 1;
        for (Map<String, Object> result : results) {
            createRow(
                    sheet,
                    rowIndex++,
                    null,
                    String.valueOf(result.get("testName")),
                    String.valueOf(result.get("status")),
                    String.valueOf(result.get("durationMs")),
                    String.valueOf(result.get("timestamp")),
                    result.get("error") != null ? String.valueOf(result.get("error")) : ""
            );
        }

        for (int column = 0; column < 5; column++) {
            sheet.autoSizeColumn(column);
        }
    }

    private static void createRow(Sheet sheet, int rowIndex, CellStyle style, String... values) {
        Row row = sheet.createRow(rowIndex);
        for (int column = 0; column < values.length; column++) {
            Cell cell = row.createCell(column);
            cell.setCellValue(values[column]);
            if (style != null) {
                cell.setCellStyle(style);
            }
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
