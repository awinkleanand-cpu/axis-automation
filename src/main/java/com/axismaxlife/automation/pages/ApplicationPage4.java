package com.axismaxlife.automation.pages;

import org.openqa.selenium.WebDriver;

public class ApplicationPage4 extends ApplicationPage3 {

    public ApplicationPage4(WebDriver driver) {
        super(driver);
    }

    public ApplicationPage5 fillAndProceed() {
        fillRequiredFieldsWithDefaults();
        proceed();
        return new ApplicationPage5(driver);
    }
}
