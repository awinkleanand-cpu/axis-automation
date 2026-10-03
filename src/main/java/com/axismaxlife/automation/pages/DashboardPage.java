package com.axismaxlife.automation.pages;

import com.axismaxlife.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    private static final By NEW_APPLICATION_BUTTON = By.xpath(
            "//button[contains(normalize-space(.), 'NEW Application')]"
                    + " | //a[contains(normalize-space(.), 'NEW Application')]"
                    + " | //*[contains(normalize-space(.), 'NEW Application') and (self::button or self::a or self::span)]");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public ApplicationPage1 startNewApplication() {
        waitUtils.click(NEW_APPLICATION_BUTTON);
        waitUtils.waitForPageLoad();
        return new ApplicationPage1(driver);
    }
}
