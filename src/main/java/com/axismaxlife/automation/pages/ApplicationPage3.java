package com.axismaxlife.automation.pages;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.utils.RequiredFieldHelper;
import org.openqa.selenium.WebDriver;

public class ApplicationPage3 extends BasePage {

    private final RequiredFieldHelper requiredFieldHelper;

    public ApplicationPage3(WebDriver driver) {
        super(driver);
        this.requiredFieldHelper = new RequiredFieldHelper(driver);
    }

    public ApplicationPage4 fillAndProceed() {
        logStep("Fill Application Page 3 - Required Details");
        requiredFieldHelper.fillRequiredFieldsWithDefaults();
        logStepWithScreenshot("Application Page 3 completed");
        proceed();
        return new ApplicationPage4(driver);
    }
}
