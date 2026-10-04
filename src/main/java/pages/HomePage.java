package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {

    public static final String HOME_URL = "https://www.hugedomains.com/index.cfm";

    private static final List<By> SEARCH_BOX_LOCATORS = List.of(
            By.cssSelector("input[name='query']"),
            By.cssSelector("input[name='domain_name']"),
            By.cssSelector("input[name='domain']"),
            By.cssSelector("input[type='search']"),
            By.cssSelector("input[placeholder*='Search' i]"),
            By.cssSelector("input[placeholder*='domain' i]")
    );

    private static final List<By> SEARCH_NAV_LOCATORS = List.of(
            By.xpath("//a[contains(translate(normalize-space(.), 'SEARCH', 'search'), 'search')]"),
            By.cssSelector("a[href*='search']")
    );

    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public HomePage openHomePage() {
        driver.get(HOME_URL);
        waitForPageLoad();
        dismissCookieBanner();
        return this;
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isTitleValid() {
        String title = getPageTitle() == null ? "" : getPageTitle().toLowerCase();
        String url = driver.getCurrentUrl() == null ? "" : driver.getCurrentUrl().toLowerCase();
        return url.contains("hugedomains.com")
                && (title.contains("huge") || title.contains("domain") || !title.isBlank());
    }

    public SearchPage navigateToSearch() {
        dismissCookieBanner();
        if (findFirstVisible(SEARCH_BOX_LOCATORS) == null) {
            WebElement searchNav = findFirstVisible(SEARCH_NAV_LOCATORS);
            if (searchNav != null) {
                searchNav.click();
                waitForPageLoad();
            }
        }
        wait.until(driver1 -> findFirstVisible(SEARCH_BOX_LOCATORS) != null);
        return new SearchPage(driver);
    }

    private void dismissCookieBanner() {
        List<By> overlays = List.of(
                By.cssSelector("button#onetrust-accept-btn-handler"),
                By.xpath("//button[contains(translate(normalize-space(.), 'ACEPT', 'acept'), 'accept')]"),
                By.xpath("//button[contains(translate(normalize-space(.), 'AGREE', 'agree'), 'agree')]")
        );
        for (By locator : overlays) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    element.click();
                    return;
                }
            }
        }
    }

    private void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    private WebElement findFirstVisible(List<By> locators) {
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    return element;
                }
            }
        }
        return null;
    }
}
