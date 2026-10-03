package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TheInternetSecurePage extends BasePage {

    private static final By FLASH = By.id("flash");
    private static final By LOGOUT_BUTTON = By.cssSelector("a.button[href='/logout']");

    public TheInternetSecurePage(WebDriver driver) {
        super(driver);
    }

    public String flashMessage() {
        return waitUtils.waitForVisible(FLASH).getText();
    }

    public boolean isLoggedIn() {
        String message = flashMessage().toLowerCase();
        return driver.getCurrentUrl().contains("/secure") && message.contains("logged into");
    }

    public TheInternetLoginPage logout() {
        logStep("Logout from secure area");
        waitUtils.click(LOGOUT_BUTTON);
        waitUtils.waitForUrlContains("/login");
        waitUtils.waitForTextPresent(FLASH, "logged out");
        waitUtils.waitForPageLoad();
        logStepWithScreenshot("Logged out");
        return new TheInternetLoginPage(driver);
    }

    public boolean isLoggedOut() {
        waitUtils.waitForUrlContains("/login");
        String message = flashMessage().toLowerCase();
        return driver.getCurrentUrl().contains("/login") && message.contains("logged out");
    }
}
