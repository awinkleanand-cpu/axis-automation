package com.axismaxlife.automation.pages.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Locale;

public class HugeDomainsDomainPage extends HugeDomainsBasePage {

    private static final List<By> DOMAIN_HEADING_LOCATORS = List.of(
            By.cssSelector("h1"),
            By.cssSelector("[class*='domain'] h1"),
            By.cssSelector("[class*='domain-name']")
    );

    public HugeDomainsDomainPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDetailsPageDisplayed() {
        String url = currentUrl();
        String source = lowerPageText();
        boolean buyOrPriceVisible = isAnyDisplayed(ADD_TO_CART_LOCATORS)
                || !driver.findElements(By.xpath("//*[contains(text(), '$')]")).isEmpty();

        return url.contains("domain")
                || source.contains("buy now")
                || source.contains("add to cart")
                || source.contains("make an offer")
                || buyOrPriceVisible;
    }

    public boolean isAvailableForPurchase() {
        return isAnyDisplayed(ADD_TO_CART_LOCATORS)
                || lowerPageText().contains("for sale")
                || lowerPageText().contains("buy now");
    }

    public String listedDomainName() {
        for (By locator : DOMAIN_HEADING_LOCATORS) {
            List<WebElement> headings = driver.findElements(locator);
            for (WebElement heading : headings) {
                if (heading.isDisplayed() && heading.getText() != null && heading.getText().contains(".")) {
                    return heading.getText().trim();
                }
            }
        }
        return driver.getTitle();
    }

    public String pageTitle() {
        return driver.getTitle();
    }

    public HugeDomainsCartPage addToCart() {
        logStep("Add listed domain to cart: " + listedDomainName());
        clickFirstVisible(ADD_TO_CART_LOCATORS, "Add to Cart");
        if (currentUrl().contains("domain") && isAnyDisplayed(ADD_TO_CART_LOCATORS)) {
            clickFirstVisible(ADD_TO_CART_LOCATORS, "Add to Cart confirmation");
        }
        waitUtils.waitForPageLoad();
        if (!currentUrl().contains("cart") && !currentUrl().contains("checkout")) {
            driver.get("https://www.hugedomains.com/shopping_cart.cfm");
            waitUtils.waitForPageLoad();
        }
        guardAndCapture("Cart page after Add to Cart");
        return new HugeDomainsCartPage(driver);
    }

    public String availabilityStatus() {
        if (isAvailableForPurchase()) {
            return "FOR_SALE";
        }
        String text = lowerPageText();
        if (text.contains("not available") || text.contains("unavailable")) {
            return "UNAVAILABLE";
        }
        return "UNKNOWN";
    }

    public String normalizedDomainToken() {
        String name = listedDomainName().toLowerCase(Locale.ROOT);
        return name.replaceAll("[^a-z0-9.-]", " ").trim();
    }
}
