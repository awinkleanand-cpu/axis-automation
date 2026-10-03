package com.axismaxlife.automation.utils;

import com.axismaxlife.automation.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class WebDriverFactory {

    private WebDriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        return switch (browser) {
            case "chrome" -> createChromeDriver();
            case "edge", "msedge", "microsoftedge" -> createEdgeDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        applyChromiumArguments(options);
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("prefs", chromiumPrefs());

        String userDataDir = ConfigReader.getOptional("chrome.user.data.dir", "");
        if (!userDataDir.isBlank()) {
            options.addArguments("--user-data-dir=" + userDataDir);
        }
        applyHeadlessOrMaximized(options);
        return new ChromeDriver(options);
    }

    private static WebDriver createEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
        applyChromiumArguments(options);
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("prefs", chromiumPrefs());

        String userDataDir = ConfigReader.getOptional("edge.user.data.dir", "");
        if (!userDataDir.isBlank()) {
            options.addArguments("--user-data-dir=" + userDataDir);
        }
        applyHeadlessOrMaximized(options);

        Path localDriver = Path.of("drivers", "msedgedriver.exe");
        if (Files.isRegularFile(localDriver)) {
            System.setProperty("webdriver.edge.driver", localDriver.toAbsolutePath().toString());
        } else {
            try {
                WebDriverManager.edgedriver().setup();
            } catch (RuntimeException ignored) {
                // Selenium Manager resolves msedgedriver when WebDriverManager cannot reach Azure CDN.
            }
        }
        return new EdgeDriver(options);
    }

    private static void applyChromiumArguments(org.openqa.selenium.chromium.ChromiumOptions<?> options) {
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
    }

    private static Map<String, Object> chromiumPrefs() {
        return Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false
        );
    }

    private static void applyHeadlessOrMaximized(org.openqa.selenium.chromium.ChromiumOptions<?> options) {
        if (shouldRunHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
        } else {
            options.addArguments("--start-maximized");
        }
    }

    private static boolean shouldRunHeadless() {
        if (ConfigReader.getBoolean("headless", false)) {
            return true;
        }
        String ci = System.getenv("CI");
        return ci != null && Boolean.parseBoolean(ci);
    }
}
