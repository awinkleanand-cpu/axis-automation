package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    @BeforeSuite(alwaysRun = true)
    public void initReports() throws Exception {
        Files.createDirectories(Path.of(System.getProperty("user.dir"), "reports"));
        ReportManager.getInstance();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriver driver = createDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        DRIVER.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void flushReports() {
        ReportManager.flush();
        System.out.println("Report saved at: /reports/ExtentReport.html");
    }

    protected WebDriver getDriver() {
        return DRIVER.get();
    }

    private WebDriver createDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if ("edge".equals(browser) || "msedge".equals(browser)) {
            return createEdgeDriver(headless);
        }
        return createChromeDriver(headless);
    }

    private WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        applyChromiumOptions(options, headless);
        return new ChromeDriver(options);
    }

    private WebDriver createEdgeDriver(boolean headless) {
        Path localDriver = Path.of("drivers", "msedgedriver.exe");
        if (Files.isRegularFile(localDriver)) {
            System.setProperty("webdriver.edge.driver", localDriver.toAbsolutePath().toString());
        } else {
            WebDriverManager.edgedriver().setup();
        }
        EdgeOptions options = new EdgeOptions();
        applyChromiumOptions(options, headless);
        return new EdgeDriver(options);
    }

    private void applyChromiumOptions(org.openqa.selenium.chromium.ChromiumOptions<?> options, boolean headless) {
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        if (headless || Boolean.parseBoolean(System.getenv("CI"))) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        } else {
            options.addArguments("--start-maximized");
        }
    }
}
