package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SauceDemoInventoryPage extends BasePage {

    private static final By INVENTORY_CONTAINER = By.id("inventory_container");
    private static final By ADD_BACKPACK = By.id("add-to-cart-sauce-labs-backpack");
    private static final By CART_LINK = By.className("shopping_cart_link");
    private static final By CART_BADGE = By.className("shopping_cart_badge");

    public SauceDemoInventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return !driver.findElements(INVENTORY_CONTAINER).isEmpty();
    }

    public SauceDemoCartPage addBackpackAndOpenCart() {
        logStep("Add Sauce Labs Backpack to cart");
        waitUtils.click(ADD_BACKPACK);
        logStepWithScreenshot("Item added. Cart count: " + waitUtils.waitForVisible(CART_BADGE).getText());
        waitUtils.click(CART_LINK);
        waitUtils.waitForPageLoad();
        return new SauceDemoCartPage(driver);
    }
}
