package com.axismaxlife.automation.pages.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Locale;

public class HugeDomainsCartPage extends HugeDomainsBasePage {

    private static final List<By> CHECKOUT_LOCATORS = List.of(
            By.xpath("//button[contains(translate(normalize-space(.), 'CHECKOUT', 'checkout'), 'checkout')]"),
            By.xpath("//a[contains(translate(normalize-space(.), 'CHECKOUT', 'checkout'), 'checkout')]"),
            By.xpath("//input[( @type='submit' or @type='button') and contains(translate(@value, 'CHECKOUT', 'checkout'), 'checkout')]"),
            By.xpath("//button[contains(translate(normalize-space(.), 'PROCEED', 'proceed'), 'proceed')]"),
            By.xpath("//a[contains(translate(normalize-space(.), 'PROCEED', 'proceed'), 'proceed')]"),
            By.xpath("//button[contains(translate(normalize-space(.), 'CONTINUE', 'continue'), 'continue')]"),
            By.cssSelector("a[href*='checkout']"),
            By.cssSelector("a[href*='payment']")
    );

    private static final List<By> CART_MARKERS = List.of(
            By.xpath("//*[contains(translate(., 'SHOPPING CART', 'shopping cart'), 'shopping cart')]"),
            By.xpath("//*[contains(translate(., 'YOUR CART', 'your cart'), 'your cart')]"),
            By.xpath("//*[contains(translate(normalize-space(.), 'CART', 'cart'), 'cart')]")
    );

    public HugeDomainsCartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        String url = currentUrl();
        return url.contains("cart")
                || url.contains("checkout")
                || isAnyDisplayed(CART_MARKERS)
                || isAnyDisplayed(CHECKOUT_LOCATORS);
    }

    public boolean containsDomain(String domainToken) {
        if (domainToken == null || domainToken.isBlank()) {
            return !lowerPageText().isBlank();
        }
        String haystack = lowerPageText();
        String needle = domainToken.toLowerCase(Locale.ROOT).trim();
        String sld = needle.contains(".") ? needle.substring(0, needle.indexOf('.')) : needle;
        return haystack.contains(needle) || (!sld.isBlank() && haystack.contains(sld));
    }

    public HugeDomainsCheckoutPage proceedToCheckout() {
        logStep("Proceed from cart to checkout");
        if (isAnyDisplayed(CHECKOUT_LOCATORS)) {
            clickFirstVisible(CHECKOUT_LOCATORS, "Checkout");
        } else if (!currentUrl().contains("checkout")) {
            driver.get("https://www.hugedomains.com/checkout.cfm");
            waitUtils.waitForPageLoad();
        }
        guardAndCapture("Checkout page loaded");
        return new HugeDomainsCheckoutPage(driver);
    }
}
