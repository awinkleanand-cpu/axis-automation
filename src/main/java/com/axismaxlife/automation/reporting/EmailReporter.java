package com.axismaxlife.automation.reporting;

import com.axismaxlife.automation.config.ConfigReader;
import com.axismaxlife.automation.config.FrameworkConstants;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public final class EmailReporter {

    private static final Logger LOGGER = LogManager.getLogger(EmailReporter.class);

    private EmailReporter() {
    }

    public static void sendReportIfRequired(int passed, int failed, int skipped, List<Map<String, Object>> results) {
        if (!ConfigReader.getBoolean("email.enabled", false)) {
            LOGGER.info("Email reporting is disabled");
            return;
        }

        boolean onFailureOnly = ConfigReader.getBoolean("email.on.failure.only", true);
        if (onFailureOnly && failed == 0) {
            LOGGER.info("Skipping email because all tests passed and email.on.failure.only=true");
            return;
        }

        try {
            sendEmail(passed, failed, skipped, results);
            LOGGER.info("Failure report email sent successfully");
        } catch (MessagingException | IOException exception) {
            LOGGER.error("Unable to send failure report email", exception);
        }
    }

    private static void sendEmail(int passed, int failed, int skipped, List<Map<String, Object>> results)
            throws MessagingException, IOException {
        String host = ConfigReader.get("email.smtp.host");
        int port = ConfigReader.getOptionalInt("email.smtp.port", 587);
        boolean tls = ConfigReader.getBoolean("email.smtp.tls", true);
        String username = ConfigReader.get("email.smtp.username");
        String password = resolvePassword();
        String from = ConfigReader.get("email.from");
        List<String> recipients = Arrays.stream(ConfigReader.get("email.to").split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .collect(Collectors.toList());

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));
        properties.put("mail.smtp.starttls.enable", String.valueOf(tls));

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        for (String recipient : recipients) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }

        String subjectPrefix = failed > 0 ? "[FAILED]" : "[PASSED]";
        message.setSubject(subjectPrefix + " Axis Max Life Automation Report - " + LocalDateTime.now());

        String body = buildEmailBody(passed, failed, skipped, results);
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        attachIfExists(multipart, FrameworkConstants.REPORT_DIR.resolve("ExtentReport.html"));
        attachIfExists(multipart, FrameworkConstants.RESULTS_DIR.resolve("test-results.xlsx"));
        attachLatestScreenshot(multipart);

        message.setContent(multipart);
        Transport.send(message);
    }

    private static String resolvePassword() {
        String envPassword = System.getenv("EMAIL_PASSWORD");
        if (envPassword != null && !envPassword.isBlank()) {
            return envPassword;
        }
        return ConfigReader.getOptional("email.smtp.password", "");
    }

    private static String buildEmailBody(int passed, int failed, int skipped, List<Map<String, Object>> results) {
        StringBuilder builder = new StringBuilder();
        builder.append("Axis Max Life Automation Execution Summary").append(System.lineSeparator());
        builder.append("Generated At: ").append(LocalDateTime.now()).append(System.lineSeparator());
        builder.append(System.lineSeparator());
        builder.append("Passed: ").append(passed).append(System.lineSeparator());
        builder.append("Failed: ").append(failed).append(System.lineSeparator());
        builder.append("Skipped: ").append(skipped).append(System.lineSeparator());
        builder.append("Total: ").append(passed + failed + skipped).append(System.lineSeparator());
        builder.append(System.lineSeparator());
        builder.append("Test Details:").append(System.lineSeparator());

        for (Map<String, Object> result : results) {
            builder.append("- ")
                    .append(result.get("testName"))
                    .append(" | ")
                    .append(result.get("status"))
                    .append(" | ")
                    .append(result.get("durationMs"))
                    .append(" ms");
            if (result.get("error") != null) {
                builder.append(" | Error: ").append(result.get("error"));
            }
            builder.append(System.lineSeparator());
        }

        builder.append(System.lineSeparator());
        builder.append("Attached: Extent HTML report, Excel results, and latest failure screenshot (if available).");
        return builder.toString();
    }

    private static void attachIfExists(Multipart multipart, Path filePath) throws IOException, MessagingException {
        if (!Files.exists(filePath)) {
            return;
        }
        MimeBodyPart attachment = new MimeBodyPart();
        attachment.attachFile(filePath.toFile());
        multipart.addBodyPart(attachment);
    }

    private static void attachLatestScreenshot(Multipart multipart) throws IOException, MessagingException {
        Path screenshotDir = FrameworkConstants.SCREENSHOT_DIR;
        if (!Files.exists(screenshotDir)) {
            return;
        }

        try (var paths = Files.list(screenshotDir)) {
            Path latestScreenshot = paths
                    .filter(path -> path.toString().toLowerCase().endsWith(".png"))
                    .max(Comparator.comparing(path -> path.toFile().lastModified()))
                    .orElse(null);

            if (latestScreenshot != null) {
                attachIfExists(multipart, latestScreenshot);
            }
        }
    }
}
