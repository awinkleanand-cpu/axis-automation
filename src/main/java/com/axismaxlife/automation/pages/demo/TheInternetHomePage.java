package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.config.ConfigReader;
import com.axismaxlife.automation.utils.CaptchaGuard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TheInternetHomePage extends BasePage {

    private static final By FORM_AUTH_LINK = By.linkText("Form Authentication");

    public TheInternetHomePage(WebDriver driver) {
        super(driver);
    }

    public TheInternetHomePage open() {
        logStep("Open The Internet: " + ConfigReader.get("theinternet.url"));
        driver.get(ConfigReader.get("theinternet.url"));
        waitUtils.waitForPageLoad();
        CaptchaGuard.assertNoBotChallenge(driver);
        logStepWithScreenshot("The Internet home page loaded");
        return this;
    }

    public TheInternetLoginPage openFormAuthentication() {
        String loginUrl = ConfigReader.get("theinternet.url").replaceAll("/$", "") + "/login";
        logStep("Open Form Authentication: " + loginUrl);
        driver.get(loginUrl);
        waitUtils.waitForPageLoad();
        waitUtils.waitForVisible(By.id("username"));
        logStepWithScreenshot("Login form displayed");
        return new TheInternetLoginPage(driver);
    }
}
