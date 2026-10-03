package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SauceDemoCheckoutPage extends BasePage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE_BUTTON = By.id("continue");
    private static final By FINISH_BUTTON = By.id("finish");
    private static final By COMPLETE_HEADER = By.className("complete-header");

    public SauceDemoCheckoutPage(WebDriver driver) {
        super(driver);
    }

    public SauceDemoCheckoutPage fillCustomerDetails() {
        logStep("Fill checkout customer details");
        waitUtils.type(FIRST_NAME, ConfigReader.get("saucedemo.first.name"));
        waitUtils.type(LAST_NAME, ConfigReader.get("saucedemo.last.name"));
        waitUtils.type(POSTAL_CODE, ConfigReader.get("saucedemo.postal.code"));
        waitUtils.click(CONTINUE_BUTTON);
        waitUtils.waitForPageLoad();
        logStepWithScreenshot("Checkout overview displayed");
        return this;
    }

    public SauceDemoCheckoutPage finishOrder() {
        logStep("Finish checkout");
        waitUtils.click(FINISH_BUTTON);
        waitUtils.waitForPageLoad();
        logStepWithScreenshot("Order complete page");
        return this;
    }

    public boolean isOrderComplete() {
        return waitUtils.waitForVisible(COMPLETE_HEADER).getText().toLowerCase().contains("thank you");
    }
}
