package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HugeDomainsCheckoutPage extends HugeDomainsBasePage {

    private static final List<By> PAYMENT_MARKERS = List.of(
            By.xpath("//*[contains(translate(., 'CREDIT CARD', 'credit card'), 'credit card')]"),
            By.xpath("//*[contains(translate(., 'CARD NUMBER', 'card number'), 'card number')]"),
            By.xpath("//*[contains(translate(., 'PAYMENT', 'payment'), 'payment')]"),
            By.cssSelector("input[name*='card' i]"),
            By.cssSelector("input[id*='card' i]"),
            By.cssSelector("input[autocomplete='cc-number']"),
            By.cssSelector("iframe[src*='stripe' i], iframe[src*='braintree' i], iframe[src*='paypal' i], iframe[name*='card' i]")
    );

    private static final List<By> CONTINUE_LOCATORS = List.of(
            By.xpath("//button[contains(translate(normalize-space(.), 'CONTINUE', 'continue'), 'continue')]"),
            By.xpath("//a[contains(translate(normalize-space(.), 'CONTINUE', 'continue'), 'continue')]"),
            By.xpath("//button[contains(translate(normalize-space(.), 'NEXT', 'next'), 'next')]"),
            By.xpath("//button[contains(translate(normalize-space(.), 'PROCEED', 'proceed'), 'proceed')]"),
            By.xpath("//input[( @type='submit' or @type='button') and (contains(translate(@value, 'CONTINUE', 'continue'), 'continue') or contains(translate(@value, 'NEXT', 'next'), 'next'))]")
    );

    private static final List<By> EMAIL_LOCATORS = List.of(
            By.cssSelector("input[type='email']"),
            By.cssSelector("input[name*='email' i]"),
            By.cssSelector("input[id*='email' i]")
    );

    private static final List<By> FIRST_NAME_LOCATORS = List.of(
            By.cssSelector("input[name*='first' i]"),
            By.cssSelector("input[id*='first' i]"),
            By.cssSelector("input[autocomplete='given-name']")
    );

    private static final List<By> LAST_NAME_LOCATORS = List.of(
            By.cssSelector("input[name*='last' i]"),
            By.cssSelector("input[id*='last' i]"),
            By.cssSelector("input[autocomplete='family-name']")
    );

    public HugeDomainsCheckoutPage(WebDriver driver) {
        super(driver);
    }

    public HugeDomainsCheckoutPage continueToPaymentWithoutEnteringCard() {
        logStep("Advance checkout until the payment page; do not enter card details");
        fillGuestContactIfPresent();

        for (int attempt = 0; attempt < 4 && !isPaymentPageDisplayed(); attempt++) {
            if (!isAnyDisplayed(CONTINUE_LOCATORS)) {
                break;
            }
            clickFirstVisible(CONTINUE_LOCATORS, "Continue");
            guardAndCapture("Checkout step " + (attempt + 1));
            fillGuestContactIfPresent();
        }

        if (!isPaymentPageDisplayed() && !currentUrl().contains("payment") && !currentUrl().contains("checkout")) {
            driver.get("https://www.hugedomains.com/checkout.cfm");
            waitUtils.waitForPageLoad();
        }

        guardAndCapture("Stopped on payment / checkout page");
        return this;
    }

    public boolean isPaymentPageDisplayed() {
        String url = currentUrl();
        String text = lowerPageText();
        return isAnyDisplayed(PAYMENT_MARKERS)
                || url.contains("payment")
                || url.contains("checkout")
                || url.contains("billing")
                || text.contains("card number")
                || text.contains("credit card")
                || text.contains("paypal")
                || text.contains("payment method");
    }

    public boolean paymentFieldsAreEmpty() {
        List<By> cardFields = List.of(
                By.cssSelector("input[autocomplete='cc-number']"),
                By.cssSelector("input[name*='card' i]"),
                By.cssSelector("input[id*='cardnumber' i]")
        );
        for (By locator : cardFields) {
            for (WebElement field : driver.findElements(locator)) {
                if (field.isDisplayed()) {
                    String value = field.getAttribute("value");
                    if (value != null && !value.isBlank()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean didNotSubmitPayment() {
        String url = currentUrl();
        String text = lowerPageText();
        return !url.contains("confirmation")
                && !url.contains("receipt")
                && !text.contains("thank you for your order")
                && !text.contains("payment successful");
    }

    private void fillGuestContactIfPresent() {
        if (isAnyDisplayed(EMAIL_LOCATORS)) {
            typeIfBlank(EMAIL_LOCATORS, ConfigReader.getOptional("demo.guest.email", "test.buyer@example.com"));
        }
        if (isAnyDisplayed(FIRST_NAME_LOCATORS)) {
            typeIfBlank(FIRST_NAME_LOCATORS, ConfigReader.getOptional("demo.guest.first.name", "Test"));
        }
        if (isAnyDisplayed(LAST_NAME_LOCATORS)) {
            typeIfBlank(LAST_NAME_LOCATORS, ConfigReader.getOptional("demo.guest.last.name", "Buyer"));
        }
    }

    private void typeIfBlank(List<By> locators, String value) {
        WebElement field = findFirstVisible(locators, "Guest field");
        String current = field.getAttribute("value");
        if (current == null || current.isBlank()) {
            field.clear();
            field.sendKeys(value);
        }
    }
}
