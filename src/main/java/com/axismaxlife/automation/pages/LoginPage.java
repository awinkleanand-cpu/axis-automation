package com.axismaxlife.automation.pages;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LoginPage extends BasePage {

    private static final List<By> USER_ID_LOCATORS = List.of(
            By.xpath("//input[contains(@placeholder, 'User') or contains(@name, 'user') or contains(@id, 'user')]"),
            By.xpath("//input[contains(@placeholder, 'ID') or contains(@name, 'userid') or contains(@id, 'userid')]"),
            By.xpath("//input[@type='text' and not(@type='hidden')][1]"),
            By.name("username"),
            By.id("username")
    );

    private static final List<By> PASSWORD_LOCATORS = List.of(
            By.xpath("//input[@type='password']"),
            By.name("password"),
            By.id("password")
    );

    private static final List<By> LOGIN_BUTTON_LOCATORS = List.of(
            By.xpath("//button[contains(normalize-space(.), 'Login') or contains(normalize-space(.), 'Sign In')]"),
            By.xpath("//input[@type='submit' and (contains(@value, 'Login') or contains(@value, 'Sign In'))]"),
            By.xpath("//a[contains(normalize-space(.), 'Login')]")
    );

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        logStep("Navigate to login page: " + ConfigReader.get("base.url"));
        driver.get(ConfigReader.get("base.url"));
        waitUtils.waitForPageLoad();
        switchToLoginFrameIfPresent();
        logStepWithScreenshot("Login page loaded - title: " + driver.getTitle());
        return this;
    }

    public DashboardPage login() {
        logStep("Enter User ID and Password");
        WebElement userIdField = findFirstVisible(USER_ID_LOCATORS, "User ID");
        WebElement passwordField = findFirstVisible(PASSWORD_LOCATORS, "Password");

        userIdField.clear();
        userIdField.sendKeys(ConfigReader.get("user.id"));
        passwordField.clear();
        passwordField.sendKeys(ConfigReader.get("password"));

        findFirstVisible(LOGIN_BUTTON_LOCATORS, "Login button").click();
        driver.switchTo().defaultContent();
        waitUtils.waitForPageLoad();
        logStepWithScreenshot("Login successful");
        return new DashboardPage(driver);
    }

    private void switchToLoginFrameIfPresent() {
        List<WebElement> frames = driver.findElements(By.tagName("iframe"));
        for (WebElement frame : frames) {
            try {
                driver.switchTo().frame(frame);
                if (isAnyLocatorVisible(USER_ID_LOCATORS) || isAnyLocatorVisible(PASSWORD_LOCATORS)) {
                    logStep("Switched to login iframe");
                    return;
                }
                driver.switchTo().defaultContent();
            } catch (Exception exception) {
                driver.switchTo().defaultContent();
            }
        }
    }

    private boolean isAnyLocatorVisible(List<By> locators) {
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    private WebElement findFirstVisible(List<By> locators, String fieldName) {
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                return waitUtils.waitForVisible(locator);
            }
        }
        throw new IllegalStateException(fieldName + " field not found on login page. Current URL: " + driver.getCurrentUrl());
    }
}
