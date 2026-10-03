package com.axismaxlife.automation.pages.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Locale;

public class HugeDomainsSearchResultsPage extends HugeDomainsBasePage {

    private static final By RESULT_LINKS = By.xpath(
            "//a[contains(@href, 'domain_profile') or contains(@href, 'domain')]"
                    + "[contains(., '.') or contains(@href, '.com') or contains(@href, 'domain_profile')]"
                    + "[not(contains(@href, 'javascript'))]");

    private static final List<By> STATUS_LOCATORS = List.of(
            By.xpath("//*[contains(translate(., 'AVAILABLE', 'available'), 'available')]"),
            By.xpath("//*[contains(translate(., 'FOR SALE', 'for sale'), 'for sale')]"),
            By.xpath("//*[contains(translate(., 'NOT AVAILABLE', 'not available'), 'not available')]"),
            By.xpath("//*[contains(translate(., 'NO RESULTS', 'no results'), 'no results')]"),
            By.xpath("//*[contains(translate(., 'PRICE', 'price'), 'price')]"),
            By.xpath("//*[contains(text(), '$')]")
    );

    public HugeDomainsSearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilLoaded() {
        waitUtils.waitForPageLoad();
        waitUtils.waitForFirstVisible(List.of(
                RESULT_LINKS,
                ADD_TO_CART_LOCATORS.get(0),
                STATUS_LOCATORS.get(0),
                By.xpath("//h1 | //h2 | //*[contains(@class, 'result')]")
        ));
    }

    public boolean isLoaded() {
        String url = currentUrl();
        return url.contains("search")
                || url.contains("domain")
                || hasResults()
                || showsAvailabilityStatus();
    }

    public boolean hasResults() {
        return !driver.findElements(RESULT_LINKS).isEmpty()
                || isAnyDisplayed(ADD_TO_CART_LOCATORS)
                || lowerPageText().contains("domain");
    }

    public boolean showsAvailabilityStatus() {
        return isAnyDisplayed(STATUS_LOCATORS)
                || isAnyDisplayed(ADD_TO_CART_LOCATORS)
                || availabilityStatus() != null;
    }

    public String availabilityStatus() {
        String text = lowerPageText();
        if (canAddToCart()) {
            return "FOR_SALE";
        }
        if (text.contains("not available") || text.contains("unavailable") || text.contains("taken")) {
            return "UNAVAILABLE";
        }
        if (text.contains("no results") || text.contains("no domains") || text.contains("didn't find")
                || text.contains("did not find") || text.contains("0 results")) {
            return "NO_RESULTS";
        }
        if (hasResults() || text.contains("for sale") || text.contains("available") || text.contains("$")) {
            return "LISTED";
        }
        return null;
    }

    public boolean canAddToCart() {
        return isAnyDisplayed(ADD_TO_CART_LOCATORS);
    }

    public HugeDomainsCartPage addToCart() {
        logStep("Add domain to cart from search results");
        clickFirstVisible(ADD_TO_CART_LOCATORS, "Add to Cart");
        guardAndCapture("Cart after adding domain from results");
        return new HugeDomainsCartPage(driver);
    }

    public HugeDomainsDomainPage openAvailableListing() {
        if (canAddToCart() && currentUrl().contains("domain_profile")) {
            return new HugeDomainsDomainPage(driver);
        }
        return openFirstResult();
    }

    public HugeDomainsDomainPage openFirstResult() {
        List<WebElement> links = driver.findElements(RESULT_LINKS);
        WebElement firstVisible = links.stream()
                .filter(WebElement::isDisplayed)
                .filter(link -> {
                    String text = link.getText();
                    String href = link.getAttribute("href");
                    return (text != null && text.contains("."))
                            || (href != null && href.toLowerCase(Locale.ROOT).contains("domain_profile"));
                })
                .findFirst()
                .orElseGet(() -> links.stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "No search result links found. URL: " + driver.getCurrentUrl())));

        logStep("Open domain listing: " + firstVisible.getText());
        clickSafely(firstVisible);
        waitUtils.waitForPageLoad();
        guardAndCapture("Domain details page loaded");
        return new HugeDomainsDomainPage(driver);
    }
}
