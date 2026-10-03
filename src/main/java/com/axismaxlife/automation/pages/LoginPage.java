package com.axismaxlife.automation.pages;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USER_ID = By.xpath(
            "//input[contains(@placeholder, 'User') or contains(@name, 'user') or contains(@id, 'user')]");
    private static final By PASSWORD = By.xpath(
            "//input[@type='password' or contains(@name, 'password') or contains(@id, 'password')]");
    private static final By LOGIN_BUTTON = By.xpath(
            "//button[contains(normalize-space(.), 'Login') or contains(normalize-space(.), 'Sign In')]"
                    + " | //input[@type='submit' and (contains(@value, 'Login') or contains(@value, 'Sign In'))]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(ConfigReader.get("base.url"));
        waitUtils.waitForPageLoad();
        return this;
    }

    public DashboardPage login() {
        waitUtils.type(USER_ID, ConfigReader.get("user.id"));
        waitUtils.type(PASSWORD, ConfigReader.get("password"));
        waitUtils.click(LOGIN_BUTTON);
        waitUtils.waitForPageLoad();
        return new DashboardPage(driver);
    }
}
