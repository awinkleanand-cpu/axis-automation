package com.axismaxlife.automation.utils;

import com.axismaxlife.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public final class WaitUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds")));
    }

    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForPageLoad() {
        wait.until(webDriver -> "complete".equals(
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState")));
    }

    public void click(By locator) {
        waitForClickable(locator).click();
    }

    public void type(By locator, String value) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    public void selectByVisibleText(By locator, String value) {
        Select select = new Select(waitForVisible(locator));
        select.selectByVisibleText(value);
    }

    public void selectByPartialText(By locator, String partialText) {
        Select select = new Select(waitForVisible(locator));
        List<WebElement> options = select.getOptions();
        for (WebElement option : options) {
            if (option.getText().trim().toLowerCase().contains(partialText.toLowerCase())) {
                select.selectByVisibleText(option.getText().trim());
                return;
            }
        }
        throw new IllegalArgumentException("No option containing '" + partialText + "' found for " + locator);
    }

    public void selectRadioOrLabel(String labelText, String optionText) {
        List<WebElement> labels = driver.findElements(By.xpath(
                "//label[contains(normalize-space(.), '" + labelText + "')]/following::label[contains(normalize-space(.), '" + optionText + "')][1]"
                        + " | //span[contains(normalize-space(.), '" + labelText + "')]/following::label[contains(normalize-space(.), '" + optionText + "')][1]"
                        + " | //label[contains(normalize-space(.), '" + optionText + "')][ancestor::*[contains(., '" + labelText + "')]][1]"));
        if (!labels.isEmpty()) {
            labels.get(0).click();
            return;
        }

        List<WebElement> radioButtons = driver.findElements(By.xpath(
                "//*[contains(normalize-space(.), '" + labelText + "')]/following::input[@type='radio'][following::*[contains(normalize-space(.), '" + optionText + "')]][1]"));
        if (!radioButtons.isEmpty()) {
            radioButtons.get(0).click();
            return;
        }

        throw new IllegalArgumentException("Unable to select radio option '" + optionText + "' for label '" + labelText + "'");
    }

    public void setInputByLabel(String labelText, String value) {
        By locator = By.xpath(
                "//label[contains(normalize-space(.), '" + labelText + "')]/following::input[1]"
                        + " | //span[contains(normalize-space(.), '" + labelText + "')]/following::input[1]"
                        + " | //*[contains(normalize-space(.), '" + labelText + "')]/following::input[not(@type='hidden')][1]"
                        + " | //input[contains(@placeholder, '" + labelText + "')]"
                        + " | //input[contains(@name, '" + toFieldName(labelText) + "')]"
                        + " | //input[contains(@id, '" + toFieldName(labelText) + "')]");
        type(locator, value);
    }

    public void selectDropdownByLabel(String labelText, String value) {
        By locator = By.xpath(
                "//label[contains(normalize-space(.), '" + labelText + "')]/following::select[1]"
                        + " | //span[contains(normalize-space(.), '" + labelText + "')]/following::select[1]"
                        + " | //*[contains(normalize-space(.), '" + labelText + "')]/following::select[1]"
                        + " | //select[contains(@name, '" + toFieldName(labelText) + "')]"
                        + " | //select[contains(@id, '" + toFieldName(labelText) + "')]");
        selectByPartialText(locator, value);
    }

    public void uploadFile(By locator, String absolutePath) {
        WebElement element = waitForVisible(locator);
        element.sendKeys(absolutePath);
    }

    public void uploadByLabel(String labelText, String absolutePath) {
        By locator = By.xpath(
                "//label[contains(normalize-space(.), '" + labelText + "')]/following::input[@type='file'][1]"
                        + " | //*[contains(normalize-space(.), '" + labelText + "')]/following::input[@type='file'][1]"
                        + " | //input[@type='file'][contains(@name, '" + toFieldName(labelText) + "')]");
        uploadFile(locator, absolutePath);
    }

    public void clickProceed() {
        List<By> proceedLocators = List.of(
                By.xpath("//button[contains(normalize-space(.), 'Proceed')]"),
                By.xpath("//input[@type='submit' and contains(@value, 'Proceed')]"),
                By.xpath("//a[contains(normalize-space(.), 'Proceed')]"),
                By.xpath("//button[contains(normalize-space(.), 'Next')]"),
                By.xpath("//input[@type='submit' and contains(@value, 'Next')]")
        );

        for (By locator : proceedLocators) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                elements.get(0).click();
                waitForPageLoad();
                return;
            }
        }
        throw new IllegalStateException("Proceed/Next button not found on page");
    }

    public void clickButtonByText(String buttonText) {
        By locator = By.xpath(
                "//button[contains(normalize-space(.), '" + buttonText + "')]"
                        + " | //a[contains(normalize-space(.), '" + buttonText + "')]"
                        + " | //input[@type='button' and contains(@value, '" + buttonText + "')]"
                        + " | //input[@type='submit' and contains(@value, '" + buttonText + "')]");
        click(locator);
        waitForPageLoad();
    }

    public void checkCheckboxByLabel(String labelText) {
        List<WebElement> checkboxes = driver.findElements(By.xpath(
                "//label[contains(normalize-space(.), '" + labelText + "')]/preceding::input[@type='checkbox'][1]"
                        + " | //label[contains(normalize-space(.), '" + labelText + "')]/input[@type='checkbox']"
                        + " | //input[@type='checkbox'][following::*[contains(normalize-space(.), '" + labelText + "')]][1]"));
        if (!checkboxes.isEmpty()) {
            WebElement checkbox = checkboxes.get(0);
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
            return;
        }
        click(By.xpath("//label[contains(normalize-space(.), '" + labelText + "')]"));
    }

    private static String toFieldName(String labelText) {
        return labelText.toLowerCase().replaceAll("[^a-z0-9]+", "");
    }
}
