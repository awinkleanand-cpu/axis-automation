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
        return login(ConfigReader.get("theinternet.username"), ConfigReader.get("theinternet.password"));
    }

    public TheInternetSecurePage login(String username, String password) {
        logStep("Login as " + username);
        waitUtils.type(USERNAME, username);
        waitUtils.type(PASSWORD, password);
        waitUtils.click(LOGIN_BUTTON);
        waitUtils.waitForPageLoad();
        logStepWithScreenshot("Submitted login form");
        return new TheInternetSecurePage(driver);
    }
}
