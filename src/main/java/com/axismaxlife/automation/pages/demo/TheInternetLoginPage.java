package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TheInternetLoginPage extends BasePage {

    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");

    public TheInternetLoginPage(WebDriver driver) {
        super(driver);
    }

    public TheInternetSecurePage login() {
        logStep("Login with published demo credentials");
        waitUtils.type(USERNAME, ConfigReader.get("theinternet.username"));
        waitUtils.type(PASSWORD, ConfigReader.get("theinternet.password"));
        waitUtils.click(LOGIN_BUTTON);
        waitUtils.waitForPageLoad();
        logStepWithScreenshot("Submitted login form");
        return new TheInternetSecurePage(driver);
    }
}
