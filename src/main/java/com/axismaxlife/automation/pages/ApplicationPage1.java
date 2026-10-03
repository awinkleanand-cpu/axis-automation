package com.axismaxlife.automation.pages;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.config.ConfigReader;
import org.openqa.selenium.WebDriver;

public class ApplicationPage1 extends BasePage {

    public ApplicationPage1(WebDriver driver) {
        super(driver);
    }

    public ApplicationPage2 fillAndProceed() {
        waitUtils.selectDropdownByLabel("Residential Status", ConfigReader.get("residential.status"));
        waitUtils.selectDropdownByLabel("Nationality", ConfigReader.get("nationality"));
        waitUtils.selectRadioOrLabel("Policy For", ConfigReader.get("policy.for"));
        waitUtils.selectDropdownByLabel("Objective", ConfigReader.get("objective"));
        waitUtils.setInputByLabel("Aadhaar", ConfigReader.get("aadhaar"));
        waitUtils.setInputByLabel("PAN", ConfigReader.get("pan"));
        waitUtils.setInputByLabel("Mobile", ConfigReader.get("mobile"));
        waitUtils.setInputByLabel("Email", ConfigReader.get("email"));
        proceed();
        return new ApplicationPage2(driver);
    }
}
