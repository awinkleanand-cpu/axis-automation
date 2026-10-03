package com.axismaxlife.automation.pages;

import com.axismaxlife.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ApplicationPage3 extends BasePage {

    public ApplicationPage3(WebDriver driver) {
        super(driver);
    }

    public ApplicationPage4 fillAndProceed() {
        fillRequiredFieldsWithDefaults();
        proceed();
        return new ApplicationPage4(driver);
    }

    protected void fillRequiredFieldsWithDefaults() {
        List<WebElement> requiredInputs = driver.findElements(By.xpath(
                "//input[@required and not(@type='hidden') and not(@type='checkbox') and not(@type='radio')]"
                        + " | //input[contains(@class, 'required') and not(@type='hidden')]"));

        for (WebElement input : requiredInputs) {
            String currentValue = input.getAttribute("value");
            if (!input.isDisplayed() || (currentValue != null && !currentValue.isBlank())) {
                continue;
            }
            String type = input.getAttribute("type");
            if ("email".equalsIgnoreCase(type)) {
                input.sendKeys("awinkle.anand@tothenew.com");
            } else if ("number".equalsIgnoreCase(type) || "tel".equalsIgnoreCase(type)) {
                input.sendKeys("7867898767");
            } else {
                input.sendKeys("NA");
            }
        }

        List<WebElement> requiredSelects = driver.findElements(By.xpath("//select[@required] | //select[contains(@class, 'required')]"));
        for (WebElement select : requiredSelects) {
            if (select.isDisplayed()) {
                waitUtils.selectByPartialText(By.xpath(generateXPath(select)), getFirstNonEmptyOption(select));
            }
        }

        List<WebElement> uncheckedRequiredCheckboxes = driver.findElements(By.xpath(
                "//input[@type='checkbox' and @required and not(@checked)]"));
        for (WebElement checkbox : uncheckedRequiredCheckboxes) {
            if (checkbox.isDisplayed()) {
                checkbox.click();
            }
        }
    }

    private String getFirstNonEmptyOption(WebElement selectElement) {
        List<WebElement> options = selectElement.findElements(By.tagName("option"));
        for (WebElement option : options) {
            String text = option.getText().trim();
            if (!text.isEmpty() && !text.equalsIgnoreCase("select") && !text.equalsIgnoreCase("--select--")) {
                return text;
            }
        }
        return options.get(1).getText().trim();
    }

    private String generateXPath(WebElement element) {
        String id = element.getAttribute("id");
        if (id != null && !id.isBlank()) {
            return "//select[@id='" + id + "']";
        }
        String name = element.getAttribute("name");
        if (name != null && !name.isBlank()) {
            return "//select[@name='" + name + "']";
        }
        throw new IllegalStateException("Unable to build xpath for select element");
    }
}
