package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.config.ConfigReader;
import com.axismaxlife.automation.utils.CaptchaGuard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SauceDemoLoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");

    public SauceDemoLoginPage(WebDriver driver) {
        super(driver);
    }

    public SauceDemoLoginPage open() {
        logStep("Open Sauce Demo: " + ConfigReader.get("saucedemo.url"));
        driver.get(ConfigReader.get("saucedemo.url"));
        waitUtils.waitForPageLoad();
        CaptchaGuard.assertNoBotChallenge(driver);
        logStepWithScreenshot("Sauce Demo login page loaded");
        return this;
    }

    public SauceDemoInventoryPage login() {
        return login(ConfigReader.get("saucedemo.username"), ConfigReader.get("saucedemo.password"));
    }

    public SauceDemoInventoryPage login(String username, String password) {
        logStep("Login as " + username);
        waitUtils.type(USERNAME, username);
        waitUtils.type(PASSWORD, password);
        waitUtils.click(LOGIN_BUTTON);
        waitUtils.waitForPageLoad();
        logStepWithScreenshot("Submitted Sauce Demo login");
        return new SauceDemoInventoryPage(driver);
    }
}
