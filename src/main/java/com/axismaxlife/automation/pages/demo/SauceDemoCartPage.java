package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SauceDemoCartPage extends BasePage {

    private static final By CHECKOUT_BUTTON = By.id("checkout");
    private static final By CART_ITEM = By.className("cart_item");

    public SauceDemoCartPage(WebDriver driver) {
        super(driver);
    }

    public boolean hasItems() {
        return !driver.findElements(CART_ITEM).isEmpty();
    }

    public SauceDemoCheckoutPage checkout() {
        logStep("Start checkout");
        waitUtils.click(CHECKOUT_BUTTON);
        waitUtils.waitForPageLoad();
        logStepWithScreenshot("Checkout information page");
        return new SauceDemoCheckoutPage(driver);
    }
}
