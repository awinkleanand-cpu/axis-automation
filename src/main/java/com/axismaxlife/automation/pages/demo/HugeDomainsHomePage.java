package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.config.ConfigReader;
import com.axismaxlife.automation.utils.CaptchaGuard;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HugeDomainsHomePage extends BasePage {

    private static final List<By> SEARCH_BOX_LOCATORS = List.of(
            By.cssSelector("input[name='query']"),
            By.cssSelector("input[type='search']"),
            By.cssSelector("input[placeholder*='Search' i]"),
            By.cssSelector("input[placeholder*='domain' i]"),
            By.xpath("//input[@type='text' and not(@type='hidden')]")
    );

    private static final List<By> SEARCH_BUTTON_LOCATORS = List.of(
            By.xpath("//button[contains(translate(normalize-space(.), 'SEARCH', 'search'), 'search')]"),
            By.cssSelector("button[type='submit']"),
            By.cssSelector("input[type='submit']")
    );

    public HugeDomainsHomePage(WebDriver driver) {
        super(driver);
    }

    public HugeDomainsHomePage open() {
        logStep("Open HugeDomains home: " + ConfigReader.get("demo.url"));
        driver.get(ConfigReader.get("demo.url"));
        waitUtils.waitForPageLoad();
        CaptchaGuard.assertNoBotChallenge(driver);
        logStepWithScreenshot("Home page loaded - title: " + driver.getTitle());
        return this;
    }

    public HugeDomainsSearchResultsPage search(String keyword) {
        logStep("Search for domain keyword: " + keyword);
        WebElement searchBox = findFirstVisible(SEARCH_BOX_LOCATORS, "Search box");
        searchBox.clear();
        searchBox.sendKeys(keyword);

        boolean clickedSearch = false;
        for (By locator : SEARCH_BUTTON_LOCATORS) {
            List<WebElement> buttons = driver.findElements(locator);
            if (!buttons.isEmpty() && buttons.get(0).isDisplayed()) {
                buttons.get(0).click();
                clickedSearch = true;
                break;
            }
        }
        if (!clickedSearch) {
            searchBox.sendKeys(Keys.ENTER);
        }

        waitUtils.waitForPageLoad();
        CaptchaGuard.assertNoBotChallenge(driver);
        logStepWithScreenshot("Search results loaded for: " + keyword);
        return new HugeDomainsSearchResultsPage(driver);
    }

    private WebElement findFirstVisible(List<By> locators, String fieldName) {
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                return waitUtils.waitForVisible(locator);
            }
        }
        throw new IllegalStateException(fieldName + " not found. Current URL: " + driver.getCurrentUrl());
    }
}
