package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.utils.CaptchaGuard;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

abstract class HugeDomainsBasePage extends BasePage {

    private static final List<By> OVERLAY_LOCATORS = List.of(
            By.xpath("//button[contains(translate(normalize-space(.), 'ACEPT', 'acept'), 'accept')]"),
            By.xpath("//button[contains(translate(normalize-space(.), 'AGREE', 'agree'), 'agree')]"),
            By.xpath("//button[contains(translate(normalize-space(.), 'GOT IT', 'got it'), 'got it')]"),
            By.xpath("//button[contains(translate(normalize-space(.), 'CLOSE', 'close'), 'close')]"),
            By.cssSelector("button#onetrust-accept-btn-handler")
    );

    protected static final List<By> ADD_TO_CART_LOCATORS = List.of(
            By.xpath("//button[contains(translate(normalize-space(.), 'ADD TO CART', 'add to cart'), 'add to cart')]"),
            By.xpath("//a[contains(translate(normalize-space(.), 'ADD TO CART', 'add to cart'), 'add to cart')]"),
            By.xpath("//input[( @type='submit' or @type='button') and contains(translate(@value, 'ADD TO CART', 'add to cart'), 'add to cart')]"),
            By.xpath("//button[contains(translate(normalize-space(.), 'BUY NOW', 'buy now'), 'buy now')]"),
            By.xpath("//a[contains(translate(normalize-space(.), 'BUY NOW', 'buy now'), 'buy now')]"),
            By.cssSelector("a[href*='shopping_cart']"),
            By.cssSelector("a[href*='cart']")
    );

    protected HugeDomainsBasePage(WebDriver driver) {
        super(driver);
    }

    protected void guardAndCapture(String step) {
        waitUtils.waitForPageLoad();
        CaptchaGuard.assertNoBotChallenge(driver);
        logStepWithScreenshot(step);
    }

    protected void dismissOverlays() {
        for (By locator : OVERLAY_LOCATORS) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    clickSafely(element);
                    return;
                }
            }
        }
    }

    protected WebElement findFirstVisible(List<By> locators, String fieldName) {
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    return element;
                }
            }
        }
        throw new IllegalStateException(fieldName + " not found. Current URL: " + driver.getCurrentUrl());
    }

    protected boolean isAnyDisplayed(List<By> locators) {
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void clickFirstVisible(List<By> locators, String fieldName) {
        clickSafely(findFirstVisible(locators, fieldName));
        waitUtils.waitForPageLoad();
    }

    protected void clickSafely(WebElement element) {
        try {
            element.click();
        } catch (RuntimeException exception) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected String lowerPageText() {
        return driver.getPageSource().toLowerCase();
    }

    protected String currentUrl() {
        return driver.getCurrentUrl().toLowerCase();
    }
}
