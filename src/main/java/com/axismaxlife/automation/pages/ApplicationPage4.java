package com.axismaxlife.automation.pages;

import org.openqa.selenium.WebDriver;

public class ApplicationPage4 extends ApplicationPage3 {

    public ApplicationPage4(WebDriver driver) {
        super(driver);
    }

    public ApplicationPage5 fillAndProceed() {
        logStep("Fill Application Page 4 - Required Details");
        fillRequiredFieldsWithDefaults();
        logStepWithScreenshot("Application Page 4 completed");
        proceed();
        return new ApplicationPage5(driver);
    }
}
