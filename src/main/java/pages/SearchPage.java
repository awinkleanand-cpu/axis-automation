package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class SearchPage {

    public static final String EXPECTED_INVALID_DOMAIN_MESSAGE =
            "Invalid domain name. Please enter a valid domain and try again.";

    private static final List<By> SEARCH_BOX_LOCATORS = List.of(
            By.cssSelector("input[name='query']"),
            By.cssSelector("input[name='domain_name']"),
            By.cssSelector("input[name='domain']"),
            By.cssSelector("input[type='search']"),
            By.cssSelector("input[placeholder*='Search' i]"),
            By.cssSelector("input[placeholder*='domain' i]")
    );

    private static final List<By> SEARCH_BUTTON_LOCATORS = List.of(
            By.xpath("//button[contains(translate(normalize-space(.), 'SEARCH', 'search'), 'search')]"),
            By.cssSelector("button[type='submit']"),
            By.cssSelector("input[type='submit']")
    );

    private static final By RESULT_LINKS = By.xpath(
            "//a[contains(@href, 'domain_profile') or contains(@href, 'domain')]"
                    + "[contains(., '.') or contains(@href, '.com') or contains(@href, 'domain_profile')]"
                    + "[not(contains(@href, 'javascript'))]");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public SearchPage enterDomainName(String domainName) {
        WebElement searchBox = wait.until(driver1 -> findFirstVisible(SEARCH_BOX_LOCATORS));
        searchBox.clear();
        searchBox.sendKeys(domainName);
        return this;
    }

    public SearchPage clickSearch() {
        WebElement searchBox = findFirstVisible(SEARCH_BOX_LOCATORS);
        WebElement searchButton = findFirstVisible(SEARCH_BUTTON_LOCATORS);
        if (searchButton != null) {
            searchButton.click();
        } else if (searchBox != null) {
            searchBox.sendKeys(Keys.ENTER);
        }
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        wait.until(driver1 -> hasResults() || getDisplayedInvalidDomainMessage() != null || pageText().contains("domain"));
        return this;
    }

    public boolean hasResults() {
        String url = currentUrl();
        String text = pageText();
        boolean resultLinksPresent = !driver.findElements(RESULT_LINKS).isEmpty();
        return resultLinksPresent
                || url.contains("search")
                || url.contains("domain")
                || text.contains("for sale")
                || text.contains("available")
                || text.contains("$");
    }

    public String getDisplayedInvalidDomainMessage() {
        String text = pageText();
        if (text.contains("invalid domain")
                || text.contains("please enter a valid domain")
                || text.contains("no matching domains")
                || text.contains("no results")) {
            return text;
        }
        return null;
    }

    public boolean showsExpectedInvalidDomainMessage() {
        String displayed = getDisplayedInvalidDomainMessage();
        return displayed != null && displayed.contains(EXPECTED_INVALID_DOMAIN_MESSAGE.toLowerCase(Locale.ROOT));
    }

    private String currentUrl() {
        return driver.getCurrentUrl() == null ? "" : driver.getCurrentUrl().toLowerCase(Locale.ROOT);
    }

    private String pageText() {
        return driver.getPageSource() == null ? "" : driver.getPageSource().toLowerCase(Locale.ROOT);
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
