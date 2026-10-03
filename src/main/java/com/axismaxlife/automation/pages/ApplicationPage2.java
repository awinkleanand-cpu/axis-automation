package com.axismaxlife.automation.pages;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.config.ConfigReader;
import org.openqa.selenium.WebDriver;

public class ApplicationPage2 extends BasePage {

    public ApplicationPage2(WebDriver driver) {
        super(driver);
    }

    public ApplicationPage3 fillAndProceed() {
        logStep("Fill Application Page 2 - Proposer Details");
        waitUtils.setInputByLabel("Name", ConfigReader.get("proposer.name"));
        waitUtils.setInputByLabel("DOB", ConfigReader.get("dob"));
        waitUtils.setInputByLabel("Date of Birth", ConfigReader.get("dob"));
        waitUtils.setInputByLabel("Address", ConfigReader.get("address"));
        waitUtils.selectDropdownByLabel("Purpose", ConfigReader.get("purpose"));
        waitUtils.selectDropdownByLabel("Life Stage", ConfigReader.get("life.stage"));
        waitUtils.setInputByLabel("Gross Annual Income", ConfigReader.get("gross.annual.income"));
        waitUtils.selectDropdownByLabel("Occupation", ConfigReader.get("occupation"));
        waitUtils.selectRadioOrLabel("Existing Policy", ConfigReader.get("existing.policy"));
        waitUtils.selectDropdownByLabel("Recommended Product", ConfigReader.get("recommended.product"));
        logStepWithScreenshot("Application Page 2 completed");
        proceed();
        return new ApplicationPage3(driver);
    }
}
