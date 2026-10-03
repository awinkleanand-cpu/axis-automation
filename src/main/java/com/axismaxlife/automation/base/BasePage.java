package com.axismaxlife.automation.base;

import com.axismaxlife.automation.utils.WaitUtils;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils waitUtils;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    protected void proceed() {
        waitUtils.clickProceed();
    }
}
