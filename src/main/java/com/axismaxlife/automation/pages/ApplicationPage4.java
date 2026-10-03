package com.axismaxlife.automation.pages;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.utils.RequiredFieldHelper;
import org.openqa.selenium.WebDriver;

public class ApplicationPage4 extends BasePage {

    private final RequiredFieldHelper requiredFieldHelper;

    public ApplicationPage4(WebDriver driver) {
        super(driver);
        this.requiredFieldHelper = new RequiredFieldHelper(driver);
    }

    public ApplicationPage5 fillAndProceed() {
        logStep("Fill Application Page 4 - Required Details");
        requiredFieldHelper.fillRequiredFieldsWithDefaults();
        logStepWithScreenshot("Application Page 4 completed");
        proceed();
        return new ApplicationPage5(driver);
    }
}
