package com.axismaxlife.automation.pages;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ApplicationPage6 extends BasePage {

    private static final By SUBMIT_BUTTON = By.xpath(
            "//button[contains(normalize-space(.), 'Submit')]"
                    + " | //input[@type='submit' and contains(@value, 'Submit')]"
                    + " | //a[contains(normalize-space(.), 'Submit')]");

    public ApplicationPage6(WebDriver driver) {
        super(driver);
    }

    public ApplicationPage6 fillDetails() {
        logStep("Fill Application Page 6 - Payment and Documents");
        waitUtils.selectRadioOrLabel("Payor different from Proposer", ConfigReader.get("payor.different"));
        waitUtils.selectDropdownByLabel("Industry", ConfigReader.get("industry"));
        waitUtils.setInputByLabel("Duties", ConfigReader.get("duties"));
        waitUtils.selectRadioOrLabel("e-Insurance", ConfigReader.get("einsurance"));
        waitUtils.selectRadioOrLabel("Payment", ConfigReader.get("payment.method"));
        waitUtils.checkCheckboxByLabel("Terms");
        waitUtils.checkCheckboxByLabel("Agree");
        uploadRequiredDocuments();
        logStepWithScreenshot("Application Page 6 completed");
        return this;
    }

    private void uploadRequiredDocuments() {
        List<WebElement> fileInputs = driver.findElements(By.xpath("//input[@type='file']"));
        String[] documents = {
                ConfigReader.resolvePath("document.pan").toString(),
                ConfigReader.resolvePath("document.aadhaar").toString(),
                ConfigReader.resolvePath("document.photo").toString()
        };

        int documentIndex = 0;
        for (WebElement fileInput : fileInputs) {
            if (!fileInput.isDisplayed()) {
                continue;
            }
            String path = documents[Math.min(documentIndex, documents.length - 1)];
            fileInput.sendKeys(path);
            documentIndex++;
        }

        if (fileInputs.isEmpty()) {
            waitUtils.uploadByLabel("PAN", ConfigReader.resolvePath("document.pan").toString());
            waitUtils.uploadByLabel("Aadhaar", ConfigReader.resolvePath("document.aadhaar").toString());
            waitUtils.uploadByLabel("Photo", ConfigReader.resolvePath("document.photo").toString());
        }
    }

    public void submitApplication() {
        logStep("Submit insurance application");
        waitUtils.click(SUBMIT_BUTTON);
        waitUtils.waitForPageLoad();
        logStepWithScreenshot("Application submitted");
    }

    public boolean isSubmissionSuccessful() {
        String pageSource = driver.getPageSource().toLowerCase();
        return pageSource.contains("success")
                || pageSource.contains("submitted")
                || pageSource.contains("thank you")
                || pageSource.contains("application number");
    }
}
